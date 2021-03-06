/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.client.fallback;

import org.springframework.stereotype.Component;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.client.MemberClient;
import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterDto;
import com.sktechx.godmusic.personal.rest.model.vo.member.InnerMemberVo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.09.04
 */

@Component
@Slf4j
public class MemberClientFallbackFactory implements FallbackFactory<MemberClient>{
    @Override
    public MemberClient create(Throwable e) {
        return new MemberClient(){
            @Override
            public CommonApiResponse<InnerMemberVo> getMember(YnType includeSktPremiumYn,
                    YnType includeSktMember, YnType includeSktMemberShip) {
                return null;
            }
            @Override
            public CommonApiResponse<CharacterDto> getCharacter(Long characterNo) {
                log.error("member-api call error. message={}, trace={}", e.getMessage(), e.getStackTrace());
                return null;
            }

            @Override
            public CommonApiResponse<Void> ping() {
                log.warn("[WARM-UP] ... 멤버 Ping 호출 실패, message={}", e.getMessage());
                return null;
            }
        };
    }
}
