/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.dto.chart;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PlayListType;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 차트 API 통신용 DTO
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-04-01
 */

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChartTrackDto {

    private Long id;
    private PlayListType type;
    private String name;

    private List<ImageInfo> imgList;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDateTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDateTime;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date dispStartDtime;

    private Integer totalCount;

    private List<TrackDto> trackList;

    private ChartType chartType;

    private YnType renewYn;

    private String chartTaste;

    private String basedOnUpdate;

    public String getChartTaste(){

        if(chartTaste == null){
            return "NOT_MIXED";
        }

        return chartTaste;
    }


    // 트랙 사이즈 조정하기
    public void decreaseTrackListSizeTo(int trackLimitSize){
        if(this.trackList != null) {
            this.trackList =
                    this.trackList.stream().limit(trackLimitSize).collect(Collectors.toList());
        }
    }

    // 트랙 순서 표기
    public void makeTrackDispSn(){

        Optional.ofNullable(
                this.trackList
        ).ifPresent(
                trackDtos -> {
                    AtomicInteger atomicInteger = new AtomicInteger();
                    trackDtos.forEach(
                            trackDto -> trackDto.setTrackSn(atomicInteger.incrementAndGet())
                    );
                }
        );
    }

    // 트랙 rank 제거
    public void disableRank(){
        Optional.ofNullable(
                this.trackList
        ).ifPresent(
                trackDtos -> trackDtos.forEach( TrackDto::disableRankIndicator)
        );
    }

    // 첫 트랙 아이디 제공
    public Optional<Long> getTrackIdOfFirstTrack(){
        return
            Optional.ofNullable(
                this.trackList
            ).orElseGet(Collections::emptyList)
                    .stream()
                    .map(TrackDto::getTrackId)
                    .findFirst();
    }
}
