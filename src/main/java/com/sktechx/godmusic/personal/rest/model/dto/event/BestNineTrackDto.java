/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.dto.event;

import com.google.common.collect.Lists;
import lombok.Builder;
import lombok.Data;

import java.util.Comparator;
import java.util.List;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 14.
 */
@Data
@Builder
public class BestNineTrackDto {

    /**
     * 캐릭터 번호
     */
    private Long characterNo;

    /**
     * 베스트 나인 앨범 이미지 PATH without baseURL
     */
    private String bestNineImagePath;

    /**
     * 베스트 나인 SNS 공유 이미지 PATH without baseURL
     */
    private String bestNineShareImagePath;

    /**
     * 베스트 나인 트랙 목록
     */
    private List<BestNineTrack> tracks = Lists.newArrayList();

    /**
     * BestNineTrack 목록을 displayOrder 로 순정렬
     * displayOrder 가 동일할 경우 track name 으로 가나다순 정렬
     */
    public void sortTrackByDisplayOrderAsc() {
        tracks.sort(
                Comparator.comparing(BestNineTrack::getDisplayOrder)
                .thenComparing(Comparator.comparing(BestNineTrack::getName)));
    }

    @Data
    public static class BestNineTrack {
        Long id;
        String name;
        Integer displayOrder;
        BestNineTrackArtist artist;
    }

    @Data
    public static class BestNineTrackArtist {
        Long id;
        String name;
    }
}
