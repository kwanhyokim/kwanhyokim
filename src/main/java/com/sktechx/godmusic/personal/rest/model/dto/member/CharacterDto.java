/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.member;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.code.YnType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.11.02
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CharacterDto {

    @ApiModelProperty(required = true, example = "1111", value = "회원 번호")
    private Long memberNo;
    @ApiModelProperty( example = "1", value = "캐릭터 번호")
    private Long characterNo;
    @ApiModelProperty(example = "홍길동", value = "캐릭터 이름")
    private String characterNm;
    @ApiModelProperty(example = "http://test.com/test.jpg", value = "캐릭터 이미지 url")
    private String characterImgUrl;
    @ApiModelProperty(required = true, example = "Y", value = "대표 캐릭터 여부")
    private YnType reprsntCharacterYn;
    @ApiModelProperty(example = "DEFAULT", value = "캐릭터 타입")
    private CharacterType characterType ;
    @ApiModelProperty(example = "2019-11-22 23:23:59", value = "캐릭터 생성일")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDtime;

    private List<PreferGenreDto> preferGenreList;
    private List<PreferArtistDto> preferArtistList;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String accessToken;
}
