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
import lombok.*;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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
@EqualsAndHashCode
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

    /**
     * 아티스트 이미지가 존재하지 않아서 디폴트 이미즈를 사용했는지 여부를 리턴한다
     * https://cdn.music-flo.com/image/artist/000/000/00/00/000000000/000000000.jpg
     * @return
     */
    public boolean  hasDefaultImage()    {
        if( this.imgList == null || this.imgList.size() == 0 )  {
            return true;
        }

        return this.imgList.get(0).getUrl().contains("000000000") ? true : false;
    }
}
