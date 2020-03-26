/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.service.recommend;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.header.RecommendPanelHeaderVo;
import com.sktechx.godmusic.personal.rest.repository.ArtistMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-19
 */

@Service("recommendPanelHeaderService")
public class RecommendPanelHeaderServiceImpl implements RecommendPanelHeaderService{

    @Autowired
    private RecommendPanelService recommendPanelService;

    @Autowired
    private RecommendReadService recommendReadService;

    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Autowired
    private ArtistMapper artistMapper;

    @Override
    public RecommendPanelHeaderVo getRecommendPanelInfo(Long characterNo, String rcmmdType,
            Long panelContentId, OsType osType) {

        RecommendPanelHeaderVo panel = null;

        ListDto<List<RecommendPanelTrackDto>> trackList = recommendPanelService.getRecommendPanelTrackList(characterNo, rcmmdType, panelContentId);

        int trackCount = 0;

        if( trackList != null && !CollectionUtils.isEmpty(trackList.getList())){
            trackCount = trackList.getList().size();
        }

        switch (rcmmdType){
            // 아티스트
            case "RC_ATST_TR":

                RecommendArtistDto recommendArtistDto = recommendReadMapper.selectRecommendArtistById(panelContentId);

                List<ArtistDto> artistDtoList;

                if(recommendArtistDto == null || CollectionUtils.isEmpty(recommendArtistDto.getArtistList())){
                    artistDtoList = artistMapper.getArtistList(Arrays.asList(12L,14L,15L));
                }else {
                    artistDtoList = recommendArtistDto.getArtistList();
                }

                List<String> artistNameList = artistDtoList.stream().map(ArtistDto::getArtistName).limit(5).collect(
                        Collectors.toList());

                // 아티스트 이미지가 default 이미지인것은 목록에서 뒷 부분으로 위치를 변경한다
                List<ArtistDto> normalImageArtistList = new LinkedList<>();
                List<ArtistDto> defaultImageArtistList = new LinkedList<>();
                artistDtoList.forEach(x ->  {
                    if( x.hasDefaultImage() ) {
                        defaultImageArtistList.add(x);
                    } else {
                        normalImageArtistList.add(x);
                    }
                });
                if(defaultImageArtistList.size() > 0)   {
                    normalImageArtistList.addAll(defaultImageArtistList);
                }

                // 아티스트의 첫 이미지를 배경 이미지로 사용
                panel =  RecommendPanelHeaderVo.builder()
                        .title(RecommendConstant.ARTIST_PANEL_TITLE)
                        .subTitle(String.join(",", artistNameList))
                        .imgList(
                                artistDtoList.get(0) == null ? null : artistDtoList.get(0).getImgList())
                        .artistList(normalImageArtistList)
                        .artistCount(artistDtoList.size())
                        .createDtime(Objects.requireNonNull(recommendArtistDto).getCreateDtime())
                        .newYn(YnType.Y)
                        .build();
                break;
            // 선호 유사
            case "RC_SML_TR":

                List<RecommendTrackDto> similarTrackList =
                        recommendReadMapper.selectRecommendSimilarTrackListByIdList(
                                Collections.singletonList(panelContentId), 1,
                                1, osType);

                int dispSn = 1;
                Date similarTrackCreateDtime = null;
                if(!CollectionUtils.isEmpty(similarTrackList)){
                    RecommendTrackDto recommendTrackDto = similarTrackList.get(0);
                    if(recommendTrackDto != null){
                        dispSn = recommendTrackDto.getDispSn();
                        similarTrackCreateDtime = recommendTrackDto.getRcmmdCreateDtime();
                    }
                }

                panel = RecommendPanelHeaderVo.builder()
                        .title(RecommendConstant.SIMILAR_TRACK_PANEL_TITLE)
                        .subTitle(RecommendConstant.SIMILAR_TRACK_PANEL_DETAIL_SUB_TITLE)
                        .imgList(getRecommendPanelInfoBgImage(rcmmdType, panelContentId, osType , dispSn))
                        .trackCount(trackCount)
                        .newYn(YnType.Y)
                        .createDtime(similarTrackCreateDtime)
                        .renewDtime(new Date())
                        .build();
                break;
            // 유사 장르
            case "RC_GR_TR":
                Date createDateTime = new Date();
                panel = RecommendPanelHeaderVo.builder()
                        .title(RecommendConstant.SIMILAR_TRACK_PANEL_TITLE)
                        .subTitle(RecommendConstant.SIMILAR_TRACK_PANEL_DETAIL_SUB_TITLE)
                        .imgList(getRecommendPanelInfoBgImage(rcmmdType, panelContentId, osType , 0) )
                        .trackCount(trackCount)
                        .newYn(YnType.Y)
                        .createDtime(createDateTime)
                        .renewDtime(createDateTime)
                        .build();

                break;
            // 추천

            case "RC_CF_TR":
                RecommendForMeDto recommendGenreVo = recommendReadMapper.selectRecommendGenreByRcmmdId(panelContentId);
                String genreNm = "";
                Date createDTime = new Date();
                YnType newYn = YnType.N;

                if(!ObjectUtils.isEmpty(recommendGenreVo)){
                    genreNm = recommendGenreVo.getSvcGenreNm();
                    createDTime = recommendGenreVo.getDispStdStartDt();

                    Date stdDate = new Date((System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
                    if(stdDate.before(createDTime)){
                        newYn = YnType.Y;
                    }
                }

                panel = RecommendPanelHeaderVo.builder()
                        .title(RecommendConstant.RCMMD_TRACK_PANEL_TITLE)
                        .subTitle(String.format(RecommendConstant.RCMMD_TRACK_PANEL_DETAIL_SUB_TITLE,(genreNm)))
                        .imgList(getRecommendPanelInfoBgImage(rcmmdType, panelContentId, osType , 0))
                        .trackCount(trackCount)
                        .newYn(newYn)
                        .createDtime(recommendGenreVo.getDispStdStartDt())
                        .renewDtime(createDTime)
                        .build();
                break;
        }

        return panel;
    }

    @Override
    public List<ImageInfo> getRecommendPanelInfoBgImage(String recommendPanelContentType,Long panelContentId, OsType osType , int dispSn){

        return Stream.of(75L, 140L, 200L, 350L, 500L, 1000L)
                .map( size -> {
                    ImageInfo imageInfo = new ImageInfo();
                    imageInfo.setSize(size);
                    imageInfo.setUrl(

                            Optional.ofNullable(
                                    recommendReadMapper.selectRecommendPanelInfoBgImageUrl(recommendPanelContentType, panelContentId, osType , (dispSn == 0 ? 1 : dispSn))
                            ).orElse(
                                    recommendReadService.getRecommendPanelDefaultImageList(osType).get(0).getUrl()
                            )

                    );
                    return imageInfo;
                })
                .collect(Collectors.toList());
    }
}
