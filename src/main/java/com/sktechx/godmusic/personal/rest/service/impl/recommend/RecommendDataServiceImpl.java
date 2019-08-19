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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.type.PersonalPhaseType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.RecommendTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendDummyDataRequest;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendV2DummyDataRequest;
import com.sktechx.godmusic.personal.rest.repository.CharacterPreferGenreMapper;
import com.sktechx.godmusic.personal.rest.repository.DevToolMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendDummyDataMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import lombok.extern.slf4j.Slf4j;

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

    @Autowired
    private CharacterPreferGenreMapper characterPreferGenreMapper;

    @Autowired
    private DevToolMapper devToolMapper;

    @Autowired
    private RecommendPanelService recommendPanelService;

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

        //캐시 삭제
        String personalRecommendPhaseKey = String.format(RedisKeyConstant.PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
        redisService.delWithPrefix(personalRecommendPhaseKey);
    }

    @Override
    public void createRecommendV2DummyData(Long characterNo, RecommendV2DummyDataRequest recommendV2DummyDataRequest) {

        List<Long> svcGenreIdList;

        switch(recommendV2DummyDataRequest.getType()){
            // 나를 위한 FLO
            case "RC_CF_TR":
                svcGenreIdList = recommendDummyDataMapper.selectRandomSvcGenreId(recommendV2DummyDataRequest.getPanelCount());
                for( int i = 0 ; i < recommendV2DummyDataRequest.getPanelCount() ; i++){
                    RecommendTrackDto recommendTrackDto = new RecommendTrackDto();
                    recommendTrackDto.setCharacterNo(characterNo);
                    recommendTrackDto.setDispSn(i+1);
                    recommendTrackDto.setSvcGenreId(svcGenreIdList.get(i));
                    recommendDummyDataMapper.insertRcmmdMforuData(recommendTrackDto);
                    recommendDummyDataMapper.insertRcmmdMforuSubData( recommendTrackDto.getRcmmdId());
                }
                break;
            // 오늘의 FLO
            case "RC_SML_TR":
//                svcGenreIdList = recommendDummyDataMapper.selectRandomSvcGenreId(recommendV2DummyDataRequest.getPanelCount());
                for( int i = 0 ; i < recommendV2DummyDataRequest.getPanelCount() ; i++){
                    RecommendTrackDto recommendTrackDto = new RecommendTrackDto();
                    recommendTrackDto.setCharacterNo(characterNo);
                    recommendTrackDto.setDispSn(i+1);
                    recommendTrackDto.setSvcGenreId(0L);
                    recommendDummyDataMapper.insertRcmmdSimilarTrackData(recommendTrackDto);
                    recommendDummyDataMapper.insertRcmmdSimilarTrackSubData( recommendTrackDto.getRcmmdId());
                }
                break;

            case "RC_ATST_TR":
                if(!existCharacterPreferArtist(characterNo)){
                    devToolMapper.insertCharacterPreferArtist(characterNo, 1L, 1876L);
                    devToolMapper.insertCharacterPreferArtist(characterNo, 2L, 80122235L);
                    devToolMapper.insertCharacterPreferArtist(characterNo, 14L, 1139L);
                }

                recommendPanelService.addPreferArtistPanel(characterNo);
                break;
        }

        //캐시 삭제
        String personalRecommendPhaseKey = String.format(RedisKeyConstant.PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
        redisService.delWithPrefix(personalRecommendPhaseKey);
    }

	@Override
	public void updateRecommendV2DummyData(Long characterNo, RecommendV2DummyDataRequest recommendV2DummyDataRequest) {
		switch(recommendV2DummyDataRequest.getType()){
			// 나를 위한 FLO
			case "RC_CF_TR":
				recommendDummyDataMapper.updateRcmmdMforuData(characterNo);
				break;
			// 오늘의 FLO
			case "RC_SML_TR":
				recommendDummyDataMapper.updateRcmmdSimilarTrackData(characterNo);
				break;

			case "RC_ATST_TR":
				recommendDummyDataMapper.updateRcmmdArtistData(characterNo);
				break;
		}

		//캐시 삭제
		String personalRecommendPhaseKey = String.format(RedisKeyConstant.PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
		redisService.delWithPrefix(personalRecommendPhaseKey);
	}

	@Override
    public void deleteRecommendV2DummyData(Long characterNo, RecommendV2DummyDataRequest recommendV2DummyDataRequest) {

        List<Long> svcGenreIdList;

        switch(recommendV2DummyDataRequest.getType()){
            // 나를 위한 FLO
            case "RC_CF_TR":
                    recommendDummyDataMapper.deleteRcmmdMforuData(characterNo);
                    recommendDummyDataMapper.deleteRcmmdMforuSubData(characterNo);
                break;
            // 오늘의 FLO
            case "RC_SML_TR":
                recommendDummyDataMapper.deleteRcmmdSimilarTrackData(characterNo);
                recommendDummyDataMapper.deleteRcmmdSimilarTrackSubData(characterNo);
                break;

            case "RC_ATST_TR":
                recommendDummyDataMapper.deleteArtistFlo(characterNo);
                break;

        }

        //캐시 삭제
        String personalRecommendPhaseKey = String.format(RedisKeyConstant.PERSONAL_RECOMMEND_PHASE_KEY, characterNo);
        redisService.delWithPrefix(personalRecommendPhaseKey);
    }
    @Override
    public void updateAfloChnl() {
        recommendDummyDataMapper.updateAfloChannel();
    }

    @Override
    public void addChart(Long characterNo) {

        devToolMapper.insertOrUpdateCharacterPreferDisp(characterNo, 1L);
        devToolMapper.insertOrUpdateCharacterPreferDisp(characterNo, 2L);
    }

    @Override
    public void deleteChart(Long characterNo) {

        devToolMapper.deleteCharacterPreferDisp(characterNo);
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

    public int addTpoRecommendDummyData(Long characterNo){
        if(recommendDummyDataMapper.selectTpoRecommendDataCount(characterNo) < 1)
            return recommendDummyDataMapper.insertTpoRecommendData(characterNo);
        return 0;
    }

    public int deleteTpoRecommendDummyData(Long characterNo){
        return recommendDummyDataMapper.deleteTpoRecommendData(characterNo);
    }

    public Boolean existCharacterPreferArtist(Long characterNo){
        return devToolMapper.selectCharacterPreferArtist(characterNo) > 0;

    }

}