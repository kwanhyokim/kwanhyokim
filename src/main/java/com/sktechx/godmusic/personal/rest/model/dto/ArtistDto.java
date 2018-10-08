/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.ArtistGroupType;
import com.sktechx.godmusic.personal.common.domain.type.GenderType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 설명 : 아티스트 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(value = {"id","name"}, alphabetic = true)
public class ArtistDto {
    @JsonProperty("id")
    private Long artistId;
    @JsonProperty("name")
    private String artistName;

    private GenderType genderCd;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private ArtistGroupType artistGroupType;

    private String artistGroupTypeStr;

    private String actStartYmd;

    private String artistStyle;

    private YnType likeYn;

    public String getArtistGroupTypeStr(){
        if(!ObjectUtils.isEmpty(this.artistGroupType)){
            return this.artistGroupType.getValue();
        }
        return null;
    }

    public String getGenderCdStr(){
        if(!ObjectUtils.isEmpty(this.genderCd)){
            return this.genderCd.getValue();
        }
        return null;
    }

    List<ImageInfo> imgList;

    private String artistType;
}
