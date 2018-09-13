/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendDummyDataRequest;
import com.sktechx.godmusic.personal.rest.repository.RecommendDummyDataMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 설명 : XXXXXXXX
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 08. 25.
 */
@Slf4j
@Service
public class RecommendDataServiceImpl implements RecommendDataService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private RecommendDummyDataMapper recommendDummyDataMapper;


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
            log.error("Recommend :: updateRecommendDataPrevent :: Error Message",e.getMessage());
        }
    }

    @Override
    public void createRecommendDummyData(Long characterNo, RecommendDummyDataRequest recommendDummyDataRequest) {
        //유효성 검사
        validateRecommendDummyDataRequest(recommendDummyDataRequest);

        //1단계 데이터
        if(PersonalPhaseType.VISIT.getPhase() == recommendDummyDataRequest.getRcmmdPhase()){
            //2, 3단계 데이터 삭제
            Long rcmmdId = recommendDummyDataMapper.selectRcmmdMforuRcmmdId(characterNo);
            if(rcmmdId!= null){
                recommendDummyDataMapper.deleteRcmmdMforuSubData(characterNo);
                recommendDummyDataMapper.deleteRcmmdMforuData(characterNo);

            }

            rcmmdId = recommendDummyDataMapper.selectRcmmdSimilarTrackRcmmdId(characterNo);
            if(rcmmdId!= null){
                recommendDummyDataMapper.deleteRcmmdSimilarTrackSubData(characterNo);
                recommendDummyDataMapper.deleteRcmmdSimilarTrackData(characterNo);

            }
            rcmmdId = recommendDummyDataMapper.selectRcmmdListenMoodChnlRcmmdId(characterNo);
            if(rcmmdId!= null){
                recommendDummyDataMapper.deleteRcmmdListenMoodChnlTrackData(characterNo,rcmmdId);
            }
        }
        //2단계 데이터
        if(PersonalPhaseType.LISTEN.getPhase() == recommendDummyDataRequest.getRcmmdPhase()){
            //3단계 데이터 삭제
            Long rcmmdId = recommendDummyDataMapper.selectRcmmdMforuRcmmdId(characterNo);
            if(rcmmdId!= null){
                recommendDummyDataMapper.deleteRcmmdMforuSubData(characterNo);
                recommendDummyDataMapper.deleteRcmmdMforuData(characterNo);

            }
            rcmmdId = recommendDummyDataMapper.selectRcmmdSimilarTrackRcmmdId(characterNo);
            if(rcmmdId== null){
                //2단계 데이터 insert
                List<Long> svcGenreIdList = recommendDummyDataMapper.selectRandomSvcGenreId(recommendDummyDataRequest.getPanelCount());
                for( int i = 0 ; i < recommendDummyDataRequest.getPanelCount() ; i++){
                    RecommendTrackDto recommendTrackDto = new RecommendTrackDto();
                    recommendTrackDto.setCharacterNo(characterNo);
                    recommendTrackDto.setDispSn(i+1);
                    recommendTrackDto.setSvcGenreId(svcGenreIdList.get(i));
                    recommendDummyDataMapper.insertRcmmdSimilarTrackData(recommendTrackDto);
                    recommendDummyDataMapper.insertRcmmdSimilarTrackSubData( recommendTrackDto.getRcmmdId());
                }

            }
        }
        //3단계 데이터
        if(PersonalPhaseType.RECOMMEND.getPhase() == recommendDummyDataRequest.getRcmmdPhase()){
            Long rcmmdId = recommendDummyDataMapper.selectRcmmdMforuRcmmdId(characterNo);
            if(rcmmdId == null){
                //3단계 데이터 insert
                List<Long> svcGenreIdList = recommendDummyDataMapper.selectRandomSvcGenreId(recommendDummyDataRequest.getPanelCount());
                for( int i = 0 ; i < recommendDummyDataRequest.getPanelCount() ; i++){
                    RecommendTrackDto recommendTrackDto = new RecommendTrackDto();
                    recommendTrackDto.setCharacterNo(characterNo);
                    recommendTrackDto.setDispSn(i+1);
                    recommendTrackDto.setSvcGenreId(svcGenreIdList.get(i));
                    recommendDummyDataMapper.insertRcmmdMforuData(recommendTrackDto);
                    recommendDummyDataMapper.insertRcmmdMforuSubData( recommendTrackDto.getRcmmdId());
                }
            }
        }
        //4단계 데이터
        //TODO : TOP 추천 데이터 ...
        if(4 == recommendDummyDataRequest.getRcmmdPhase()){

        }

        //캐시 삭제
        String personalRecommendPhaseKey = String.format(RedisKeyConstant.PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
        redisService.delWithPrefix(personalRecommendPhaseKey);
    }

    private void validateRecommendDummyDataRequest(RecommendDummyDataRequest recommendDummyDataRequest){
        Integer panelCount = recommendDummyDataRequest.getPanelCount();
        Integer rcmmdPhase = recommendDummyDataRequest.getRcmmdPhase();

        if(PersonalPhaseType.VISIT.getPhase() == rcmmdPhase
                || PersonalPhaseType.LISTEN.getPhase() == rcmmdPhase
                    || PersonalPhaseType.RECOMMEND.getPhase() == rcmmdPhase){

            if(panelCount > 0 && panelCount <=2){
                return ;
            }
        }
        throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
    }


}