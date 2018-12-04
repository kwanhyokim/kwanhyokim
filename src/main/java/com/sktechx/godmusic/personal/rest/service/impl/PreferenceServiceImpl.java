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

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.domain.HomeContentType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.model.dto.preference.PreferSimilarArtistDto;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Artist;
import com.sktechx.godmusic.personal.rest.model.vo.preference.Chart;
import com.sktechx.godmusic.personal.rest.model.vo.preference.ChartResponse;
import com.sktechx.godmusic.personal.rest.repository.ArtistMapper;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.ChartMapper;
import com.sktechx.godmusic.personal.rest.repository.ImageManagementMapper;
import com.sktechx.godmusic.personal.rest.service.PreferenceService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.PERSONAL_RREFERENCE_ARTIST_KEY;
import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.PERSONAL_SIMILAR_ARTIST_HISTORY_KEY;
import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.PERSONAL_SIMILAR_ARTIST_KEY;

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

    @Override
    public ChartResponse getPreferenceGenreList(Long characterNo) {
        List<CharacterPreferGenreDto> characterPreferGenreList = Collections.EMPTY_LIST;
        List<CharacterPreferDispDto> characterPreferDispList = Collections.EMPTY_LIST;
        List<ChartDto> chartDtoList;
        List<Chart> chartList;

        if (characterNo != null) {
            characterPreferGenreList = characterPreferGenreMapper.selectCharacterPreferGenreList(characterNo);
            characterPreferDispList = characterPreferGenreMapper.selectCharacterPreferDispList(characterNo);
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
        String personalPreferenceArtistKey = String.format(PERSONAL_RREFERENCE_ARTIST_KEY, characterNo);
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

	private List<Artist> makeSimilarArtistList(Long characterNo, Integer sectionNumber) {

		String personalSimilarArtistKey = String.format(PERSONAL_SIMILAR_ARTIST_KEY, sectionNumber, characterNo);

		// redis 캐쉬 검색

		List<ArtistDto> artistDtoList = redisService.getListWithPrefix(personalSimilarArtistKey, ArtistDto.class);

		// 캐쉬된 내용이 없을 경우
		if (CollectionUtils.isEmpty(artistDtoList)) {

			// 우선 빈 캐쉬를 저장 (중복 호출 방지)
			setRedisWithArtistDtoList(characterNo, null);

			// 3일간 노출 이력
			// yyymmdd, 노출된 시드 아티스트 아이디 값으로 저장
			Map<String, List<Long>> historyMap = getSeedArtistIdHistoryMap(characterNo);

			List<Long> legacySeedIdList = new ArrayList<>();

			historyMap.values().forEach(
					x-> legacySeedIdList.addAll(x)
			);

			// 이력이 있을 경우, 3일간 노출된 시드 아티스트 정보를 노출 불가 시드 아티스트로 하여 시드 아티스트를 검색
			List<ArtistDto> totalSeedArtistList = artistMapper.selectSeedArtistList(characterNo, (legacySeedIdList.size() <= 0 ? null : legacySeedIdList));

			if(CollectionUtils.isEmpty(totalSeedArtistList)) {
				return null;
			}

			// 랜덤으로 시드 아티스트를 추출
			Collections.shuffle(totalSeedArtistList);
			totalSeedArtistList = totalSeedArtistList.stream().limit(20).collect(Collectors.toList());

			// 전체 시드 아티스트 기준으로 유사 아티스트 목록 추출
			List<PreferSimilarArtistDto> preferSimilarArtistDtoList =
					artistMapper.selectArtistListBySimilarArtist(
							characterNo,

							totalSeedArtistList.stream().map(x -> x.getArtistId()).collect(Collectors.toList())

							.stream().distinct().collect(
					Collectors.toList()));

			if (CollectionUtils.isEmpty(preferSimilarArtistDtoList)) {
				return null;
			}

			// 시드 아티스트 별 유사아티스트 맵 작성
			Map<Long, List<PreferSimilarArtistDto>> preferSimilarArtistDtoMap = preferSimilarArtistDtoList.stream()
					.sorted(Comparator.comparingInt(PreferSimilarArtistDto::getRank))
					.collect(Collectors.groupingBy(x -> x.getSeedArtistId()));

			// 전체 선호 아티스트 들 중 시드 아티스트 아이디 2개를 추출
			List<Long> seedArtistIdList = preferSimilarArtistDtoList.stream().map(x -> x.getSeedArtistId()).distinct().limit(2).collect(
					Collectors.toList());

			// 시드 아티스트 이력 갱신
			refreshSeedArtistIdHistoryMap(characterNo, historyMap, seedArtistIdList);

			// 결과 아티스트 목록 생성
			List<ArtistDto>[] resultArtistDtoList = makeResultArtistDtoList(totalSeedArtistList,
					preferSimilarArtistDtoMap, seedArtistIdList);

			// 결과를 레디스에 저장
			setRedisWithArtistDtoList(characterNo, resultArtistDtoList);

			artistDtoList = resultArtistDtoList[sectionNumber -1];

		}

		if (isEmptyArtistDtoList(artistDtoList)) {
			return null;
		}

		return preferenceArtistListConvert(artistDtoList);
	}
	private List<ArtistDto>[] makeResultArtistDtoList(List<ArtistDto> totalSeedArtistList,
			Map<Long, List<PreferSimilarArtistDto>> preferSimilarArtistDtoMap,
			List<Long> seedArtistIdList) {

		// 결과 만들기
		List<ArtistDto>[] resultArtistDtoList = fillArtistDtoList(null);

		Iterator iter = seedArtistIdList.listIterator();


		int seedIndex = 0;

		while(iter.hasNext()) {

			Long seedArtistId = (Long) iter.next();

			// 중복 제거, 20명으로 자르기
			List<ArtistDto> currentPreferArtistDtoList = preferSimilarArtistDtoMap.get(seedArtistId).stream()
					.map(
							x -> ArtistDto.builder()
									.artistId(x.getArtistId())
									.artistName(x.getArtistName())
									.imgList(x.getImgList())
									.build()

					)
					.distinct()
					.limit(20)
					.collect(Collectors.toList())
					;


			// 유사아티스트가 2명 이하면 생성 X
			if(currentPreferArtistDtoList.size() <= 2){
				continue;
			}

			// 랜덤으로 다섯명 추출
			Collections.shuffle(currentPreferArtistDtoList);
			currentPreferArtistDtoList = currentPreferArtistDtoList.stream().distinct().limit(5).collect(
					Collectors.toList());

			// seed 1, 결과 5 으로 결과 생성
			resultArtistDtoList[seedIndex].clear();
			resultArtistDtoList[seedIndex].add(0, totalSeedArtistList.stream().filter(x-> x.getArtistId().equals(seedArtistId)).findFirst().get());
			resultArtistDtoList[seedIndex].addAll(currentPreferArtistDtoList);

			seedIndex++;

		}
		return resultArtistDtoList;
	}

	private void refreshSeedArtistIdHistoryMap(Long characterNo, Map<String, List<Long>> historyMap,
			List<Long> seedArtistIdList) {
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

	private void setRedisWithArtistDtoList(Long characterNo, List<ArtistDto>[] resultArtistDtoList) {

    	resultArtistDtoList = fillArtistDtoList(resultArtistDtoList);

		// 캐쉬 만기는 당일 자정
		long expireSeconds = LocalTime.now().until(LocalTime.MAX, ChronoUnit.SECONDS);

		redisService.setWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, 1, characterNo), resultArtistDtoList[0], (int) expireSeconds);
		redisService.setWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, 2, characterNo), resultArtistDtoList[1], (int) expireSeconds);
	}

	private List<ArtistDto>[] fillArtistDtoList(List<ArtistDto>[] resultArtistDtoList) {

    	if(resultArtistDtoList == null) {
		    resultArtistDtoList = new ArrayList[2];
		    resultArtistDtoList[0] = new ArrayList<>();
		    resultArtistDtoList[1] = new ArrayList<>();
		    resultArtistDtoList[0].add(ArtistDto.builder().build());
		    resultArtistDtoList[1].add(ArtistDto.builder().build());
	    }

		if(resultArtistDtoList[1] == null){
			resultArtistDtoList[1] = new ArrayList<>();
			resultArtistDtoList[1].add(ArtistDto.builder().build());
		}


		return resultArtistDtoList;
	}

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
	public ChartResponse deletePreferSimilarArtistName(Long characterNo) {

		redisService
				.delWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_HISTORY_KEY, characterNo));
		redisService.delWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, 1, characterNo));
		redisService.delWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, 2, characterNo));

		return null;
	}
	//    private List<Chart> preferenceGenreListConvert(List<ChartDto> chartDtoList) {
//        List<Chart> chartList = new ArrayList<>();
//
//        for (ChartDto chartDto : chartDtoList) {
//            List<Chart.AlbumImg> albumImgList = new ArrayList<>();
//
//            Optional.ofNullable(chartDto.getTrackList())
//                    .flatMap(trackList -> Optional.ofNullable(trackList.get(0)))
//                    .flatMap(track -> Optional.ofNullable(track.getAlbum()))
//                    .flatMap(album -> Optional.ofNullable(album.getImgList()))
//                    .ifPresent(imageInfos -> {
//                        albumImgList.addAll(imageInfos.stream()
//                                .map(imageInfo -> Chart.AlbumImg.builder()
//                                        .size(imageInfo.getSize())
//                                        .url(imageInfo.getUrl())
//                                        .build())
//                                .collect(Collectors.toList()));
//                    });
//
//            Chart chart = Chart.builder()
//                    .chartId(chartDto.getChartId())
//                    .chartNm(chartDto.getChartNm())
//                    .albumImgList(albumImgList)
//                    .build();
//
//            chartList.add(chart);
//        }
//
//        return chartList;
//    }

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

}
