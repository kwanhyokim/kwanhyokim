package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.CreateOcrSessionRequest;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.CreateOcrSessionResponse;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrTrackListVo;
import com.sktechx.godmusic.personal.rest.service.OcrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/ocr")
@Api(value = "ocr", description = "ocr API - Chris")
public class OcrController {

    @Autowired
    private OcrService ocrService;


    @ApiOperation(value = "OCR 세션 정보 생성", httpMethod = "POST", notes = "OCR 분석할 파일을 올리기 위한, 세션 아이디를 생성 한다")
    @PostMapping("/session")
    public CommonApiResponse<CreateOcrSessionResponse> createOcrSession(@RequestBody CreateOcrSessionRequest request){

        String sessionId = ocrService.createOcrSession(request);
        return new CommonApiResponse<>(CreateOcrSessionResponse.builder().sessionId(sessionId).build());
    }

    @ApiOperation(value = "OCR 파일 업로드", httpMethod = "POST", notes = "플레이리스트 이미지 업로드 한다.")
    @PostMapping(value="/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonApiResponse uploadOcrFile(
                                            MultipartFile file,
                                           @RequestParam("sessionId") String sessionId,
                                           @RequestParam("index") Integer index ){
        // for test
        ocrService.uploadOcrFile(1000190L, file, sessionId, index);
        ocrService.uploadOcrFile(GMContext.getContext().getMemberNo(), file, sessionId, index);

        return new CommonApiResponse<>().emptySuccess();
    }

    @ApiOperation(value = "OCR 이미지 분석 결과 도출", httpMethod = "GET", notes = "OCR 이미지 분석된 정보를 가져가는 API")
    @GetMapping("/{sessionId}")
    public CommonApiResponse<OcrTrackListVo> getOcrFile(@PathVariable String sessionId){
        return new CommonApiResponse<>(ocrService.getOcrTrackList(sessionId));
    }

}
