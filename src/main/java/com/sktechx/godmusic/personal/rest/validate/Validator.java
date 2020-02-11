package com.sktechx.godmusic.personal.rest.validate;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.LoginValidationException;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ResourcePlayLogRequestParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time AM 11:50
 */
@Slf4j
public class Validator {
	public static void requestValidate(Long memberNo) {
		requestValidate(memberNo, null);
	}

	public static void loginValidate(GMContext context) {
		try {
			if(ObjectUtils.isEmpty(context.getMemberNo()) || ObjectUtils.isEmpty(context.getCharacterNo())) {
				log.info(context.toString());
				throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
			}
		} catch (NullPointerException e) {
			log.info("GMcontext is null");
			throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
		}
	}

	public static void requestValidate(Long memberNo, Long characterNo) {

		GMContext currentContext = GMContext.getContext();

        if(ObjectUtils.isEmpty(currentContext.getMemberNo())) throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
        if(!ObjectUtils.isEmpty(memberNo) && !memberNo.equals(currentContext.getMemberNo())) throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
        if(!ObjectUtils.isEmpty(characterNo) && !characterNo.equals(currentContext.getCharacterNo())) throw new LoginValidationException(PersonalErrorDomain.GM_CONTEXT_MEMBER_NO_NOT_EXIST);
	}

	public static void bulkResourcePlayLogRequestParamValidate(List<ResourcePlayLogRequestParam> logRequestParamList) {
		if (CollectionUtils.isEmpty(logRequestParamList)) {
			throw new CommonBusinessException(PersonalErrorDomain.BULK_LIST_SIZE_ZERO);
		}

		for (ResourcePlayLogRequestParam param : logRequestParamList) {
			if (null == param.getResourceId()
					|| null == param.getSourceType()
					|| null == param.getOsType()
					|| null == param.getQuality()
					|| null == param.getFreeYn()) {
				throw new CommonBusinessException(PersonalErrorDomain.MUST_NEED_VALUE);
			}
		}
	}
}
