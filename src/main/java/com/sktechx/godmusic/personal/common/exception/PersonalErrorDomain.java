/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.common.exception;

import org.springframework.http.HttpStatus;

import com.sktechx.godmusic.lib.domain.exception.ErrorDomain;

public enum PersonalErrorDomain implements ErrorDomain {
	GM_CONTEXT_MEMBER_NO_NOT_EXIST(4030701, HttpStatus.FORBIDDEN ,"회원 정보 없음", "유효하지 않은 API 접근입니다.")

    , TRACK_DUPLICATED_LIKE(4090702, HttpStatus.CONFLICT, "이미 추가된 곡 입니다.", "이미 추가된 곡")
	, TRACK_OVER_ADD_LIKE(4090703, HttpStatus.CONFLICT,"좋아요 한 곡을 더 이상 추가할 수 없습니다. 최대 1000곡까지 담을 수 있습니다.","좋아하는 곡 추가 최대값 초과")
	, CHANNEL_DUPLICATED_LIKE(4090704, HttpStatus.CONFLICT, "이미 추가된 테마리스트 입니다.","중복된 채널")
	, CHANNEL_OVER_ADD_LIKE(4090705,HttpStatus.CONFLICT,"좋아요 한 테마리스트를 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다.","좋아하는 채널 최대값 초과")
	, ARTIST_DUPLICATED_LIKE(4090706, HttpStatus.CONFLICT, "이미 추가된 아티스트 입니다." ,"이미 추가된 아티스트")
	, ARTIST_OVER_ADD_LIKE(4090707, HttpStatus.CONFLICT, "좋아요 한 아티스트를 더 이상 추가 할 수 없습니다. 최대 1000명까지 담을 수 있습니다." ,"좋아하는 아티스트 최대값 초과")
	, ALBUM_DUPLICATED_LIKE(4090708, HttpStatus.CONFLICT, "이미 추가된 앨범 입니다." ,"이미 추가된 앨범")
	, ALBUM_OVER_ADD_LIKE(4090709, HttpStatus.CONFLICT, "좋아요 한 앨범을 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다." ,"좋아하는 앨범 최대값 초과")
	, CHART_DUPLICATED_LIKE(4090710, HttpStatus.CONFLICT, "이미 추가된 테마리스트 입니다." ,"이미 추가된 차트")
	, CHART_OVER_ADD_LIKE(4090711, HttpStatus.CONFLICT, "좋아요 한 테마리스트를 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다." ,"좋아하는 차트 최대값 초과")
	, MY_CHANNEL_OVER_CREATE(4090712, HttpStatus.CONFLICT,"내 리스트를 더 이상 추가할 수 없습니다.\n최대 1000개의 내 리스트를 만들 수 있습니다.","My 플레이 리스트 생성 최대값 초과")
	, MY_CHANNEL_TRACK_OVER_ADD(4090713, HttpStatus.CONFLICT, "내 리스트에는 최대 1000곡까지 담을 수 있습니다.\n새로운 플레이리스트를 만들어 주세요.","My 플레이 리스트 곡 추가 최대값 초과")
	, MY_CHANNEL_DUPLICATED_NAME(4090714, HttpStatus.CONFLICT, "동일한 내 리스트 명이 이미 존재합니다" ,"채널명 중복")
	, CHANNEL_NOT_FOUND(4090715, HttpStatus.BAD_REQUEST, "존재 하지 않은 채널 정보 입니다.", "채널 정보 없음")
	, CHART_NOT_FOUND(4090716, HttpStatus.BAD_REQUEST, "존재 하지 않은 차트 정보 입니다.", "차트 정보 없음")
	, ALBUM_NOT_FOUND(4090717,HttpStatus.BAD_REQUEST ,"존재 하지 않은 앨범 정보 입니다.","앨범 정보 없음"  )
	, ARTIST_NOT_FOUND(4090718,HttpStatus.BAD_REQUEST ,"존재 하지 않은 아티스트 정보 입니다.","아티스트 정보 없음"  )
	, TRACK_NOT_FOUND(4090719,HttpStatus.BAD_REQUEST ,"권리가 중단되어 서비스가 불가능한 곡입니다.","곡 정보 없음"  )
    , VIDEO_OVER_ADD_LIKE(4090720, HttpStatus.CONFLICT,"좋아요 한 영상을 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다.","좋아하는 영상 추가 최대값 초과")
    , VIDEO_NOT_FOUND(4090721,HttpStatus.BAD_REQUEST ,"권리가 중단되어 서비스가 불가능한 영상입니다.","영상 정보 없음"  )

	, PREFER_ARTIST_PANEL_FAIL(4090720,HttpStatus.CONFLICT ,"선호/유사 아티스트 인기곡 입력시 문제가 발생했습니다.","데이터 문제"  )
	, PREFER_GENRE_PANEL_FAIL(4090721,HttpStatus.CONFLICT ,"선호 장르 유사곡 입력시 문제가 발생했습니다.","데이터 문제"  )

	, USER_PSSRL_NOT_FOUND(4090701, HttpStatus.CONFLICT, "회원의 PSSRL이 존재하지 않아 정산 요청을 할 수 없습니다.", "회원 PSSRL 코드 없음")
	, OWNER_TOKEN_INVALID(4090725, HttpStatus.CONFLICT, "OWNER TOKEN이 유효하지 않습니다.", "OWNER TOKEN 유효하지 않음")

    , NOT_FOUND_OCR_FILE(4090722, HttpStatus.CONFLICT, "OCR 파일 정보가 없습니다.", "OCR 파일 정보가 없습니다.")
    , ALREADY_UPLOAD_OCR_FILE(4090723, HttpStatus.CONFLICT, "이미 업로드 처리된 OCR파일 입니다", "이미 업로드 처리된 OCR파일 입니다")
    , FAIL_UPLOAD_OCR_FILE(4090724, HttpStatus.CONFLICT, "OCR파일 업로드 실패", "OCR파일 업로드 실패"), OUT_OF_OCR_SERVICE(4090725, HttpStatus.CONFLICT, "서버 점검으로 인해 일시적으로 해당 기능을 사용할 수 없습니다.", "Ocr not working")
    ;

    private int code;
    private HttpStatus httpStatus;
    private String message;
    private String logMessage;

    private PersonalErrorDomain(int code, HttpStatus httpStatus,String message, String logMessage) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.message = message;
        this.logMessage = logMessage;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
    @Override
    public String getLogMessage() {
        return this.logMessage;
    }

    @Override
    public String getDisplayMessage() {
        return this.message;
    }

}
