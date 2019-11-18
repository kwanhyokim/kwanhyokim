/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.event;

import com.google.common.collect.Lists;
import com.sktechx.godmusic.personal.rest.model.dto.event.BestNineTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.IdNameVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 14.
 */
@Getter
@Builder
public class BestNineVo {

    @ApiModelProperty("캐릭터 번호")
    private Long characterNo;

    @ApiModelProperty("베스트 나인 앨범 아트 이미지 경로 (랜딩 이미지)")
    private String albumArtImagePath;

    @ApiModelProperty("SNS 공유 이미지 경로")
    private String publishingImagePath;

    @ApiModelProperty("베스트 트랙 목록")
    private List<BestNineTrack> tracks = Lists.newArrayList();

    @Getter
    public static class BestNineTrack extends IdNameVo {

        @ApiModelProperty("전시 순서")
        private Integer displayOrder;

        @ApiModelProperty("대표 아티스트")
        private IdNameVo artist;

        @Builder
        public BestNineTrack(Long id, String name, Integer displayOrder, IdNameVo artist) {
            super(id, name);
            this.displayOrder = displayOrder;
            this.artist = artist;
        }

        public static BestNineTrack from(BestNineTrackDto.BestNineTrack other) {
            BestNineTrackDto.BestNineTrackArtist artist = Optional.ofNullable(other.getArtist()).orElseGet(BestNineTrackDto.BestNineTrackArtist::new);
            IdNameVo bestNineTrackArtist = new IdNameVo(artist.getId(), artist.getName());
            return BestNineTrack.builder()
                    .id(other.getId())
                    .name(other.getName())
                    .displayOrder(other.getDisplayOrder())
                    .artist(bestNineTrackArtist)
                    .build();
        }
    }

    public static BestNineVo from(BestNineTrackDto other, String imageBaseUrl) {

        List<BestNineTrack> tracks = other.getTracks().stream()
                .map(BestNineTrack::from).collect(Collectors.toList());

        return BestNineVo.builder()
                .characterNo(other.getCharacterNo())
                .albumArtImagePath(imageBaseUrl + other.getBestNineImagePath())
                .publishingImagePath(imageBaseUrl + other.getBestNineShareImagePath())
                .tracks(tracks)
                .build();
    }
}
