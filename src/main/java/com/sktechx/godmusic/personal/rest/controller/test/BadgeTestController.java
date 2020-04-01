package com.sktechx.godmusic.personal.rest.controller.test;

import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeDto;
import com.sktechx.godmusic.personal.rest.repository.BadgeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@ApiIgnore
@Slf4j
@Profile("!prod")
@RequiredArgsConstructor
@Controller
@RequestMapping(Naming.serviceCode + "/test/badge")
public class BadgeTestController {

    private final BadgeMapper badgeMapper;

    @GetMapping("/")
    public String testBadge(Model model) {
        List<BadgeDto> badgeList = badgeMapper.findAll();
        model.addAttribute("badgeList", badgeList);
        return "testBadge";
    }

}
