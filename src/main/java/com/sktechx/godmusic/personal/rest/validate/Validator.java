package com.sktechx.godmusic.personal.rest.validate;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.domain.exception.LoginValidationException;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
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
			if(ObjectUtils.isEmpty(context.getMemberNo()) || ObjectUtils.isEmpty(context.getCharacterNo())) throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
		} catch (NullPointerException e) {
			throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
		}
	}

	public static void requestValidate(Long memberNo, Long characterNo) {

		GMContext currentContext = GMContext.getContext();

        if(ObjectUtils.isEmpty(currentContext.getMemberNo())) throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
        if(!ObjectUtils.isEmpty(memberNo) && !memberNo.equals(currentContext.getMemberNo())) throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
        if(!ObjectUtils.isEmpty(characterNo) && !characterNo.equals(currentContext.getCharacterNo())) throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
	}
}
