/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.vo.chart;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.*;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.PreferPropsType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PlayListType;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.model.dto.TasteMixDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartDispPropsDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.CHART_PANEL_HOURLY_BASIS_PHRASES;

/**
 * 설명 : 차트 vo
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-31
 */

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "id", "name", "tasteMix","imgList", "createDateTime", "updateDateTime",
        "renewDateTime", "likeYn", "renewYn", "totalCount", "basedOnUpdate", "trackList"})
public class ChartVo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
    private String displayName;

    @JsonIgnore
    private String name;

    private String playTime;
    private Integer totalCount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDateTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date registDateTime;

    private List<ImageInfo> imgList;

    private List<TrackDto> trackList;

    private ChartType chartType;

    @JsonProperty("type")
    private PlayListType playListType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date renewDateTime;

    private YnType renewYn;

    public boolean getRenew(){
        return YnType.Y == renewYn;
    }

    public void setImgList(List<ImageInfo> imgList) {

        if (imgList != null) {
            imgList.sort(null);
        }

        this.imgList = imgList;
    }

    public String getDisplayName(){
        return this.name + " 내 취향 MIX";
    }

    @JsonIgnore
    private TasteMixDto tasteMixDto;

    @ApiModelProperty(hidden = true)
    private Date dispStartDtime;

    public String basedOnUpdate;

    @JsonIgnore
    private String requestedMixYn;

    public TasteMixDto getTasteMix(){

        if(tasteMixDto == null){
            TasteMixDto currentTasteMixDto = RCMMD_TASTE_MIX_VO_MAP.get("OFF");

            return TasteMixDto.builder()
                    .mixYn(currentTasteMixDto.getMixYn())
                    .status(currentTasteMixDto.getStatus())
                    .displayMessage(
                            String.format(currentTasteMixDto.getDisplayMessage(), "FLO 차트"))
                    .build();

        }

        if("N".equals(requestedMixYn)){
            TasteMixDto currentTasteMixDto = RCMMD_TASTE_MIX_VO_MAP.get("OFF");

            return TasteMixDto.builder()
                    .mixYn(currentTasteMixDto.getMixYn())
                    .status(currentTasteMixDto.getStatus())
                    .displayMessage(
                            String.format(currentTasteMixDto.getDisplayMessage(), this.name))
                    .build();
        }

        return tasteMixDto;

    }

    public static ChartVo from (ChartDispPropsDto chartDispPropsDto, ChartTrackDto chartTrackDto){

        if(chartDispPropsDto == null || chartTrackDto == null){
            return null;
        }

        TasteMixDto currentTasteMixDto = RCMMD_TASTE_MIX_VO_MAP.get(
                (chartTrackDto.getChartTaste() == null ? "NOT_MIXED" :
                        chartTrackDto.getChartTaste()
                )
        );

        TasteMixDto tasteMixDto = TasteMixDto.builder()
                .mixYn(currentTasteMixDto.getMixYn())
                .status(currentTasteMixDto.getStatus())
                .displayMessage(currentTasteMixDto.getDisplayMessage())
                .descriptionMessage(currentTasteMixDto.getDescriptionMessage())
                .build();


        if("SAME".equals(tasteMixDto.getStatus())){
            tasteMixDto.setDescriptionMessage(
                    String.format(tasteMixDto.getDescriptionMessage(), chartDispPropsDto.getChartNm())
            );
        }

        tasteMixDto.setDisplayMessage(
                String.format(tasteMixDto.getDisplayMessage(), chartDispPropsDto.getChartNm())
        );


        return
                ChartVo.builder()
                        .id(chartTrackDto.getId())
                        .name(chartDispPropsDto.getChartNm())
                        .playListType(chartTrackDto.getType())
                        .trackList(chartTrackDto.getTrackList())
                        .createDateTime(chartTrackDto.getCreateDateTime())
                        .updateDateTime(chartTrackDto.getUpdateDateTime())
                        .basedOnUpdate(PreferPropsType.TOP100.getCode().equals(
                                chartDispPropsDto.getDispPropsType()
                                ) ?
                                DateUtil.dateToString(
                                        (chartTrackDto.getDispStartDtime() == null ?
                                                new Date() : chartTrackDto.getDispStartDtime()
                                        ), "HH") + CHART_PANEL_HOURLY_BASIS_PHRASES
                                        :
                                chartTrackDto.getBasedOnUpdate()
                        )
                        .chartType(chartTrackDto.getChartType())
                        .renewYn(chartTrackDto.getRenewYn())
                        .totalCount(chartTrackDto.getTotalCount())
                        .imgList(
                                chartDispPropsDto.getImgList().stream().map(
                                        chartImageInfo -> (ImageInfo)chartImageInfo).collect(
                                        Collectors.toList())
                        )
                        .tasteMixDto(tasteMixDto)
                        .build()
                ;
    }

    public static final Map<String, TasteMixDto> RCMMD_TASTE_MIX_VO_MAP;

    static {
        Map<String, TasteMixDto> rcmmdTasteMixVoMap = new ConcurrentHashMap<>();

        rcmmdTasteMixVoMap.put("NOT_MIXED",
                TasteMixDto.builder()
                        .mixYn(YnType.N)
                        .status("NOT_MIXED")
                        .descriptionMessage("취향이 충분히 쌓일 때 까지 일반 순위가 표시됩니다.")
                        .displayMessage("%s를 내 취향 순서로 변경했습니다.")
                        .build()
        );

        rcmmdTasteMixVoMap.put("MIXED",
                TasteMixDto.builder()
                        .mixYn(YnType.Y)
                        .status("MIXED")
                        .displayMessage("%s를 내 취향 순서로 변경했습니다.")
                        .build()
        );
        rcmmdTasteMixVoMap.put("SAME",
                TasteMixDto.builder()
                        .mixYn(YnType.Y)
                        .status("SAME")
                        .descriptionMessage("일반 %s와 비슷한 취향이에요!")
                        .displayMessage("%s를 내 취향 순서로 변경했습니다.")
                        .build()
        );
        rcmmdTasteMixVoMap.put("REQUIRE_MORE_LISTEN",
                TasteMixDto.builder()
                        .mixYn(YnType.N)
                        .status("REQUIRE_MORE_LISTEN")
                        .descriptionMessage("취향인 곡이 없어 일반 순위가 표시됩니다.")
                        .displayMessage("%s를 내 취향 순서로 변경했습니다.")
                        .build()
        );
        rcmmdTasteMixVoMap.put("OFF",
                TasteMixDto.builder()
                        .mixYn(YnType.N)
                        .status("OFF")
                        .displayMessage("인기 순서의 일반 %s로 변경했습니다.")
                        .build()
        );

        rcmmdTasteMixVoMap.put("REQUIRE_LOGIN",
                TasteMixDto.builder()
                        .mixYn(YnType.N)
                        .status("REQUIRE_LOGIN")
                        .displayMessage("인기 순서의 일반 %s로 변경했습니다.")
                        .build()
        );


        RCMMD_TASTE_MIX_VO_MAP = Collections.unmodifiableMap(rcmmdTasteMixVoMap);
    }

}


