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

import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.common.util.DateUtil;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;
import java.util.Enumeration;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 정덕진(Deockjin Chung)/Music사업팀/SKTECH(djin.chung@sk.com)
 * @date 2018.07.02
 */

public class TransactionIdInterceptor extends HandlerInterceptorAdapter implements Ordered {

    private String getNextTransactionId() {
        return "PER" + DateUtil.getCurrentDateTime() + CommonUtils.getRandomStr(3);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        MDC.put("hostName", InetAddress.getLocalHost().getHostName());

        Enumeration<String> idEnum = request.getHeaders(CommonConstant.X_GM_TRANSACTION_ID);
        if (idEnum.hasMoreElements()) {
            MDC.put("transactionId", idEnum.nextElement());
            return super.preHandle(request, response, handler);
        }

        String transactionId = getNextTransactionId();
        MDC.put("transactionId", transactionId);
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }

    /* (non-Javadoc)
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
