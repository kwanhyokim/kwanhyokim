package com.sktechx.godmusic.personal.rest.controller.v1;

import javax.validation.Valid;

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.ErrorDomain;
import com.sktechx.godmusic.personal.common.domain.type.AwsBucketType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.rest.client.ExternalClient;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrEventDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrEventMemberDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrFileDto;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.CommonConstant;
import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.common.domain.domain.Naming;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrDto;
import com.sktechx.godmusic.personal.rest.service.OcrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(Naming.serviceCode + "/v1/ocr")
@Api(value = "ocr", description = "ocr API - Chris")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @Autowired
    private ExternalClient externalApiProxy;


    @ApiOperation(value = "OCR 세션 정보 생성", httpMethod = "POST", notes = "OCR 분석할 파일을 올리기 위한, 세션 아이디를 생성 한다")
    @PostMapping("")
    public CommonApiResponse<CreateOcrSessionResponse> createOcrSession(
            @ApiParam(value = "디바이스 아이디", required = true, defaultValue = "test_device") @RequestHeader(value = CommonConstant.X_GM_DEVICE_ID) String deviceId,
            @Valid @RequestBody CreateOcrSessionRequest request) {

        OcrDto ocrDto = ocrService.createOcr(GMContext.getContext().getMemberNo(), GMContext.getContext().getCharacterNo(), GMContext.getContext().getDeviceId(), request.getTotalFileCnt());
        return new CommonApiResponse<>(CreateOcrSessionResponse.builder().ocrNo(ObjectUtils.isEmpty(ocrDto) ? null : ocrDto.getOcrNo()).build());
    }


    @PostMapping(value = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public AwsFileVo uploadOcrFile(MultipartFile file) {
        log.debug("Aws FileUpload start:");
        CommonApiResponse<AwsFileVo> response = externalApiProxy.createOcrFile(file, AwsBucketType.OCR, GMContext.getContext().getMemberNo());
        log.debug("Aws FileUpload end");

        // 4xx test
        if (Optional.ofNullable(response)
                .map(CommonApiResponse::getErrorDomain)
                .map(ErrorDomain::getHttpStatus)
                .map(HttpStatus::is4xxClientError)
                .isPresent()) {

            if (response.getErrorDomain().getHttpStatus().is4xxClientError()) {
                throw new CommonBusinessException(response.getErrorDomain());
            }
        }

        if (StringUtils.isEmpty(response) || StringUtils.isEmpty(response.getCode())
                || !"2000000".equals(response.getCode()) || CommonUtils.empty(response.getData()))
            throw new CommonBusinessException("file upload fail");

        return response.getData();
    }


    @ApiOperation(value = "OCR 파일 업로드", httpMethod = "POST", notes = "플레이리스트 이미지 업로드 한다.")
    @PostMapping(value = "/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonApiResponse uploadOcrFile(MultipartFile file, @RequestParam("ocrNo") Long ocrNo, @RequestParam("ocrFileNo") Integer ocrFileNo) {

        // select 로 검색 하여 기 처리된 데이터 인지 확인 upload 여부, 분석 요청 여부.
        AwsFileVo awsFileVo = ocrService.uploadOcrFile(GMContext.getContext().getMemberNo(), GMContext.getContext().getCharacterNo(), file, ocrNo, ocrFileNo);
        ocrService.updateOcrFile(OcrFileDto.builder()
                .ocrNo(ocrNo)
                .ocrFileNo(ocrFileNo)
                .awsBucketNm(awsFileVo.getBucket())
                .awsBucketKey(awsFileVo.getBucketKey())
                .uploadYn(YnType.Y)
                .build());

        ocrService.requestAnalysisToOcrServer(GMContext.getContext().getCharacterNo(), ocrNo, ocrFileNo, awsFileVo);

        ocrService.updateOcrFile(OcrFileDto.builder()
                .ocrNo(ocrNo)
                .ocrFileNo(ocrFileNo)
                .analsStartDtime(new Date())
                .build());
        return new CommonApiResponse<>().emptySuccess();
    }

    @ApiOperation(value = "OCR 이미지 분석 결과 도출", httpMethod = "GET", notes = "OCR 이미지 분석된 정보를 가져가는 API")
    @GetMapping("/{ocrNo}")
    public CommonApiResponse<OcrAnalsVo> getOcrResult(@PathVariable Long ocrNo) {

        OcrAnalsVo ocrAnalsVo = ocrService.getOcrAnals(GMContext.getContext().getCharacterNo(), ocrNo);

        OcrEventDto ocrEvent = ocrService.getOcrEvent();
        if (!ObjectUtils.isEmpty(ocrAnalsVo) && !ObjectUtils.isEmpty(ocrEvent)) {
            ocrAnalsVo.setEventUrl(ocrEvent.getEventUrl());
        }

        return new CommonApiResponse<>(ocrAnalsVo);
    }

    @ApiOperation(value = "OCR 이미지 분석 상태", httpMethod = "GET", notes = "OCR 이미지 분석된 정보를 가져가는 API")
    @GetMapping("/{ocrNo}/status")
    public CommonApiResponse<GetOcrStatusResponse> getOcrStatus(@PathVariable Long ocrNo) {

        return new CommonApiResponse<>(ocrService.getOcrStatus(GMContext.getContext().getCharacterNo(), ocrNo));
    }

    @ApiOperation(value = "OCR 분석 완료 후 push받지 않음. 사용자가 중간확인 하여 더이상 필요 없을 때", httpMethod = "PUT", notes = "OCR 분석 완료 후 push받지 않음. 사용자가 중간확인 하여 더이상 필요 없을 때")
    @PutMapping("/{ocrNo}/no-more-push")
    public CommonApiResponse noMorePush(@PathVariable Long ocrNo) {

        ocrService.noMorePush(GMContext.getContext().getCharacterNo(), ocrNo);

        return new CommonApiResponse<>().emptySuccess();
    }


    @ApiOperation(value = "OCR 이벤트 응모 정보 저장", httpMethod = "POST", notes = "OCR 이벤트 응모 정보를 저장한다.")
    @PostMapping("/event")
    public CommonApiResponse createOcrEventMember(
            @Valid @RequestBody CreateOcrEventMemberRequest request) {

        ocrService.createOcrEventMember(OcrEventMemberDto.builder()
                .memberNo(GMContext.getContext().getMemberNo())
                .ocrNo(request.getOcrNo())
                .memberChnlId(request.getMemberChnlId())
                .build());

        return CommonApiResponse.emptySuccess();
    }


}
