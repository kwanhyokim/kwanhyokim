package com.sktechx.godmusic.personal.common.exception;

import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.ErrorDomain;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time PM 4:01
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends CommonBusinessException {
	private static final long serialVersionUID = 1L;

	public NotFoundException(ErrorDomain errorMessageEnum) {
		super(errorMessageEnum);
	}
}
