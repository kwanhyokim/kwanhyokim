package com.sktechx.godmusic.personal.rest.controller.v1;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrDto;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.CreateOcrSessionRequest;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.CreateOcrSessionResponse;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrAnalsVo;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.GetOcrStatusResponse;
import com.sktechx.godmusic.personal.rest.service.OcrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
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
    @PostMapping("")
    public CommonApiResponse<CreateOcrSessionResponse> createOcrSession(
            @ApiParam(value = "디바이스 아이디", required = true, defaultValue = "test_device") @RequestHeader(value = CommonConstant.X_GM_DEVICE_ID) OsType deviceId,
            @RequestBody CreateOcrSessionRequest request){

        //OcrDto ocrDto = ocrService.createOcr(2100981L, 2101151L, "test_device", request.getTotalFileCnt());
        OcrDto ocrDto = ocrService.createOcr(GMContext.getContext().getMemberNo(), GMContext.getContext().getCharacterNo(), GMContext.getContext().getDeviceId(), request.getTotalFileCnt());
        return new CommonApiResponse<>(CreateOcrSessionResponse.builder().ocrNo(ObjectUtils.isEmpty(ocrDto) ? null : ocrDto.getOcrNo() ).build());
    }

    @ApiOperation(value = "OCR 파일 업로드", httpMethod = "POST", notes = "플레이리스트 이미지 업로드 한다.")
    @PostMapping(value="/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonApiResponse uploadOcrFile( MultipartFile file, @RequestParam("ocrNo") Long ocrNo, @RequestParam("ocrFileNo") Integer ocrFileNo ){

        // select 로 검색 하여 기 처리된 데이터 인지 확인 upload 여부, 분석 요청 여부.
        // for test
        //AwsFileVo awsFileVo = ocrService.uploadOcrFile(1000190L, file, ocrNo, ocrFileNo);
        AwsFileVo awsFileVo = ocrService.uploadOcrFile(GMContext.getContext().getMemberNo(), file, ocrNo, ocrFileNo);
        ocrService.requestAnalysisToOcrServer(ocrNo, ocrFileNo, awsFileVo);

        return new CommonApiResponse<>().emptySuccess();
    }

    @ApiOperation(value = "OCR 이미지 분석 결과 도출", httpMethod = "GET", notes = "OCR 이미지 분석된 정보를 가져가는 API")
    @GetMapping("/{ocrNo}")
    public CommonApiResponse<OcrAnalsVo> getOcrResult(@PathVariable Long ocrNo){

        OcrAnalsVo ocrAnalsVo = ocrService.getOcrAnals(GMContext.getContext().getCharacterNo(), ocrNo);

        return new CommonApiResponse<>(ocrAnalsVo);
    }

    @ApiOperation(value = "OCR 이미지 분석 상태", httpMethod = "GET", notes = "OCR 이미지 분석된 정보를 가져가는 API")
    @GetMapping("/{ocrNo}/status")
    public CommonApiResponse<GetOcrStatusResponse> getOcrStatus(@PathVariable Long ocrNo){

        return new CommonApiResponse<>(ocrService.getOcrStatus(GMContext.getContext().getCharacterNo(), ocrNo));
    }

}
