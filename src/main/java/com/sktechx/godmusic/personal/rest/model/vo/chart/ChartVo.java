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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.ChartType;
import com.sktechx.godmusic.personal.common.domain.type.PlayListType;
import com.sktechx.godmusic.personal.rest.model.dto.ChartDto;
import com.sktechx.godmusic.personal.rest.model.dto.TasteMixDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.ChartTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 차트 vo
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-03-31
 */

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "id", "name", "imgList", "createDateTime", "updateDateTime",
        "renewDateTime", "likeYn", "renewYn", "totalCount", "basedOnUpdate", "trackList"})
public class ChartVo {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("name")
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

    @JsonProperty("tasteMix")
    private TasteMixDto tasteMixDto;

    @ApiModelProperty(hidden = true)
    private Date dispStartDtime;

    public String basedOnUpdate;

    public static ChartVo from (ChartDto chartDto, ChartTrackDto chartTrackDto){

        return
            (chartDto == null || chartTrackDto == null ?
                null
                :
                ChartVo.builder()
                        .id(chartTrackDto.getId())
                        .name(chartDto.getChartNm())
                        .playListType(chartTrackDto.getType())
                        .trackList(chartTrackDto.getTrackList())
                        .createDateTime(chartTrackDto.getCreateDateTime())
                        .updateDateTime(chartTrackDto.getUpdateDateTime())
                        .basedOnUpdate(chartTrackDto.getBasedOnUpdate())
                        .chartType(chartTrackDto.getChartType())
                        .renewYn(chartTrackDto.getRenewYn())
                        .totalCount(chartTrackDto.getTotalCount())
                        .imgList(chartDto.getImgList())
                        .tasteMixDto(
                                RCMMD_TASTE_MIX_VO_MAP.get(
                                    (chartTrackDto.getChartTaste() == null ? "NOT_MIX" :
                                            chartTrackDto.getChartTaste()
                                    )
                                )
                        )
                        .build()
                );
    }

    private static final Map<String, TasteMixDto> RCMMD_TASTE_MIX_VO_MAP;

    static {
        Map<String, TasteMixDto> rcmmdTasteMixVoMap = new HashMap<>();

        rcmmdTasteMixVoMap.put("NOT_MIX",
                TasteMixDto.builder()
                        .mixYn(YnType.N)
                        .status("NOT_MIXED")
                        .descriptionMessage("취향인 곡이 없어 일반 순위가 표시됩니다.")
                        .displayMessage("FLO 차트를 내 취향 순서로 변경했습니다.")
                        .build()
        );

        rcmmdTasteMixVoMap.put("PRIVATE",
                TasteMixDto.builder()
                        .mixYn(YnType.Y)
                        .status("MIXED")
                        .displayMessage("FLO 차트를 내 취향 순서로 변경했습니다.")
                        .build()
        );
        rcmmdTasteMixVoMap.put("CHART_TASTE",
                TasteMixDto.builder()
                        .mixYn(YnType.N)
                        .status("SAME")
                        .descriptionMessage("일반FLO 차트와 비슷한 취향이에요!")
                        .displayMessage("FLO 차트를 내 취향 순서로 변경했습니다.")
                        .build()
        );
        rcmmdTasteMixVoMap.put("NOT_ENOUGH_TASTE",
                TasteMixDto.builder()
                        .mixYn(YnType.N)
                        .status("REQUIRE_MORE_LISTEN")
                        .descriptionMessage("취향이 충분히 쌓일 때 까지 일반 순위가 표시됩니다.")
                        .displayMessage("FLO 차트를 내 취향 순서로 변경했습니다.")
                        .build()
        );

        RCMMD_TASTE_MIX_VO_MAP = Collections.unmodifiableMap(rcmmdTasteMixVoMap);
    }

}


