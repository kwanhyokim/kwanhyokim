package com.sktechx.godmusic.personal.rest.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${name}")
    private String name;

    @ApiOperation(value = "메세지 테스트", httpMethod = "POST", notes = "메세지 테스트 입니다.")
    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public String hello(
            @ApiParam(name = "messageType", value = "메세지유형(PUSH:푸시, SMS:단문자메세지, LMS:장문자메세지, MMS:멀티미디어메세지, EMAIL:이메일)", defaultValue = "SMS", required = true) @RequestParam String messageType,
            @ApiParam(name = "message", value = "메세지", defaultValue = "메세지~", required = true) @RequestParam String message) {

        return "hello personal ~ " + name + ", " + messageType + ":" + message;
    }

    @ApiOperation(value = "메세지 테스트", httpMethod = "GET", notes = "메세지 테스트 입니다.")
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {

        return "hello personal ~";
    }

}
