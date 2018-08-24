package com.sktechx.godmusic.personal.common.exception;

import com.sktechx.godmusic.lib.domain.exception.ErrorDomain;
import org.springframework.http.HttpStatus;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time PM 1:49
 */
public enum CommonErrorMessage implements ErrorDomain {

	/*
	 * 200 Content Empty
	 */
	// Common 	0000
	EMPTY_DATA(2040001,HttpStatus.OK,"요청 결과 정보가 없습니다.", "요청 결과 정보 없음")
	/*
	 * 400 Default Error
	 */
	// Common 	0000
	, BAD_REQUEST(4000001,HttpStatus.BAD_REQUEST,"유효하지 않은 파라미터 입니다.", "잘못된 파라미터")

	, TRACK_DUPLICATED_LIKE(4000301, HttpStatus.BAD_REQUEST,"이미 추가된 곡 입니다.", "이미 추가된 곡")
	, TRACK_OVER_ADD_LIKE(4000302, HttpStatus.BAD_REQUEST,"좋아요 한 곡을 더 이상 추가할 수 없습니다. 최대 1000곡까지 담을 수 있습니다.","좋아하는 곡 추가 최대값 초과")
	, CHANNEL_DUPLICATED_LIKE(4000303, HttpStatus.BAD_REQUEST, "이미 추가된 채널 입니다.","중복된 채널")
	, CHANNEL_OVER_ADD_LIKE(4000304,HttpStatus.BAD_REQUEST,"좋아요 한 플레이리스트를 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다.","좋아하는 채널 최대값 초과")
	, ARTIST_DUPLICATED_LIKE(4000305, HttpStatus.BAD_REQUEST, "이미 추가된 아티스트 입니다." ,"이미 추가된 아티스트")
	, ARTIST_OVER_ADD_LIKE(4000306, HttpStatus.BAD_REQUEST, "좋아요 한 아티스트를 더 이상 추가 할 수 없습니다. 최대 1000명까지 담을 수 있습니다." ,"좋아하는 아티스트 최대값 초과")
	, ALBUM_DUPLICATED_LIKE(4000307, HttpStatus.BAD_REQUEST, "이미 추가된 앨범 입니다." ,"이미 추가된 앨범")
	, ALBUM_OVER_ADD_LIKE(4000308, HttpStatus.BAD_REQUEST, "좋아요 한 앨범을 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다." ,"좋아하는 앨범 최대값 초과")
	, CHART_DUPLICATED_LIKE(4000309, HttpStatus.BAD_REQUEST, "이미 추가된 차트 입니다." ,"이미 추가된 차트")
	, CHART_OVER_ADD_LIKE(4000310, HttpStatus.BAD_REQUEST, "좋아요 한 차트을 더 이상 추가할 수 없습니다. 최대 1000개까지 담을 수 있습니다." ,"좋아하는 차트 최대값 초과")
	, MY_CHANNEL_OVER_CREATE(4000311, HttpStatus.BAD_REQUEST,"my플레이리스트를 더 이상 추가할 수 없습니다.\n최대 1000개의 my플레이리스트를 만들 수 있습니다.","My 플레이 리스트 생성 최대값 초과")
	, MY_CHANNEL_TRACK_OVER_ADD(4000312, HttpStatus.BAD_REQUEST, "my플레이리스트에는 최대 1000곡까지 담을 수 있습니다.\n새로운 플레이리스트를 만들어 주세요.","My 플레이 리스트 곡 추가 최대값 초과")
	, MY_CHANNEL_DUPLICATED_NAME(4000313, HttpStatus.BAD_REQUEST, "동일한 my플레이리스트 명이 이미 존재합니다" ,"채널명 중복")
	, MY_CHANNEL_NOT_FOUND(4040314, HttpStatus.BAD_REQUEST, "요청하신 채널이 존재하지 않습니다.", "요청 채널 에러")
	/*
	 * 401 Default Error
	 */
	// Common 	0000
	,UNAUTHORIZED(4010001, HttpStatus.UNAUTHORIZED, "인증에 실패 하였습니다." , "인증 실패")
	,HMAC_AUTHORIZATION_FAIL(4010002, HttpStatus.UNAUTHORIZED, "Signature 불일치, Musicmate API 이용불가" ,"Hmac 인증 실패")

	// My 		0300
	, CHANNEL_NOT_FOUND(4040301, HttpStatus.BAD_REQUEST, "존재 하지 않은 채널 정보 입니다.", "채널 정보 없음")
	, CHART_NOT_FOUND(4040302, HttpStatus.BAD_REQUEST, "존재 하지 않은 차트 정보 입니다.", "차트 정보 없음")
	, ALBUM_NOT_FOUND(4040303,HttpStatus.NOT_FOUND ,"존재 하지 않은 앨범 정보 입니다.","앨범 정보 없음"  )
	, ARTIST_NOT_FOUND(4040304,HttpStatus.NOT_FOUND ,"존재 하지 않은 아티스트 정보 입니다.","아티스트 정보 없음"  )
	, TRACK_NOT_FOUND(4040305,HttpStatus.NOT_FOUND ,"존재 하지 않은 아티스트 정보 입니다.","아티스트 정보 없음"  )

	, USER_PSSRL_NOT_FOUND(4040402, HttpStatus.NOT_FOUND, "회원의 PSSRL이 존재하지 않아 정산 요청을 할 수 없습니다.", "회원 PSSRL 코드 없음")

	// Common 	0000
	, INTERNAL_SERVER_ERROR(5000001,HttpStatus.INTERNAL_SERVER_ERROR,"일시적으로 접속이 원활하지 않습니다.\n잠시 후 다시 이용해\n 주시기 바랍니다.","알수 없는 시스템 에러")
	, DATABASE_ERROR(5000002, HttpStatus.INTERNAL_SERVER_ERROR, "DB장애" ,"DB장애")
	, REGULAR_CHECK(5000003, HttpStatus.INTERNAL_SERVER_ERROR, "서비스 점검 중입니다.\n이용에 불편을 드려 죄송합니다." ,"서비스 점검 중")
	;

	private int code;
	private HttpStatus httpStatus;
	private String message;
	private String logMessage;
	private Object data;

	private CommonErrorMessage(int code, HttpStatus httpStatus,String message, String logMessage) {
		this.code = code;
		this.httpStatus = httpStatus;
		this.message = message;
		this.logMessage = logMessage;
	}

	private CommonErrorMessage(int code, HttpStatus httpStatus,String message, String logMessage, Object data) {
		this.code = code;
		this.httpStatus = httpStatus;
		this.message = message;
		this.logMessage = logMessage;
		this.data = data;
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

	public Object getData() {
		return this.data;
	}
}
