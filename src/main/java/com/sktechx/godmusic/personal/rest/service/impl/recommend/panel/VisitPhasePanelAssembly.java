/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend.panel;

import com.sktechx.godmusic.personal.common.domain.type.OsType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelType;
import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.ImageDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.channel.PreferGenrePopularChannelPanel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.data.GenreVo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelSignAssembly;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 설명 : 방문 단계 패널 생성기
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 14.
 */
@Slf4j
@Service("visitPhasePanelAssembly")
public class VisitPhasePanelAssembly extends PanelSignAssembly {

    @Override
    protected List<Panel> defaultPanelSetting(PersonalPhaseMeta personalPhaseMeta) {
        final List<Panel> panelList =new ArrayList();

        List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.POPULAR_CHANNEL , personalPhaseMeta.getOsType());
        List<ChnlDto> hotplayChannelList = channelService.getHotplayChannelList(3);

        hotplayChannelList.stream().forEach(channel -> {
            try{
                panelList.add(new PopularChannelPanel(RecommendPanelType.POPULAR_CHANNEL,channel,bgImgList));
            }catch(Exception e){
                log.error("VisitPhasePanel defaultPanelSetting error : {}",e.getMessage());
            }
        });
        return panelList;
    }

    @Override
    protected void appendPreferencePanel(PersonalPhaseMeta personalPhaseMeta ,final List<Panel> panelList){
        List<Panel> addPanelList = getPreferGenrePopularChannelPanelList(personalPhaseMeta.getCharacterNo(),personalPhaseMeta.getOsType());
        if(!CollectionUtils.isEmpty(addPanelList)) {
            IntStream.range(0, addPanelList.size()).forEach(index ->{
                panelList.set(index,addPanelList.get(index));
            });
        }
        appendPreferArtistPanel(personalPhaseMeta,panelList, 0);
    }

    private List<Panel> getPreferGenrePopularChannelPanelList(Long characterNo, OsType osType){
        List<PreferGenrePopularChnlDto> preferGenrePopularChnlList = channelMapper.selectPreferGenrePopularChannel(characterNo);
        if(!CollectionUtils.isEmpty(preferGenrePopularChnlList)) {
            List<ImageDto> bgImgList = recommendPanelService.getPanelBackgroundImageList(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL , osType);
            List<Panel> preferGenrePopularChannelPanelList = new ArrayList();
            preferGenrePopularChnlList.stream().forEach(channel -> {
                try {
                    preferGenrePopularChannelPanelList.add(new PreferGenrePopularChannelPanel(RecommendPanelType.PREFER_GENRE_POPULAR_CHANNEL, channel.getPopularChannel(), new GenreVo(channel), bgImgList));
                } catch (Exception e) {
                    log.error("VisitPhasePanel appendPreferencePanel error : {}", e.getMessage());
                }
            });
            return preferGenrePopularChannelPanelList;
        }
        return null;
    }

}
