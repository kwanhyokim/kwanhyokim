package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.domain.type.AwsBucketType;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.CreateOcrSessionRequest;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrTrackListVo;
import com.sktechx.godmusic.personal.rest.service.ExternalApiProxy;
import com.sktechx.godmusic.personal.rest.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
public class OcrServiceImpl implements OcrService {

    @Autowired
    private ExternalApiProxy externalApiProxy;

    @Override
    public String createOcrSession(CreateOcrSessionRequest request){

        String ocrSession = UUID.randomUUID().toString().replace("-", "");

        //TODO DB설계 필요.


        return ocrSession;
    }

    @Override
    public void uploadOcrFile(Long memberNo, MultipartFile multipartFile, String sessionId, Integer index){


        log.info(multipartFile.getOriginalFilename());
        AwsFileVo awsFileVo = uploadFile(multipartFile, AwsBucketType.OCR, memberNo);
        //TODO DB설계 필요.

        //완료시잠 push 발송
    }

    @Override
    public OcrTrackListVo getOcrTrackList(String sessionId){

        return null;
    }

    private AwsFileVo uploadFile(MultipartFile file, AwsBucketType awsBucketType, Long memberNo) {

        log.debug("Aws FileUpload start:");
        CommonApiResponse<AwsFileVo> response = externalApiProxy.createOcrFile(file, awsBucketType, memberNo);
        log.debug("Aws FileUpload end");

        if(StringUtils.isEmpty(response) || StringUtils.isEmpty(response.getCode())
                || !"2000000".equals(response.getCode()) || CommonUtils.empty(response.getData())) throw new CommonBusinessException("file upload fail");

        return response.getData();
    }



}
