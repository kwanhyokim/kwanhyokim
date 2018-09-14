package com.sktechx.godmusic.personal.rest.validate;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.domain.exception.LoginValidationException;
import org.springframework.util.ObjectUtils;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time AM 11:50
 */
public class Validator {
	public static void requestValidate(Long memberNo) {
		requestValidate(memberNo, null);
	}

	public static void loginValidate(GMContext context) {
		try {
			if(ObjectUtils.isEmpty(context.getMemberNo()) || ObjectUtils.isEmpty(context.getCharacterNo())) throw new LoginValidationException(CommonErrorDomain.UNAUTHORIZED);
		} catch (NullPointerException e) {
			throw new LoginValidationException(CommonErrorDomain.UNAUTHORIZED);
		}
	}

	public static void requestValidate(Long memberNo, Long characterNo) {

		GMContext currentContext = GMContext.getContext();

        if(ObjectUtils.isEmpty(currentContext.getMemberNo())) throw new LoginValidationException(CommonErrorDomain.UNAUTHORIZED);
        if(!ObjectUtils.isEmpty(memberNo) && !memberNo.equals(currentContext.getMemberNo())) throw new LoginValidationException(CommonErrorDomain.UNAUTHORIZED);
        if(!ObjectUtils.isEmpty(characterNo) && !characterNo.equals(currentContext.getCharacterNo())) throw new LoginValidationException(CommonErrorDomain.UNAUTHORIZED);
	}
}
