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
package com.sktechx.godmusic.personal.common.exception;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.ErrorDomain;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
  * 설명 : 클라이언트 예외 ( 400 )
  *
  * @author 오경무/SKTECHX (km.oh@sk.com)
  * @date 2017. 7. 10.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ValidationException extends CommonBusinessException {
	private static final long serialVersionUID = 1L;

	public ValidationException(ErrorDomain errorMessageEnum) {
		super(errorMessageEnum);
	}
}
