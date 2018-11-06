/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sktechx.godmusic.personal.common.domain.domain.GMUser;
import com.sktechx.godmusic.personal.common.domain.domain.GMUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 정덕진(Deockjin Chung)/Music사업팀/SKTECH(djin.chung@sk.com)
 * @date 2018.07.03
 */

@Slf4j
public class UserInfoInterceptor extends HandlerInterceptorAdapter implements Ordered {
    public final static String      XGmUserKey = "X-GM-User";
    private ObjectMapper objectMapper;
    private GMUser emptyGMUser = new EmptyGMUser();

    public UserInfoInterceptor(ObjectMapper objectMapper)      {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        Enumeration<String> userEnum = request.getHeaders(XGmUserKey);
        if( userEnum.hasMoreElements() )  {
            try {
                GMUserInfo userInfo = objectMapper.readValue(userEnum.nextElement(), GMUserInfo.class);
                if( userInfo != null ) {
                    log.debug("set userinfo : {}", userInfo);
                    request.setAttribute(GMUser.attributeKey, userInfo);
                    return super.preHandle(request, response, handler);
                }
            } catch(Exception ex)   {
                log.warn("fail to extract userInfo : {}", ex.getMessage());
            }
        }

        request.setAttribute(GMUser.attributeKey, emptyGMUser);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }

    /**
     * empty gm user
     */
    class EmptyGMUser   implements GMUser   {

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public Long getMemberNo() {
            return null;
        }

        @Override
        public String   toString()  {
            return "empty user";
        }
    }
}
