/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.annotation.RedisCacheable;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.PreferPropsType;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.domain.HomeContentType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.client.model.MetaVideoRequestVo;
import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.model.dto.preference.PreferSimilarArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Artist;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import com.sktechx.godmusic.personal.rest.model.vo.preference.ChartResponse;
import com.sktechx.godmusic.personal.rest.model.vo.preference.PreferenceSimilarArtistListRedisWrapper;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.repository.*;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import com.sktechx.godmusic.personal.rest.service.impl.recommend.panel.assembly.v2.PreferArtistVideoPanelAssembly;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.*;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 7. 19.
 */
@Slf4j
@Service
public class PreferenceServiceImpl implements PreferenceService {
    @Autowired
    private CharacterPreferGenreMapper characterPreferGenreMapper;

    @Autowired
    private ChartMapper chartMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private ImageManagementMapper imageManagementMapper;

    @Autowired
    RedisService redisService;

    @Autowired
    private PreferArtistVideoPanelAssembly preferArtistVideoPanelAssembly;

    @Autowired
    private PreferenceMapper preferenceMapper;

    @Autowired
    private MetaClient metaClient;

    @Override
    public ChartResponse getPreferenceGenreList(Long characterNo) {
        List<CharacterPreferGenreDto> characterPreferGenreList = Collections.EMPTY_LIST;
        List<CharacterPreferDispDto> characterPreferDispList = Collections.EMPTY_LIST;
        List<ChartDto> chartDtoList;
        List<Chart> chartList;

        if (characterNo != null) {
            characterPreferGenreList = characterPreferGenreMapper.selectCharacterPreferGenreList(characterNo);
            characterPreferDispList = characterPreferGenreMapper.selectCharacterPreferDispList(characterNo);

			characterPreferDispList = characterPreferDispList.stream()
					.filter(x -> PreferPropsType.KIDS100.getCode().equals(x.getDispPropsType()))
					.collect(Collectors.toList());
        }

        if (characterNo == null
                || (CollectionUtils.isEmpty(characterPreferGenreList) && CollectionUtils.isEmpty(characterPreferDispList))) {
            chartDtoList = chartMapper.selectChartListByDefaultGenre();
        } else {
            chartDtoList = chartMapper.selectChartListByPreferGenre(characterNo);
        }

        if (CollectionUtils.isEmpty(chartDtoList) || chartDtoList.size() < 2) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        } else {
            chartList = new ArrayList<>(); // 최소 2개 이상 노출

            for (ChartDto chartDto : chartDtoList) {
                String shortcutType = chartDto.getChartType() == ChartType.NEW ? "RECENT" : "POPULARITY";
                List<ImageManagementDto> imgMangList =  imageManagementMapper.selectImageManagementList(HomeContentType.GENRE.getCode(), chartDto.getSvcContentId(), shortcutType);

                if (!CollectionUtils.isEmpty(imgMangList)) { // 쇼컷 이미지가 없는 경우 목록에서 제외
                    List<Chart.AlbumImg> albumImgList = new ArrayList<>();
                    albumImgList.addAll(imgMangList.stream()
                            .map(imgMang -> Chart.AlbumImg.builder()
                                    .size(imgMang.getImgSize())
                                    .url(imgMang.getImgUrl())
                                    .build())
                            .collect(Collectors.toList()));

                    Chart chart = Chart.builder()
                            .chartId(chartDto.getChartId())
                            .chartNm(chartDto.getChartNm())
                            .albumImgList(albumImgList)
                            .build();

                    chartList.add(chart);
                }
            }
        }


        return new ChartResponse<>(chartList, HomeContentType.CHART);
    }

    @Override
    public ChartResponse getPreferenceArtistList(Long characterNo) {
        String personalPreferenceArtistKey = String.format(PERSONAL_PREFERENCE_ARTIST_KEY, characterNo);
        List<ArtistDto> artistDtoList = redisService.getListWithPrefix(personalPreferenceArtistKey, ArtistDto.class);

        if (CollectionUtils.isEmpty(artistDtoList)) {
            artistDtoList = artistMapper.selectArtistListByPreferArtist(characterNo);

            if (CollectionUtils.isEmpty(artistDtoList) || artistDtoList.size() < 2) { // 최소 2개 이상 노출
                throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

            } else {
                if (!CollectionUtils.isEmpty(artistDtoList)) {
                    LocalTime nowTime = LocalTime.now();
                    LocalTime endTime = LocalTime.MAX;

                    long expireSeconds =  nowTime.until(endTime, ChronoUnit.SECONDS);

                    redisService.setWithPrefix(personalPreferenceArtistKey, artistDtoList, (int) expireSeconds);
                }
            }

        }

        return new ChartResponse<>(preferenceArtistListConvert(artistDtoList), HomeContentType.ARTIST);
    }

    // 선호 유사 아티스트
	// added by Bob 2018.11.30
	@Override
	public ChartResponse getPreferSimilarArtistList(Long characterNo, Integer sectionNumber) {

		List<Artist> similarArtistList = makeSimilarArtistList(characterNo, sectionNumber);

		if(CollectionUtils.isEmpty(similarArtistList)){
			return null;
		}

		return new ChartResponse<>(similarArtistList, HomeContentType.ARTIST );
	}

	@Override
	public String getPreferSimilarArtistName(Long characterNo, Integer sectionNumber) {

		List<Artist> similarArtistList = makeSimilarArtistList(characterNo, sectionNumber);

		if(CollectionUtils.isEmpty(similarArtistList)){
			return null;
		}

		try {
			return similarArtistList.get(0).getArtistNm();
		}catch (Exception e){
			return null;
		}
	}

	@Override
	public void deletePreferSimilarArtistName(Long characterNo) {
		redisService.delWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_HISTORY_KEY, characterNo));
		redisService.delWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, characterNo));
	}

	private List<Artist> makeSimilarArtistList(Long characterNo, Integer sectionNumber) {

		String personalSimilarArtistKey = String.format(PERSONAL_SIMILAR_ARTIST_KEY, characterNo);

		// redis 캐쉬 검색 (Wrapper class 호출)
		PreferenceSimilarArtistListRedisWrapper preferenceSimilarArtistListRedisWrapper = redisService.getWithPrefix(personalSimilarArtistKey, PreferenceSimilarArtistListRedisWrapper.class);

		List<ArtistDto> artistDtoList = null;

		// 캐쉬가 있을 경우 ( 빈 캐쉬도 있을 수 있음 )
		if(preferenceSimilarArtistListRedisWrapper != null && preferenceSimilarArtistListRedisWrapper.isCached()){
			if(preferenceSimilarArtistListRedisWrapper.getArtistDtoList() != null && preferenceSimilarArtistListRedisWrapper.getArtistDtoList().length > 1) {
				artistDtoList = preferenceSimilarArtistListRedisWrapper.getArtistDtoList()[sectionNumber - 1];
			}
		}else{
			//1. 빈 캐쉬를 저장 (중복 호출 방지)
			preferenceSimilarArtistListRedisWrapper = PreferenceSimilarArtistListRedisWrapper.builder()
					.artistDtoList(null)
					.cached(true)
					.build();

			setRedisWithWrapper(personalSimilarArtistKey, preferenceSimilarArtistListRedisWrapper);

			// 2. 3일간 노출 이력으로 yyymmd를 키로, 노출된 시드 아티스트 아이디 값으로 저장
			Map<String, List<Long>> historyMap = getSeedArtistIdHistoryMap(characterNo);

			List<Long> legacySeedIdList = new ArrayList<>();

			historyMap.values().forEach(
					x-> legacySeedIdList.addAll(x)
			);

			log.info("[유사아티스트] - [{}] 기존 노출 시드 아티스트 이력 : {}", characterNo, legacySeedIdList);

			// 3. 이력이 있을 경우, 3일간 노출된 시드 아티스트 정보를 노출 불가 시드 아티스트로 하여 시드 아티스트를 검색
			List<ArtistDto> totalSeedArtistList = artistMapper.selectSeedArtistList(characterNo, (legacySeedIdList.size() <= 0 ? null : legacySeedIdList));

			if(CollectionUtils.isEmpty(totalSeedArtistList)) {
				log.info("[유사아티스트] - [{}] 노출할 시드 아티스트 없음", characterNo);
				return null;
			}

			// 4. 랜덤으로 시드 아티스트를 추출
			Collections.shuffle(totalSeedArtistList);
			totalSeedArtistList = totalSeedArtistList.stream().limit(10).collect(Collectors.toList());

			log.info("[유사아티스트] - [{}] 타켓 시드 아티스트 추출 : {}", characterNo, totalSeedArtistList);

			// 5. 전체 시드 아티스트 기준으로 유사 아티스트 목록 추출
			List<PreferSimilarArtistDto> preferSimilarArtistDtoList =
					artistMapper.selectArtistListBySimilarArtist(
							characterNo,

							totalSeedArtistList.stream().map(x -> x.getArtistId()).collect(Collectors.toList())

							.stream().distinct().collect(
					Collectors.toList()));


			if (CollectionUtils.isEmpty(preferSimilarArtistDtoList)) {
				log.info("[유사아티스트] - [{}] 노출할 word2vec에 아티스트 없어 기존 테이블 조회", characterNo);
				preferSimilarArtistDtoList =
						artistMapper.selectArtistListBySimilarArtistOld(
								characterNo,

								totalSeedArtistList.stream().map(x -> x.getArtistId()).collect(Collectors.toList())

										.stream().distinct().collect(
										Collectors.toList()));
			}

			if (CollectionUtils.isEmpty(preferSimilarArtistDtoList)) {
				log.info("[유사아티스트] - [{}] 노출할 유사아티스트 없음", characterNo);
				return null;
			}

			// 6. 시드 아티스트 별 유사아티스트 맵 작성
			Map<Long, List<PreferSimilarArtistDto>> preferSimilarArtistDtoMap = preferSimilarArtistDtoList.stream()
					.sorted(Comparator.comparingInt(PreferSimilarArtistDto::getRank))
					.collect(Collectors.groupingBy(x -> x.getSeedArtistId()));

			// 7. 전체 선호 아티스트 들 중 시드 아티스트 아이디 2개를 추출
			List<Long> seedArtistIdList = preferSimilarArtistDtoList.stream().map(x -> x.getSeedArtistId()).distinct().limit(2).collect(
					Collectors.toList());

			log.info("[유사아티스트] - [{}] 오늘의 시드 아티스트 : {}", characterNo, seedArtistIdList);

			// 8. 시드 아티스트 이력 갱신
			refreshSeedArtistIdHistoryMap(characterNo, historyMap, seedArtistIdList);

			// 9. 결과 아티스트 목록 생성
			List<ArtistDto>[] resultArtistDtoList = makeResultArtistDtoList(characterNo, totalSeedArtistList,
					preferSimilarArtistDtoMap, seedArtistIdList);

			preferenceSimilarArtistListRedisWrapper.setArtistDtoList(resultArtistDtoList);

			// 10. 결과를 레디스에 저장
			setRedisWithWrapper(personalSimilarArtistKey, preferenceSimilarArtistListRedisWrapper);
			artistDtoList = resultArtistDtoList[sectionNumber -1];

		}

		// 캐쉬된 혹은 새로 만들어진 결과가 빈 리스트인 경우에 null 처리
		if (isEmptyArtistDtoList(artistDtoList)) {
			log.info("[유사아티스트] - [{}] 노출할 유사아티스트 없음", characterNo);
			return null;
		}

		return preferenceArtistListConvert(artistDtoList);
	}
	private List<ArtistDto>[] makeResultArtistDtoList(
			Long characterNo,
			List<ArtistDto> totalSeedArtistList,
			Map<Long, List<PreferSimilarArtistDto>> preferSimilarArtistDtoMap,
			List<Long> seedArtistIdList) {

		// 결과 만들기
		List<ArtistDto>[] resultArtistDtoList = new ArrayList[2];

		Iterator iter = seedArtistIdList.listIterator();

		int seedIndex = 0;

		while(iter.hasNext()) {

			Long seedArtistId = (Long) iter.next();

			// ArtistDto로 축소, 시드 아티스트가 들어있는 경우 제거, 중복 제거, 20명으로 자르기
			List<ArtistDto> currentPreferArtistDtoList = preferSimilarArtistDtoMap.get(seedArtistId).stream()
					.map(
							x -> ArtistDto.builder()
									.artistId(x.getArtistId())
									.artistName(x.getArtistName())
									.imgList(x.getImgList())
									.build()

					)
					.filter(x-> !seedArtistIdList.contains(x.getArtistId()))
					.distinct()
					.limit(20)
					.collect(Collectors.toList())
					;


			// 유사아티스트가 2명 이하면 생성 X
			if(currentPreferArtistDtoList.size() <= 2){
				log.info("[유사아티스트] - [{}] 선호 아티스트 섹션{} 결과 없음", characterNo, seedIndex + 1);
				continue;
			}

			// 랜덤으로 다섯명 추출
			Collections.shuffle(currentPreferArtistDtoList);
			currentPreferArtistDtoList = currentPreferArtistDtoList.stream().distinct().limit(5).collect(
					Collectors.toList());

			// seed 1, 결과 5 으로 결과 생성
			resultArtistDtoList[seedIndex] = new ArrayList<>();
			resultArtistDtoList[seedIndex].add(0, totalSeedArtistList.stream().filter(x-> x.getArtistId().equals(seedArtistId)).findFirst().get());
			resultArtistDtoList[seedIndex].addAll(currentPreferArtistDtoList);

			log.info("[유사아티스트] - [{}] 선호 아티스트 섹션{} 생성 완료", characterNo, seedIndex + 1);

			seedIndex++;

		}
		return resultArtistDtoList;
	}

	private void refreshSeedArtistIdHistoryMap(Long characterNo, Map<String, List<Long>> historyMap,
			List<Long> seedArtistIdList) {

		log.info("[유사아티스트] - [{}] 노출 시드 이력 저장", characterNo);

		String currentDate = DateUtil.toString(new Date(),"yyyyMMdd");
		// 시드 아이디 노출 이력 기록
		historyMap.put(currentDate, seedArtistIdList);
		// 3알전 기록 삭제
		historyMap.remove(DateUtil.getMinusDate(currentDate, -2));
		// 레디스에 히스토리 기록 (만기일 3일 추가)
		LocalDateTime expireDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(3L);
		long expireSeconds = LocalDateTime.now().until(expireDateTime, ChronoUnit.SECONDS);
		redisService.setWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_HISTORY_KEY, characterNo), historyMap, (int) expireSeconds);

	}

	private Map<String, List<Long>> getSeedArtistIdHistoryMap(Long characterNo) {

		Map<String, List<Long>> historyMap = redisService.getWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_HISTORY_KEY, characterNo), Map.class);

		if(historyMap == null) {
			historyMap = new HashMap<>();
		}

		return historyMap;
	}

	private boolean isEmptyArtistDtoList(List<ArtistDto> artistDtoList) {

    	try{
			if (CollectionUtils.isEmpty(artistDtoList) ||
					ObjectUtils.isEmpty(artistDtoList.get(0).getArtistId())
			) {
				return true;
			}

	    }catch(Exception e){
    	    log.error(e.getMessage());
	    }

		return false;
	}

	private void setRedisWithWrapper(String key, PreferenceSimilarArtistListRedisWrapper preferenceSimilarArtistListRedisWrapper) {
		// 캐쉬 만기는 당일 자정
		redisService.setWithPrefix(key, preferenceSimilarArtistListRedisWrapper, (int) LocalTime.now().until(LocalTime.MAX, ChronoUnit.SECONDS));
	}

    private List<Artist> preferenceArtistListConvert(List<ArtistDto> artistDtoList) {
        List<Artist> artistList = new ArrayList<>();

        for (ArtistDto artistDto : artistDtoList) {
            List<Artist.ArtistImg> artistImgList = new ArrayList<>();

            Optional.ofNullable(artistDto.getImgList())
                    .ifPresent(imageInfos -> {
                        artistImgList.addAll(
                                imageInfos.stream()
                                        .map(imageInfo -> Artist.ArtistImg.builder()
                                                .size(imageInfo.getSize())
                                                .url(imageInfo.getUrl())
                                                .build())
                                        .collect(Collectors.toList()));
        });

            Artist artist = Artist.builder()
                    .artistId(artistDto.getArtistId())
                    .artistNm(artistDto.getArtistName())
                    .albumImgList(artistImgList)
                    .build();

            artistList.add(artist);
        }

        return artistList;
    }

	/**
	 * 선호 아티스트 최신 영상
	 * @param characterNo
	 * @return
	 */
	@Override
	public List<VideoVo> getPreferenceVideoArtistNewList(Long characterNo){

		if(ObjectUtils.isEmpty(characterNo)){
			return null;
		}

		String redisKey = String.format(RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_ARTIST_NEW_LIST, characterNo);

		if(redisService.exists(redisKey)){
			return redisService.getListWithPrefix(redisKey, VideoVo.class);
		}

		// 3일 전 부터 현 시각 사이의 전시시작일 분리
		Date to = new Date();
		Date from = DateUtil.getDate(to, -259200);

		List<VideoVo> videoVoList 	=
				Optional.ofNullable(
						metaClient.getVideos(
								MetaVideoRequestVo.builder()
										.videoIds(preferenceMapper.selectPreferArtistVideoIdListByCharacterNo(characterNo))
										.build()
						).getData().getList()

				).orElseGet(Collections::emptyList)
						.stream()
						.filter(Objects::nonNull)
						.filter(videoVo -> videoVo.getDispStartDtime().after(from) && videoVo.getDispStartDtime().before(to))
						.collect(Collectors.toList());


		if(CollectionUtils.isEmpty(videoVoList)){
			return null;
		}

		redisService.setWithPrefix(redisKey, videoVoList, 3600);

		return videoVoList;
	}

	/**
	 * 선호 장르 최신 영상
	 * @param characterNo
	 * @return
	 */
	@Override
    public List<VideoVo> getPreferenceVideoGenreNewList(Long characterNo){

		List<Long> videoIdList;
		List<VideoVo> videoVoList;

		// 캐릭터 없으면 최신 디폴트 비디오
		if(ObjectUtils.isEmpty(characterNo)){
			return getDefaultSvcGenreVideoVoList();
		}

		String redisKey = String.format(RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_GENRE_NEW_LIST, characterNo);

		if (redisService.exists(redisKey)) {
			return redisService.getListWithPrefix(redisKey, VideoVo.class);
		}

		videoIdList = preferenceMapper.selectPreferGenreVideoIdListByCharacterNo(characterNo);

		// 선호 장르 없는 경우, 기본 장르 선택
		if(CollectionUtils.isEmpty(videoIdList)) {
			return getDefaultSvcGenreVideoVoList();
		}

		// 선호 장르 있는 경우
		Date to = new Date();
		Date from = DateUtil.getDate(to, -604800);

		videoVoList = Optional.ofNullable(
				metaClient.getVideos(
						MetaVideoRequestVo.builder().videoIds(videoIdList).build()
				)
						.getData().getList()
		)
				.orElseGet(Collections::emptyList)
				.stream()
				.filter(Objects::nonNull)
				.filter(videoVo -> videoVo.getDispStartDtime().after(from) && videoVo.getDispStartDtime().before(to))
				.collect(Collectors.toList());

		List<VideoVo> preferArtistVideoVoList = getPreferenceVideoArtistNewList(characterNo);

		if(!CollectionUtils.isEmpty(preferArtistVideoVoList)) {
			videoVoList.removeAll(preferArtistVideoVoList);
		}

		if(CollectionUtils.isEmpty(videoVoList)){
			return null;
		}

		// 최신 비디오는 한시간 마다 갱신되므로 캐쉬도 한시간 정책..
		redisService.setWithPrefix(redisKey, videoVoList, 3600);

		return videoVoList;
    }

	/**
	 * 기본 장르의 최신 비디오 목록 조회
	 * @return
	 */

	@RedisCacheable(key = RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_GENRE_NEW_DEFAULT_LIST, expireSeconds = 3600)
	private List<VideoVo> getDefaultSvcGenreVideoVoList(){

	    return Optional.ofNullable(
			    metaClient.getVideos(
					    MetaVideoRequestVo.builder().videoIds(preferenceMapper.selectDefaultSvcGenreVideoIdList()).build()
			    )
					    .getData().getList()
	    )
			    .orElseGet(Collections::emptyList)
			    .stream()
			    .filter(Objects::nonNull)
			    .collect(Collectors.toList());
    }


	/**
	 * 주어진 영상 리스트를 주어진 크기로 자르고 shuffle
	 * @param videoVoList
	 * @param limitSize
	 * @return
	 */
	@Override
	public List<VideoVo> getLimitedShuffledVideoList(List<VideoVo> videoVoList, Integer limitSize){

		if(CollectionUtils.isEmpty(videoVoList)){
			return null;
		}

		if(videoVoList.size() >= 5) {
			Collections.shuffle(videoVoList);
		}

		if(videoVoList.size() > limitSize){
			videoVoList = videoVoList.stream().limit(limitSize).collect(Collectors.toList());
		}

		return videoVoList;
	}

	@Override
	public void clearCachePreferenceVideoArtistNewList(Long characterNo) {
		redisService.delWithPrefix(String.format(RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_ARTIST_NEW_LIST, characterNo));
	}

	@Override
	public void clearCachePreferenceVideoGenreNewList(Long characterNo) {
		redisService.delWithPrefix(String.format(RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_GENRE_NEW_LIST, characterNo));
	}

}
