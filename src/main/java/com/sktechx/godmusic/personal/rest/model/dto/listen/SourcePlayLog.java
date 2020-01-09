/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.dto.listen;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.SourceType;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 곡, 비디오 청취 로그 MQ로 전달하기 위한 클래스
 *
 * @author groot
 * @since 2019. 12. 19
 */
@Data
@Builder
public class SourcePlayLog {
    @JsonProperty("play_chnl")
    private String playChnl;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("time_millis")
    private long timeMillis;

    @JsonProperty("member_no")
    private Long memberNo;

    @JsonProperty("character_no")
    private Long characterNo;

    @JsonProperty("source_type")
    private SourceType sourceType;

    @JsonProperty("track_id")
    private Long trackId;

    @JsonProperty("source_id")
    private String sourceId;

    @JsonProperty("log_type")
    private String logType;

    @JsonProperty("bitrate")
    private String bitrate;

    @JsonProperty("quality")
    private String quality;

    @JsonProperty("track_tot_tm")
    private Long trackTotTm;

    @JsonProperty("elapsed_tm")
    private Long elapsedTm;

    @JsonProperty("os_type")
    private String osType;

    @JsonProperty("goods_id")
    private Long goodsId;

    @JsonProperty("prchs_id")
    private Long prchsId;

    @JsonProperty("dvc_id")
    private String dvcId;

    @JsonProperty("album_id")
    private Long albumId;

    @JsonProperty("chnl_id")
    private Long chnlId;

    @JsonProperty("chnl_type")
    private String chnlType;

    @JsonProperty("member_rcmd_id")
    private Long memberRcmdId;

    @JsonProperty("add_tm")
    private String addTm;

    @JsonProperty("free")
    @Builder.Default
    private Boolean free = false;

    @JsonProperty("pssrl_cd")
    private String pssrlCd;

    @JsonProperty("session_token")
    private String sessionToken;

    @JsonProperty("user_client_ip")
    private String userClientIp;

    @JsonProperty("service_id")
    private String serviceId;

    @JsonProperty("drm_member_no")
    private Long drmMemberNo;

    @JsonProperty("drm_prchs_id")
    private Long drmPrchsId;

    @JsonProperty("drm_goods_id")
    private Long drmGoodsId;

    @JsonProperty("drm_pssrl_cd")
    private String drmPssrlCd;

    @JsonProperty("drm_service_id")
    private String drmServiceId;

    @JsonProperty("meta_cache_update_dtime")
    private String metaCacheUpdateDtime;

    @JsonProperty("offline_start_dtime")
    private String offlineStartDtime;

    @JsonProperty("play_offline_yn")
    private YnType playOfflineYn;

    @JsonProperty("play_cache_yn")
    private YnType playCacheYn;
}
