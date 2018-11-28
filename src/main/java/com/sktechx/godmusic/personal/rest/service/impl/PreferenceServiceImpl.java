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

			// 우선 빈 캐쉬를 저장
			setRedisWithArtistDtoList(characterNo, null);

			Date now = new Date();
			String currentDate = DateUtil.toString(now,"yyyyMMdd");

			// 3일간 노출 이력
			// yyymmdd, 노출된 시드 아티스트 아이디 값으로 저장
			Map<String, List<Long>> historyMap = redisService.getWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_HISTORY_KEY, characterNo), Map.class);
			List<Long> legacySeedArtistIdList = null;

			// 이력이 있을 경우, 3일간 노출된 시드 아티스트 정보를 수집
			if(historyMap != null){
				legacySeedArtistIdList = new ArrayList<>();

				Iterator mapIterator = historyMap.values().iterator();

				while(mapIterator.hasNext()){

					legacySeedArtistIdList.addAll((List)mapIterator.next());
				}
			}else{
				historyMap = new HashMap<>();
			}

			// not in 으로 노출 불가 시드 아티스트 추가하여 시드 아티스트를 검색
			List<ArtistDto> totalSeedArtistList = artistMapper.selectSeedArtistList(characterNo, legacySeedArtistIdList);

			if(CollectionUtils.isEmpty(totalSeedArtistList)) {
				setRedisWithArtistDtoList(characterNo, makeNoDataArtistDtoList());
				return null;
			}

			// 랜덤으로 시드 아티스트를 추출
			Random rand = new Random();

			totalSeedArtistList = rand.ints(20,0,totalSeedArtistList.size()).mapToObj(
					totalSeedArtistList::get).limit(20).collect(
					Collectors.toList());

			List<Long> totalPreferSimilarSeedArtistIdList = totalSeedArtistList.stream().map(x -> x.getArtistId()).collect(
					Collectors.toList());

			// 전체 시드 아티스트 기준으로 유사 아티스트 목록 추출
			List<PreferSimilarArtistDto> preferSimilarArtistDtoList = artistMapper.selectArtistListBySimilarArtist(characterNo, totalPreferSimilarSeedArtistIdList);

			if (CollectionUtils.isEmpty(preferSimilarArtistDtoList)) {
				// 데이터가 없는 경우, No Data Artist를 저장
				setRedisWithArtistDtoList(characterNo, makeNoDataArtistDtoList());
				return null;
			}

			// rank 순으로 정렬
			preferSimilarArtistDtoList = preferSimilarArtistDtoList.stream().sorted(
					Comparator.comparingInt(PreferSimilarArtistDto::getRank)).collect(Collectors.toList());

			// 시드 아티스트 아이디 2개를 랜덤으로 추출
			Iterator preferSimilarArtistDtoListIterator = preferSimilarArtistDtoList.iterator();

			List<Long> seedArtistIdList = new ArrayList<>();

			while(preferSimilarArtistDtoListIterator.hasNext()) {

				PreferSimilarArtistDto preferSimilarArtistDto = (PreferSimilarArtistDto) preferSimilarArtistDtoListIterator.next();
				if(seedArtistIdList.contains(preferSimilarArtistDto.getSeedArtistId())){
					continue;
				}

				seedArtistIdList.add(preferSimilarArtistDto.getSeedArtistId());

				if(seedArtistIdList.size() == 2){
					break;
				}
			}
			// 2. 결과 처리를 위한 시드 아티스트 DTO 추출
			List<ArtistDto> seedArtistList = totalSeedArtistList.stream().filter(x -> seedArtistIdList.contains(x.getArtistId())).collect(
					Collectors.toList());

			seedArtistList = seedArtistList.stream().distinct().collect(Collectors.toList());

			seedArtistList.forEach(x -> log.debug("seed artist name" + x.getArtistName()));

			// 3. 유사 아티스트 목록 추출
			artistDtoList = preferSimilarArtistDtoList.stream().filter(x-> seedArtistIdList.contains(x.getSeedArtistId())).collect(
					Collectors.toList());

			log.debug("total similar artist count : " + artistDtoList.size());

			// 시드 아이디 노출 이력 기록
			historyMap.put(currentDate, seedArtistIdList);

			// 3알전 기록 삭제
			historyMap.remove(DateUtil.getMinusDate(currentDate, -2));

			// 레디스에 히스토리 기록 (만기일 3일 추가)
			LocalDateTime expireDateTime = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0).plusDays(3L);
			long expireSeconds = LocalDateTime.now().until(expireDateTime, ChronoUnit.SECONDS);
			redisService.setWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_HISTORY_KEY, characterNo), historyMap, (int) expireSeconds);

			// 결과 만들기 (시드1, 나머지 5)
			List<ArtistDto>[] resultArtistDtoList = new ArrayList[2];
			int resultIndex = 0;
			Iterator<ArtistDto> iterator = artistDtoList.iterator();

			for(ArtistDto artistDto : seedArtistList){
				resultArtistDtoList[resultIndex] = new ArrayList<>();
				while(iterator.hasNext()){
					// 시드 아티스트를 갖고 있는 유사아티스트는 결과 목록으로
					PreferSimilarArtistDto tempArtistDto = (PreferSimilarArtistDto)iterator.next();

					if(tempArtistDto.getSeedArtistId().equals(artistDto.getArtistId())) {
						resultArtistDtoList[resultIndex].add(tempArtistDto);
					}
				}

				if(!CollectionUtils.isEmpty(resultArtistDtoList[resultIndex])) {
					// 20명 중 5명 랜덤 추출
					resultArtistDtoList[resultIndex] = resultArtistDtoList[resultIndex].stream().limit(20).collect(Collectors.toList());
					resultArtistDtoList[resultIndex] = rand.ints(20, 0, totalSeedArtistList.size()).mapToObj(resultArtistDtoList[resultIndex]::get).limit(5)
							.collect(Collectors.toList());
					// 시드 아티스트를 섹션 맨 앞에 추가
					resultArtistDtoList[resultIndex].add(0, artistDto);

					// 시드 아티스트 + 유사아티스트가 3명 미만이면 노출하지 않는다.
					if (resultArtistDtoList[resultIndex].size() < 3) {
						resultArtistDtoList[resultIndex].clear();
						resultArtistDtoList[resultIndex].add(ArtistDto.builder().artistId(-1L).build());
					}
				}

				resultIndex++;
			}

			setRedisWithArtistDtoList(characterNo, resultArtistDtoList);

			artistDtoList = resultArtistDtoList[sectionNumber -1];

		}
		if (isEmptyArtistDtoList(artistDtoList)) {
			return null;
		}

		return preferenceArtistListConvert(artistDtoList);
	}

	private boolean isEmptyArtistDtoList(List<ArtistDto> artistDtoList) {

    	try{
			if (CollectionUtils.isEmpty(artistDtoList) ||
					ObjectUtils.isEmpty(artistDtoList.get(0).getArtistId()) ||
					new Long(-1L).equals(artistDtoList.get(0).getArtistId())
			) {
				return true;
			}

	    }catch(Exception e){
    	    log.error(e.getMessage());
	    }

		return false;
	}

	private void setRedisWithArtistDtoList(Long characterNo, List<ArtistDto>[] resultArtistDtoList) {

		if(resultArtistDtoList == null) {
			resultArtistDtoList = makeEmptyArtistDtoList();
		}

		// 캐쉬 만기는 당일 자정
		long expireSeconds = LocalTime.now().until(LocalTime.MAX, ChronoUnit.SECONDS);

		redisService.setWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, 1, characterNo), resultArtistDtoList[0], (int) expireSeconds);
		redisService.setWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, 2, characterNo), resultArtistDtoList[1], (int) expireSeconds);
	}

	private List<ArtistDto>[] makeEmptyArtistDtoList() {

		List<ArtistDto>[] resultArtistDtoList = new ArrayList[2];
		resultArtistDtoList[0] = new ArrayList<>();
		resultArtistDtoList[1] = new ArrayList<>();
		resultArtistDtoList[0].add(ArtistDto.builder().build());
		resultArtistDtoList[1].add(ArtistDto.builder().build());

		return resultArtistDtoList;
	}

	private List<ArtistDto>[] makeNoDataArtistDtoList() {

		List<ArtistDto>[] resultArtistDtoList = new ArrayList[2];
		resultArtistDtoList[0] = new ArrayList<>();
		resultArtistDtoList[1] = new ArrayList<>();
		resultArtistDtoList[0].add(ArtistDto.builder().artistId(-1L).build());
		resultArtistDtoList[1].add(ArtistDto.builder().build());

		return resultArtistDtoList;
	}


	@Override
	public ChartResponse getPreferSimilarArtistList(Long characterNo, Integer sectionNumber) {

		List<Artist> similarArtistList = makeSimilarArtistList(characterNo, sectionNumber);

		if(CollectionUtils.isEmpty(similarArtistList)){
			return null;
		}

		return new ChartResponse<>(similarArtistList, (sectionNumber == 1 ? HomeContentType.ARTIST1 : HomeContentType.ARTIST2) );
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

		String personalSimilarArtistKey = String.format(PERSONAL_SIMILAR_ARTIST_KEY, 1, characterNo);

		// redis 캐쉬 검색
		List<ArtistDto> artistDtoList = redisService.getListWithPrefix(personalSimilarArtistKey, ArtistDto.class);

		if (CollectionUtils.isEmpty(artistDtoList)) {
			return null;
		}

		log.debug("XXXXXXXX" + artistDtoList.get(0).getArtistId());

		// artist id 가 빈 경우에 캐쉬를 갱신
		if(ObjectUtils.isEmpty(artistDtoList.get(0).getArtistId())) {
			redisService
					.delWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_HISTORY_KEY, characterNo));
			redisService.delWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, 1, characterNo));
			redisService.delWithPrefix(String.format(PERSONAL_SIMILAR_ARTIST_KEY, 2, characterNo));
		}

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
