package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.rest.model.dto.member.MemberDvcDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("member-api")
public interface MemberApiProxy {

    @GetMapping("/member/v1/inner/members/member-dvc")
    CommonApiResponse<MemberDvcDto> getMemberDvc(@RequestParam(value = "memberNo") Long memberNo, @RequestParam(value = "deviceId") String deviceId);

}

