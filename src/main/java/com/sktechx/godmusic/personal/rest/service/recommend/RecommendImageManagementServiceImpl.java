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

package com.sktechx.godmusic.personal.rest.service.recommend;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.type.ImageDisplayType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.repository.RecommendImageManagementMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 11. 3.
 */
@Service
@Slf4j
public class RecommendImageManagementServiceImpl implements RecommendImageManagementService {

    private final RecommendImageManagementMapper recommendImageManagementMapper;

    private final RedisService redisService;

    public RecommendImageManagementServiceImpl(
            RecommendImageManagementMapper recommendImageManagementMapper,
            RedisService redisService

    ) {
        this.recommendImageManagementMapper = recommendImageManagementMapper;
        this.redisService = redisService;
    }

    @Override
    public List<ImageManagementDto> getRecommendImageList(RecommendPanelContentType recommendType, Long recommendId, ImageDisplayType imageType, OsType osType) {
        List<ImageManagementDto> imageList = Collections.emptyList();

        switch (recommendType) {
            case RC_SML_TR:
                imageList = recommendImageManagementMapper.selectRecommendImageManagementList(recommendType, recommendId, imageType, osType);
                break;

            case RC_ATST_TR:
                imageList = recommendImageManagementMapper.selectFixedRecommendImageList(recommendType, recommendId, imageType, osType);
                break;

            case RC_GR_TR:
            case RC_CF_TR:
                imageList = recommendImageManagementMapper.selectMappingImageRecommendImageList(recommendType, recommendId, imageType, osType);
                break;
        }

        return imageList;
    }


    @Override
    public List<ImageInfo> getRecommendPanelDefaultImageList(OsType osType){

        List<ImageInfo> imgList = null;

        try{
            imgList = redisService.getListWithPrefix(RedisKeyConstant.RECOMMEND_PANEL_DEFAULT_IMGLIST_KEY,ImageInfo.class);
        }catch(Exception e){
            log.error("getRecommendPanelDefaultImageList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(imgList)){
                imgList = recommendImageManagementMapper.selectRecommendPanelDefaultImageList();
                if(!CollectionUtils.isEmpty(imgList)){
                    int recommendPanelDefaultImageExpiredSec = 3600;
                    redisService.setWithPrefix(RedisKeyConstant.RECOMMEND_PANEL_DEFAULT_IMGLIST_KEY, imgList,
                            recommendPanelDefaultImageExpiredSec);
                }
            }
        }

        if(!CollectionUtils.isEmpty(imgList)){
            Collections.shuffle(imgList);

            return Collections.singletonList(
                    imgList.stream().filter(imageInfo -> osType.equals(imageInfo.getOsType()))
                            .findFirst().orElse(null));
        }
        return null;
    }

    @Override
    public List<ImageInfo> selectRecommendPanelInfoBgImageUrl(
            RecommendPanelContentType recommendPanelContentType, Long rcmmdId,
            OsType osType, int dispSn) {

        return Stream.of(75L, 140L, 200L, 350L, 500L, 1000L)
                .map(
                        size -> {
                            ImageInfo imageInfo = new ImageInfo();
                            imageInfo.setSize(size);
                            imageInfo.setUrl(
                                    Optional.ofNullable(
                                            recommendImageManagementMapper.selectRecommendPanelInfoBgImageUrl(
                                                    recommendPanelContentType, rcmmdId, osType , (dispSn == 0 ? 1 : dispSn))
                                    ).orElseGet(
                                            () -> getRecommendPanelDefaultImageList(osType).get(0)
                                                    .getUrl()
                                    )
                            );

                            return imageInfo;
                        })
                .collect(Collectors.toList());

    }
    @Override
    @Deprecated
    public List<ImageInfo> getAdaptivePanelHomeImageList(OsType osType) {
        List<ImageInfo> imgList = null;

        try{
            imgList = redisService.getListWithPrefix(RedisKeyConstant.REACTIVE_PANEL_IMGLIST_KEY,
                    ImageInfo.class);

        }catch(Exception e){
            log.error("getAdaptivePanelHomeImageList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(imgList)){
                imgList = recommendImageManagementMapper.selectAdaptivePanelHomeImageList();
                if(!CollectionUtils.isEmpty(imgList)){
                    int recommendPanelDefaultImageExpiredSec = 3600;
                    redisService.setWithPrefix(RedisKeyConstant.REACTIVE_PANEL_IMGLIST_KEY, imgList,
                            recommendPanelDefaultImageExpiredSec);
                }
            }
        }

        if(!CollectionUtils.isEmpty(imgList)){
            Collections.shuffle(imgList);

            return Collections.singletonList(
                    imgList.stream().filter(imageInfo -> osType.equals(imageInfo.getOsType()))
                            .findFirst().orElse(null));
        }
        return null;
    }

    @Override
    public List<ImageInfo> getAdaptivePanelBgImageAtRandomlyByOsType(OsType osType, int count) {
        List<ImageInfo> imageInfoList = fetchAllAdaptivePanelBgImageAndCacheIt().getImageListByOsType(osType);
        Collections.shuffle(imageInfoList);
        return imageInfoList.subList(0, Math.min(count, imageInfoList.size()));
    }

    private AdaptivePanelBgImageHolder fetchAllAdaptivePanelBgImageAndCacheIt() {

        AdaptivePanelBgImageHolder imageHolder =
                redisService.getWithPrefix(RedisKeyConstant.REACTIVE_PANEL_IMGLIST_KEY, AdaptivePanelBgImageHolder.class);

        if (imageHolder == null) {
            imageHolder = new AdaptivePanelBgImageHolder(
                    recommendImageManagementMapper
                            .selectAdaptivePanelHomeImageList()
                            .stream()
                            .collect(Collectors.groupingBy(ImageInfo::getOsType)));

            if (imageHolder.isNotEmpty()) {
                redisService.setWithPrefix(RedisKeyConstant.REACTIVE_PANEL_IMGLIST_KEY, imageHolder, 3_600);
            }
        }

        return imageHolder;
    }

    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Getter
    static class AdaptivePanelBgImageHolder {

        private Map<OsType, List<ImageInfo>> osTypeImagesPair;

        List<ImageInfo> getImageListByOsType(OsType osType) {
            return osTypeImagesPair.getOrDefault(osType, Collections.emptyList());
        }

        boolean isNotEmpty() {
            return !CollectionUtils.isEmpty(osTypeImagesPair);
        }
    }
}
