/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 10. 13.
 */
@Getter
@Builder
public class MostWatchedVideoDto {

    private Long characterNo;

    private Long videoId;

    private Integer watchCount;

    private Date lastWatchDateTime;

    private Date createDateTime;

    private Date updateDateTime;

    public MostWatchedVideoDto() {
    }

    public MostWatchedVideoDto(Long characterNo, Long videoId, Integer watchCount, Date lastWatchDateTime, Date createDateTime, Date updateDateTime) {
        this.characterNo = characterNo;
        this.videoId = videoId;
        this.watchCount = watchCount;
        this.lastWatchDateTime = lastWatchDateTime;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MostWatchedVideoDto that = (MostWatchedVideoDto) o;
        return characterNo.equals(that.characterNo) &&
                videoId.equals(that.videoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterNo, videoId);
    }
}
