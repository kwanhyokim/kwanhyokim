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

import com.sktechx.godmusic.lib.domain.exception.ErrorDomain;
import org.springframework.http.HttpStatus;

public enum PersonalErrorDomain implements ErrorDomain {

    TRACK_DUPLICATED_LIKE(4090701,HttpStatus.CONFLICT,"이미 추가된 곡 입니다.", "이미 추가된 곡")
	, TRACK_OVER_ADD_LIKE(4090702, HttpStatus.CONFLICT,"좋아요 한 곡을 더 이상 추가할 수 없습니다. 최대 1000곡까지 담을 수 있습니다.","좋아하는 곡 추가 최대값 초과")
	, CHANNEL_DUPLICATED_LIKE(4090703, HttpStatus.CONFLICT, "이미 추가된 채널 입니다.","중복된 채널")
	, CHANNEL_OVER_ADD_LIKE(4090704,HttpStatus.CONFLICT,"좋아요 한 플레이리스트를 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다.","좋아하는 채널 최대값 초과")
	, ARTIST_DUPLICATED_LIKE(4090705, HttpStatus.CONFLICT, "이미 추가된 아티스트 입니다." ,"이미 추가된 아티스트")
	, ARTIST_OVER_ADD_LIKE(4090706, HttpStatus.CONFLICT, "좋아요 한 아티스트를 더 이상 추가 할 수 없습니다. 최대 1000명까지 담을 수 있습니다." ,"좋아하는 아티스트 최대값 초과")
	, ALBUM_DUPLICATED_LIKE(4090707, HttpStatus.CONFLICT, "이미 추가된 앨범 입니다." ,"이미 추가된 앨범")
	, ALBUM_OVER_ADD_LIKE(4090708, HttpStatus.CONFLICT, "좋아요 한 앨범을 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다." ,"좋아하는 앨범 최대값 초과")
	, CHART_DUPLICATED_LIKE(4090709, HttpStatus.CONFLICT, "이미 추가된 차트 입니다." ,"이미 추가된 차트")
	, CHART_OVER_ADD_LIKE(4090710, HttpStatus.CONFLICT, "좋아요 한 차트을 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다." ,"좋아하는 차트 최대값 초과")
	, MY_CHANNEL_OVER_CREATE(4090711, HttpStatus.CONFLICT,"my플레이리스트를 더 이상 추가할 수 없습니다.\n최대 1000개의 my플레이리스트를 만들 수 있습니다.","My 플레이 리스트 생성 최대값 초과")
	, MY_CHANNEL_TRACK_OVER_ADD(4090712, HttpStatus.CONFLICT, "my플레이리스트에는 최대 1000곡까지 담을 수 있습니다.\n새로운 플레이리스트를 만들어 주세요.","My 플레이 리스트 곡 추가 최대값 초과")
	, MY_CHANNEL_DUPLICATED_NAME(4090713, HttpStatus.CONFLICT, "동일한 my플레이리스트 명이 이미 존재합니다" ,"채널명 중복")
//	, MY_CHANNEL_NOT_FOUND(4090701, HttpStatus.BAD_REQUEST, "요청하신 채널이 존재하지 않습니다.", "요청 채널 에러")

	, USER_PSSRL_NOT_FOUND(4090701, HttpStatus.CONFLICT, "회원의 PSSRL이 존재하지 않아 정산 요청을 할 수 없습니다.", "회원 PSSRL 코드 없음")
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
