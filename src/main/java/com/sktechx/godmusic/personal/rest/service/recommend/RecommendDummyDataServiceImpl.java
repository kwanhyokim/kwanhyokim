/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.client.MetaClient;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendV2DummyDataRequest;
import com.sktechx.godmusic.personal.rest.model.vo.test.RecommendChartRequest;
import com.sktechx.godmusic.personal.rest.repository.RecommendDummyDataMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.chart.ChartService;
import com.sktechx.godmusic.personal.rest.service.mongo.PersonalMongoClient;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant.PERSONAL_CHART_KEY;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 25.
 */
@Slf4j
@Service
public class RecommendDummyDataServiceImpl implements RecommendDummyDataService {

    private final RedisService redisService;
    private final RecommendMapper recommendMapper;

    private final ChartService chartService;

    private final RecommendDummyDataMapper recommendDummyDataMapper;

    private final RecommendPanelService recommendPanelService;

    private final PersonalMongoClient personalMongoClient;

    private final MetaClient metaClient;

    public RecommendDummyDataServiceImpl(RedisService redisService,
            RecommendMapper recommendMapper,
            @Qualifier("chartService") ChartService chartService,
            RecommendDummyDataMapper recommendDummyDataMapper,
            RecommendPanelService recommendPanelService,
            PersonalMongoClient personalMongoClient,
            MetaClient metaClient) {
        this.redisService = redisService;
        this.recommendMapper = recommendMapper;
        this.chartService = chartService;
        this.recommendDummyDataMapper = recommendDummyDataMapper;
        this.recommendPanelService = recommendPanelService;
        this.personalMongoClient = personalMongoClient;
        this.metaClient = metaClient;
    }

    @Override
    public void updateRecommendDataRemovePrevent(ListenRequest request, Long characterNo) {

        try{
            if(request != null){
                RecommendPanelContentType recommendPanelContentType = RecommendPanelContentType.fromCode(request.getListenType());
                if(recommendPanelContentType != null && request.getListenTypeId() != null && characterNo != null){
                    recommendMapper.updateRecommendDataRemovePrevent(recommendPanelContentType , request.getListenTypeId() , characterNo);
                }
            }
        }catch(Exception e){
            log.error("recommend :: updateRecommendDataPrevent :: Error Message {}",e.getMessage());
        }
    }

    @Override
    public void createRecommendV2DummyData(Long characterNo, RecommendV2DummyDataRequest recommendV2DummyDataRequest) {

        List<Long> svcGenreIdList;

        switch(recommendV2DummyDataRequest.getType()){
            // 나를 위한 새로운 발견
            case "RC_CF_TR":
                svcGenreIdList = recommendDummyDataMapper.selectRandomSvcGenreId(recommendV2DummyDataRequest.getPanelCount());
                for( int i = 0 ; i < recommendV2DummyDataRequest.getPanelCount() ; i++){
                    RecommendTrackDto recommendTrackDto = RecommendTrackDto.builder()
                            .characterNo(characterNo)
                            .dispSn(i+1)
                            .svcGenreId(svcGenreIdList.get(i))
                            .build();
                    recommendDummyDataMapper.insertRcmmdMforuData(recommendTrackDto);
                    recommendDummyDataMapper.insertRcmmdMforuSubData( recommendTrackDto.getRcmmdId());
                }
                break;
            // 오늘의 추천
            case "RC_SML_TR":
//                svcGenreIdList = recommendDummyDataMapper.selectRandomSvcGenreId(recommendV2DummyDataRequest.getPanelCount());
                for( int i = 0 ; i < recommendV2DummyDataRequest.getPanelCount() ; i++){
                    RecommendTrackDto recommendTrackDto = RecommendTrackDto.builder()
                            .characterNo(characterNo)
                            .dispSn(i+1)
                            .svcGenreId(0L)
                            .build();
                    recommendDummyDataMapper.insertRcmmdSimilarTrackData(recommendTrackDto);
                    recommendDummyDataMapper.insertRcmmdSimilarTrackSubData( recommendTrackDto.getRcmmdId());
                }
                break;

            case "RC_ATST_TR":
                if(!existCharacterPreferArtist(characterNo)){
                    recommendDummyDataMapper.insertCharacterPreferArtist(characterNo, 1L, 1876L);
                    recommendDummyDataMapper.insertCharacterPreferArtist(characterNo, 2L, 80122235L);
                    recommendDummyDataMapper.insertCharacterPreferArtist(characterNo, 14L, 1139L);
                }

                recommendPanelService.addPreferArtistPanel(characterNo);
                break;
        }

        //캐시 삭제
        clearCacheHome(characterNo);
    }

	@Override
	public void updateRecommendV2DummyData(Long characterNo, RecommendV2DummyDataRequest recommendV2DummyDataRequest) {
		switch(recommendV2DummyDataRequest.getType()){
			// 나를 위한 새로운 발견
			case "RC_CF_TR":
				recommendDummyDataMapper.updateRcmmdMforuData(characterNo);
				break;
			// 오늘의 추천
			case "RC_SML_TR":
				recommendDummyDataMapper.updateRcmmdSimilarTrackData(characterNo);
				break;

			case "RC_ATST_TR":
				recommendDummyDataMapper.updateRcmmdArtistData(characterNo);
				break;
		}

		//캐시 삭제
        clearCacheHome(characterNo);
    }

	@Override
    public void deleteRecommendV2DummyData(Long characterNo, RecommendV2DummyDataRequest recommendV2DummyDataRequest) {

        switch(recommendV2DummyDataRequest.getType()){
            // 나를 위한 새로운 발견
            case "RC_CF_TR":
                    recommendDummyDataMapper.deleteRcmmdMforuData(characterNo);
                    recommendDummyDataMapper.deleteRcmmdMforuSubData(characterNo);
                break;
            // 오늘의 추천
            case "RC_SML_TR":
                recommendDummyDataMapper.deleteRcmmdSimilarTrackData(characterNo);
                recommendDummyDataMapper.deleteRcmmdSimilarTrackSubData(characterNo);
                break;

            case "RC_ATST_TR":
                recommendDummyDataMapper.deleteArtistFlo(characterNo);
                break;

        }

        //캐시 삭제
        clearCacheHome(characterNo);
    }
    @Override
    public void updateAfloChnl() {
        recommendDummyDataMapper.updateAfloChannel();
    }

    @Override
    public void addChart(Long characterNo) {

        recommendDummyDataMapper.insertOrUpdateCharacterPreferDisp(characterNo, 1L);
        recommendDummyDataMapper.insertOrUpdateCharacterPreferDisp(characterNo, 2L);
        clearCacheHome(characterNo);
    }

    @Override
    public String clearCacheHome(Long characterNo) {
        String personalRecommendPhaseKey = String
                .format(RedisKeyConstant.PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
        return (redisService.delWithPrefix(personalRecommendPhaseKey) ? "true" : "false");
    }
    @Override
    public void addPrivateChart(Long characterNo, String mix) {

        ChartDto chartDto = chartService.getChartDtoForHomeByDispPropsTypeWithTrackList(characterNo,
                "TOP100", OsType.IOS, 100);


        redisService.delWithPrefix(String.format(PERSONAL_CHART_KEY, chartDto.getChartId(),
                characterNo));

        Collections.shuffle(chartDto.getTrackList());

        int i =0;

        for(TrackDto trackDto : chartDto.getTrackList()) {
            trackDto.getRank().setRankBadge(i++);
        }

        personalMongoClient.addRecommendChart(characterNo,
                RecommendChartRequest.builder()
                        .chartId(chartDto.getChartId())
                        .characterNo(characterNo)
                        .chartTaste(mix)
                        .trackIdList(

                                chartDto.getTrackList().stream().map(TrackDto::getTrackId).collect(
                                        Collectors.toList())
                        )
                .build()
        );

        chartDto = chartService.getChartDtoForHomeByDispPropsTypeWithTrackList(characterNo,
                "KIDS100", OsType.IOS, 100);

        Collections.shuffle(chartDto.getTrackList());

        i =0;

        for(TrackDto trackDto : chartDto.getTrackList()) {
            trackDto.getRank().setRankBadge(i++);
        }

        redisService.delWithPrefix(String.format(PERSONAL_CHART_KEY, chartDto.getChartId(),
                characterNo));


        personalMongoClient.addRecommendChart(characterNo,
                RecommendChartRequest.builder()
                        .chartId(chartDto.getChartId())
                        .characterNo(characterNo)
                        .chartTaste(mix)
                        .trackIdList(

                                chartDto.getTrackList().stream().map(TrackDto::getTrackId).collect(
                                        Collectors.toList())
                        )
                        .build()
        );


    }
    @Override
    public void deletePrivateChart(Long characterNo) {

        ChartDto chartDto = chartService.getChartDtoForHomeByDispPropsTypeWithTrackList(characterNo,
                "TOP100", OsType.IOS, 100);

        redisService.delWithPrefix(String.format(PERSONAL_CHART_KEY, chartDto.getChartId(),
                characterNo));

        personalMongoClient.deleteRecommendChart(characterNo,
                RecommendChartRequest.builder()
                        .chartId(chartDto.getChartId())
                        .characterNo(characterNo)
                .build()
        );

        chartDto = chartService.getChartDtoForHomeByDispPropsTypeWithTrackList(characterNo,
                "KIDS100", OsType.IOS, 100);

        redisService.delWithPrefix(String.format(PERSONAL_CHART_KEY, chartDto.getChartId(),
                characterNo));

        personalMongoClient.deleteRecommendChart(characterNo,
                RecommendChartRequest.builder()
                        .chartId(chartDto.getChartId())
                        .characterNo(characterNo)
                .build()
        );
    }

    @Override
    public void deleteChart(Long characterNo) {
        recommendDummyDataMapper.deleteCharacterPreferDisp(characterNo);
        clearCacheHome(characterNo);
    }


    public int addTpoRecommendDummyData(Long characterNo){
        if(recommendDummyDataMapper.selectTpoRecommendDataCount(characterNo) < 1)
            return recommendDummyDataMapper.insertTpoRecommendData(characterNo);
        return 0;
    }

    public int deleteTpoRecommendDummyData(Long characterNo){
        return recommendDummyDataMapper.deleteTpoRecommendData(characterNo);
    }

    private Boolean existCharacterPreferArtist(Long characterNo){
        return recommendDummyDataMapper.selectCharacterPreferArtist(characterNo) > 0;

    }

}