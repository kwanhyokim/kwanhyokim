/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.client.fallback.MemberClientFallbackFactory;
import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterDto;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.09.11
 */

@FeignClient(value = "member-api", fallbackFactory = MemberClientFallbackFactory.class)
public interface MemberClient {
    @GetMapping("/member/v1/members/characters/{characterNo}")
    CommonApiResponse<CharacterDto> getCharacter(@PathVariable("characterNo") Long characterNo);
}
