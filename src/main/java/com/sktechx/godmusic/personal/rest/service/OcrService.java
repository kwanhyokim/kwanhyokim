package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrEventMemberDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrFileDto;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrAnalsVo;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.GetOcrStatusResponse;
import org.springframework.web.multipart.MultipartFile;


public interface OcrService {

    OcrDto createOcr(Long memberNo, Long characterNo, String deviceId, int totalFileCnt);

    void requestAnalysisToOcrServer(Long characterNo, Long ocrNo, Integer ocrFileNo, AwsFileVo awsFileVo);

    AwsFileVo uploadOcrFile(Long memberNo, Long characterNo, MultipartFile multipartFile, Long ocrNo, Integer ocrFileNo);

    OcrAnalsVo getOcrAnals(Long characterNo, Long ocrNo);

    GetOcrStatusResponse getOcrStatus(Long characterNo, Long ocrNo);

    void noMorePush(Long characterNo, Long ocrNo);

    void updateOcrFile(OcrFileDto ocrFileDto);

    void createOcrEventMember(OcrEventMemberDto ocrEventMemberDto);

}
