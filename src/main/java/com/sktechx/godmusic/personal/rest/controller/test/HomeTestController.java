package com.sktechx.godmusic.personal.rest.controller.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.member.CharacterType;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendV2DummyDataRequest;
import com.sktechx.godmusic.personal.rest.service.DevToolService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendDataService;
import lombok.extern.slf4j.Slf4j;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 홈 테스트를 위환 컨트롤러
 *
 * @author 김관효
 * @date 2019. 7. 5.
 */
@Profile({"!prod"})
@Controller
@Slf4j
@RequestMapping(Naming.serviceCode + "/test")
@ApiIgnore
public class HomeTestController {


    @Autowired
    RedisService redisService;

    @Autowired
    RecommendDataService recommendDataService;

    @Autowired
    DevToolService devToolService;

    @GetMapping(value = "/v2home")
    public ModelAndView testV2Home() {
        return new ModelAndView("testV2Home");
    }

    @GetMapping(value = "/clearHomeCache")
    @ResponseBody
    public String clearHomeCache(@RequestParam Long characterNo){
        log.info("clearHomeCache :" + characterNo);

        return String.valueOf(redisService.delWithPrefix("godmusic.personalapi.recommend.phase:" + characterNo));
    }

    @GetMapping(value = "/createRecommendPanel")
    @ResponseBody
    public String createRecommendPanel(@RequestParam String type, @RequestParam Long characterNo){
        log.info("createRecommendPanel :" + characterNo);
        RecommendV2DummyDataRequest recommendV2DummyDataRequest = new RecommendV2DummyDataRequest();

        recommendV2DummyDataRequest.setPanelCount(5);
        recommendV2DummyDataRequest.setType(type);

        recommendDataService.createRecommendV2DummyData(characterNo,recommendV2DummyDataRequest);

        return String.valueOf(redisService.delWithPrefix("godmusic.personalapi.recommend.phase:" + characterNo));
    }

    @GetMapping(value = "/updateRecommendPanel")
    @ResponseBody
    public String updateRecommendPanel(@RequestParam String type, @RequestParam Long characterNo){
        log.info("updateRecommendPanel :" + characterNo);
        RecommendV2DummyDataRequest recommendV2DummyDataRequest = new RecommendV2DummyDataRequest();

        recommendV2DummyDataRequest.setType(type);

        recommendDataService.updateRecommendV2DummyData(characterNo,recommendV2DummyDataRequest);

        return String.valueOf(redisService.delWithPrefix("godmusic.personalapi.recommend.phase:" + characterNo));
    }


    @GetMapping(value = "/deleteRecommendPanel")
    @ResponseBody
    public String deleteRecommendPanel(@RequestParam String type, @RequestParam Long characterNo){
        log.info("deleteRecommendPanel :" + characterNo);
        RecommendV2DummyDataRequest recommendV2DummyDataRequest = new RecommendV2DummyDataRequest();

        recommendV2DummyDataRequest.setType(type);

        recommendDataService.deleteRecommendV2DummyData(characterNo,recommendV2DummyDataRequest);

        return String.valueOf(redisService.delWithPrefix("godmusic.personalapi.recommend.phase:" + characterNo));
    }

    @GetMapping(value = "/changeCharacterType")
    @ResponseBody
    public String changeCharacterType(@RequestParam CharacterType type, @RequestParam Long characterNo){
        log.info("changeCharacterType :" + characterNo);
        devToolService.updateCharacterType(characterNo, type);

        return String.valueOf(redisService.delWithPrefix("godmusic.personalapi.recommend.phase:" + characterNo));
    }


    @GetMapping(value = "/updateAfloChnl")
    @ResponseBody
    public String updateAfloChnl(@RequestParam Long characterNo){
        log.info("updateAfloChnl :" );

        recommendDataService.updateAfloChnl();

        return String.valueOf(redisService.delWithPrefix("godmusic.personalapi.recommend.phase:" + characterNo));
    }


}

