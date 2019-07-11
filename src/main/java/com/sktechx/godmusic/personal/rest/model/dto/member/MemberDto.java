/*
 *
 * Copyright (c) 2017 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 *
 */
package com.sktechx.godmusic.personal.rest.model.dto.member;

import java.util.Date;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.SexType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.mybatis.annotation.SecureDomain;
import com.sktechx.godmusic.lib.mybatis.annotation.SecureField;
import com.sktechx.godmusic.personal.common.domain.MemberType;
import lombok.*;

/**
 * 멤버 테이블(tb_memeber) dto
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2017. 6. 27.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@SecureDomain
@ToString(exclude = {"memberMdn", "memberId", "memberPwd", "memberName"})
public class MemberDto {
    public static final String NON_SKT_SVM_MANG_NO_VALUE = "NA";
    
    @JsonProperty("no")
    private Long memberNo;                // 회원번호
    private MemberType memberType;        // 회원타입 (IDM, MDN)
    private OsType osType;                // OS타입
    private String deviceToken;            // 단말토큰
    @JsonProperty("mdn")
    @SecureField
    private String memberMdn;            // 회원MDN
    private String serviceManagementNo;    // 회원서비스관리번호
    @SecureField
    private String memberId;            // 회원아이디(e-mail)
    @JsonIgnore
    private String memberPwd;            // 회원비밀번호
    private String memberOauthId;        // 회원OAUTH아이디
    @JsonProperty("name")
    @SecureField
    private String memberName;            // 회원성명

    @JsonProperty("nickname")
    @SecureField
    private String memberNickname;        // 회원닉네임
    @JsonProperty("birthYear")
    private String memberBirthyy;        // 회원생년
    private String profileImgFileName;    // 프로필이미지파일명
    private YnType adultAuthYn;            // 성인인증여부
    private Date adultAuthDate;            // 성인인증일시
    private String memberCi;            // 회원CI
    private String tlifeMemberType;        // T라이프회원타입
    private Long tlifeMemberNo;            // T라이프회원번호

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date lastLoginDateTime;            // 최종로그인일시

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date createDateTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date updateDateTime;

//    private EventChannelType eventChannelType;  //회원 가입 유입 채널

    private String memberBirth;  //회원 생년월일 19000101
    private SexType memberSex;

    @JsonIgnore
    private String smsAuthId;            // sms 인증 아이디
    @JsonIgnore
    private String smsAuthPwd;            // sms 인증 패스워드
//	@JsonIgnore
//	private List<ClauseAgreeDto> clauseAgreeList;

    @JsonIgnore
    private String sessionToken;

    // SignIn시에 사용하며.... 일반적으로 해당 값은 설정되지 않음.
//	@JsonIgnore
//	private UserLoginChannelType userLoginChannelType;
    public boolean isSktAuthMember() {
        return !StringUtils.isEmpty(this.serviceManagementNo) && !NON_SKT_SVM_MANG_NO_VALUE
		        .equals(this.serviceManagementNo);
    }
}
