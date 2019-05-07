package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrDto;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrAnalsVo;
import org.springframework.web.multipart.MultipartFile;


public interface OcrService {

    OcrDto createOcr(Long memberNo, Long characterNo, int totalFileCnt);

    void requestAnalysisToOcrServer(Long ocrNo, Integer ocrFileNo, AwsFileVo awsFileVo);

    AwsFileVo uploadOcrFile(Long memberNo, MultipartFile multipartFile, Long ocrNo, Integer ocrFileNo);

    OcrAnalsVo getOcrAnals(Long ocrNo);


}
