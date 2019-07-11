/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.vo.member;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.SexType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.MemberType;
import com.sktechx.godmusic.personal.rest.model.dto.member.AfloCharacterDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InnerMemberVo {

    private Long memberNo;

    private String joinChnlType;

    private MemberType memberType;

    private OsType osType;

    private String memberMdn;

    private String memberId;

    private String memberOauthId;

    private String memberNm;

    private String memberBirth;

    private SexType memberSex;

    private YnType foreignerYn;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date adultAuthDtime;

    private YnType adultAuthYn;

    private String memberCi;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date lastLoginDtime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDtime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDtime;

    private SktMemberVo sktMember;

    private YnType sktFeePremiumYn;

    private SktMemberShip sktMemberShip;

    private List<AfloCharacterDto> afloCharacterList;

}
