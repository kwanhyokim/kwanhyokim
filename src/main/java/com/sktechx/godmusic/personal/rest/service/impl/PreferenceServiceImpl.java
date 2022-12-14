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
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.preference.PreferSimilarArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Artist;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import com.sktechx.godmusic.personal.rest.model.vo.preference.ChartResponse;
import com.sktechx.godmusic.personal.rest.model.vo.preference.PreferenceSimilarArtistListRedisWrapper;
import com.sktechx.godmusic.personal.rest.model.vo.video.VideoVo;
import com.sktechx.godmusic.personal.rest.repository.*;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import com.sktechx.godmusic.personal.rest.service.image.ImageReadService;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.*;

/**
 * ?????? :
 *
 * @author ?????????/SKTECHX (younghyun.ahn@sk.com)
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
    private ImageReadService imageReadService;

    @Autowired
    RedisService redisService;

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
            chartList = new ArrayList<>(); // ?????? 2??? ?????? ??????

            for (ChartDto chartDto : chartDtoList) {
                String shortcutType = chartDto.getChartType() == ChartType.NEW ? "RECENT" : "POPULARITY";
                List<ImageManagementDto> imgMangList =  imageReadService.getImageManagementList(
                		HomeContentType.GENRE.getCode(), chartDto.getSvcContentId(), shortcutType
                );

                if (!CollectionUtils.isEmpty(imgMangList)) { // ?????? ???????????? ?????? ?????? ???????????? ??????
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

            if (CollectionUtils.isEmpty(artistDtoList) || artistDtoList.size() < 2) { // ?????? 2??? ?????? ??????
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

    // ?????? ?????? ????????????
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

		// redis ?????? ?????? (Wrapper class ??????)
		PreferenceSimilarArtistListRedisWrapper preferenceSimilarArtistListRedisWrapper = redisService.getWithPrefix(personalSimilarArtistKey, PreferenceSimilarArtistListRedisWrapper.class);

		List<ArtistDto> artistDtoList = null;

		// ????????? ?????? ?????? ( ??? ????????? ?????? ??? ?????? )
		if(preferenceSimilarArtistListRedisWrapper != null && preferenceSimilarArtistListRedisWrapper.isCached()){
			if(preferenceSimilarArtistListRedisWrapper.getArtistDtoList() != null && preferenceSimilarArtistListRedisWrapper.getArtistDtoList().length > 1) {
				artistDtoList = preferenceSimilarArtistListRedisWrapper.getArtistDtoList()[sectionNumber - 1];
			}
		}else{
			//1. ??? ????????? ?????? (?????? ?????? ??????)
			preferenceSimilarArtistListRedisWrapper = PreferenceSimilarArtistListRedisWrapper.builder()
					.artistDtoList(null)
					.cached(true)
					.build();

			setRedisWithWrapper(personalSimilarArtistKey, preferenceSimilarArtistListRedisWrapper);

			// 2. 3?????? ?????? ???????????? yyymmd??? ??????, ????????? ?????? ???????????? ????????? ????????? ??????
			Map<String, List<Long>> historyMap = getSeedArtistIdHistoryMap(characterNo);

			List<Long> legacySeedIdList = new ArrayList<>();

			historyMap.values().forEach(
					x-> legacySeedIdList.addAll(x)
			);

			log.info("[??????????????????] - [{}] ?????? ?????? ?????? ???????????? ?????? : {}", characterNo, legacySeedIdList);

			// 3. ????????? ?????? ??????, 3?????? ????????? ?????? ???????????? ????????? ?????? ?????? ?????? ??????????????? ?????? ?????? ??????????????? ??????
			List<ArtistDto> totalSeedArtistList = artistMapper.selectSeedArtistList(characterNo, (legacySeedIdList.size() <= 0 ? null : legacySeedIdList));

			if(CollectionUtils.isEmpty(totalSeedArtistList)) {
				log.info("[??????????????????] - [{}] ????????? ?????? ???????????? ??????", characterNo);
				return null;
			}

			// 4. ???????????? ?????? ??????????????? ??????
			Collections.shuffle(totalSeedArtistList);
			totalSeedArtistList = totalSeedArtistList.stream().limit(10).collect(Collectors.toList());

			log.info("[??????????????????] - [{}] ?????? ?????? ???????????? ?????? : {}", characterNo, totalSeedArtistList);

			// 5. ?????? ?????? ???????????? ???????????? ?????? ???????????? ?????? ??????
			List<PreferSimilarArtistDto> preferSimilarArtistDtoList =
					artistMapper.selectArtistListBySimilarArtist(
							characterNo,

							totalSeedArtistList.stream().map(x -> x.getArtistId()).collect(Collectors.toList())

							.stream().distinct().collect(
					Collectors.toList()));


			if (CollectionUtils.isEmpty(preferSimilarArtistDtoList)) {
				log.info("[??????????????????] - [{}] ????????? word2vec??? ???????????? ?????? ?????? ????????? ??????", characterNo);
				preferSimilarArtistDtoList =
						artistMapper.selectArtistListBySimilarArtistOld(
								characterNo,

								totalSeedArtistList.stream().map(x -> x.getArtistId()).collect(Collectors.toList())

										.stream().distinct().collect(
										Collectors.toList()));
			}

			if (CollectionUtils.isEmpty(preferSimilarArtistDtoList)) {
				log.info("[??????????????????] - [{}] ????????? ?????????????????? ??????", characterNo);
				return null;
			}

			// 6. ?????? ???????????? ??? ?????????????????? ??? ??????
			Map<Long, List<PreferSimilarArtistDto>> preferSimilarArtistDtoMap = preferSimilarArtistDtoList.stream()
					.sorted(Comparator.comparingInt(PreferSimilarArtistDto::getRank))
					.collect(Collectors.groupingBy(x -> x.getSeedArtistId()));

			// 7. ?????? ?????? ???????????? ??? ??? ?????? ???????????? ????????? 2?????? ??????
			List<Long> seedArtistIdList = preferSimilarArtistDtoList.stream().map(x -> x.getSeedArtistId()).distinct().limit(2).collect(
					Collectors.toList());

			log.info("[??????????????????] - [{}] ????????? ?????? ???????????? : {}", characterNo, seedArtistIdList);

			// 8. ?????? ???????????? ?????? ??????
			refreshSeedArtistIdHistoryMap(characterNo, historyMap, seedArtistIdList);

			// 9. ?????? ???????????? ?????? ??????
			List<ArtistDto>[] resultArtistDtoList = makeResultArtistDtoList(characterNo, totalSeedArtistList,
					preferSimilarArtistDtoMap, seedArtistIdList);

			preferenceSimilarArtistListRedisWrapper.setArtistDtoList(resultArtistDtoList);

			// 10. ????????? ???????????? ??????
			setRedisWithWrapper(personalSimilarArtistKey, preferenceSimilarArtistListRedisWrapper);
			artistDtoList = resultArtistDtoList[sectionNumber -1];

		}

		// ????????? ?????? ?????? ???????????? ????????? ??? ???????????? ????????? null ??????
		if (isEmptyArtistDtoList(artistDtoList)) {
			log.info("[??????????????????] - [{}] ????????? ?????????????????? ??????", characterNo);
			return null;
		}

		return preferenceArtistListConvert(artistDtoList);
	}
	private List<ArtistDto>[] makeResultArtistDtoList(
			Long characterNo,
			List<ArtistDto> totalSeedArtistList,
			Map<Long, List<PreferSimilarArtistDto>> preferSimilarArtistDtoMap,
			List<Long> seedArtistIdList) {

		// ?????? ?????????
		List<ArtistDto>[] resultArtistDtoList = new ArrayList[2];

		Iterator iter = seedArtistIdList.listIterator();

		int seedIndex = 0;

		while(iter.hasNext()) {

			Long seedArtistId = (Long) iter.next();

			// ArtistDto??? ??????, ?????? ??????????????? ???????????? ?????? ??????, ?????? ??????, 20????????? ?????????
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


			// ????????????????????? 2??? ????????? ?????? X
			if(currentPreferArtistDtoList.size() <= 2){
				log.info("[??????????????????] - [{}] ?????? ???????????? ??????{} ?????? ??????", characterNo, seedIndex + 1);
				continue;
			}

			// ???????????? ????????? ??????
			Collections.shuffle(currentPreferArtistDtoList);
			currentPreferArtistDtoList = currentPreferArtistDtoList.stream().distinct().limit(5).collect(
					Collectors.toList());

			// seed 1, ?????? 5 ?????? ?????? ??????
			resultArtistDtoList[seedIndex] = new ArrayList<>();
			resultArtistDtoList[seedIndex].add(0, totalSeedArtistList.stream().filter(x-> x.getArtistId().equals(seedArtistId)).findFirst().get());
			resultArtistDtoList[seedIndex].addAll(currentPreferArtistDtoList);

			log.info("[??????????????????] - [{}] ?????? ???????????? ??????{} ?????? ??????", characterNo, seedIndex + 1);

			seedIndex++;

		}
		return resultArtistDtoList;
	}

	private void refreshSeedArtistIdHistoryMap(Long characterNo, Map<String, List<Long>> historyMap,
			List<Long> seedArtistIdList) {

		log.info("[??????????????????] - [{}] ?????? ?????? ?????? ??????", characterNo);

		String currentDate = DateUtil.toString(new Date(),"yyyyMMdd");
		// ?????? ????????? ?????? ?????? ??????
		historyMap.put(currentDate, seedArtistIdList);
		// 3?????? ?????? ??????
		historyMap.remove(DateUtil.getMinusDate(currentDate, -2));
		// ???????????? ???????????? ?????? (????????? 3??? ??????)
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
		// ?????? ????????? ?????? ??????
		long expiredSec = LocalTime.now().until(LocalTime.MAX, ChronoUnit.SECONDS);
		if ( expiredSec > 0) {
			redisService.setWithPrefix(key, preferenceSimilarArtistListRedisWrapper, Long.valueOf(expiredSec).intValue());
		}
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
	 * ?????? ???????????? ?????? ??????
	 * @param characterNo
	 * @return
	 */
	@Override
	public List<VideoVo> getPreferenceVideoArtistNewList(Long characterNo){

		if(ObjectUtils.isEmpty(characterNo)){
			return null;
		}

		String redisKey = String.format(RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_ARTIST_NEW_LIST, characterNo);

		if(redisService.existsWithPrefix(redisKey)){
			return redisService.getListWithPrefix(redisKey, VideoVo.class);
		}

		List<Long> videoIdList = preferenceMapper.selectPreferArtistVideoIdListByCharacterNo(characterNo);

		if(CollectionUtils.isEmpty(videoIdList)) {
			return null;
		}

        // 3??? ??? ?????? ??? ?????? ????????? ??????????????? ??????
        Date to = new Date();
        Date from = DateUtil.getDate(to, -259200);

        List<VideoVo> videoVoList 	=
				Optional.ofNullable(
						metaClient.getVideos(
								MetaVideoRequestVo.builder()
										.videoIds(videoIdList)
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
	 * ?????? ?????? ?????? ??????
	 * @param characterNo
	 * @return
	 */
	@Override
    public List<VideoVo> getPreferenceVideoGenreNewList(Long characterNo){

		List<Long> videoIdList;
		List<VideoVo> videoVoList;

		// ????????? ????????? ?????? ????????? ?????????
		if(ObjectUtils.isEmpty(characterNo)){
			return getDefaultSvcGenreVideoVoList();
		}

		String redisKey = String.format(RedisKeyConstant.PERSONAL_PREFERENCE_VIDEO_GENRE_NEW_LIST, characterNo);

		if (redisService.existsWithPrefix(redisKey)) {
			return redisService.getListWithPrefix(redisKey, VideoVo.class);
		}

		videoIdList = preferenceMapper.selectPreferGenreVideoIdListByCharacterNo(characterNo);

		// ?????? ?????? ?????? ??????, ?????? ?????? ??????
		if(CollectionUtils.isEmpty(videoIdList)) {
			return getDefaultSvcGenreVideoVoList();
		}

		// ?????? ?????? ?????? ??????
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

		// ?????? ???????????? ????????? ?????? ??????????????? ????????? ????????? ??????..
		redisService.setWithPrefix(redisKey, videoVoList, 3600);

		return videoVoList;
    }

	/**
	 * ?????? ????????? ?????? ????????? ?????? ??????
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
	 * ????????? ?????? ???????????? ????????? ????????? ????????? shuffle
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
