package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.personal.common.domain.type.AwsBucketType;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name="external-api")
public interface ExternalApiProxy {

    @PostMapping(value="/external/v1/ocr/ocr-recognize")
    CommonApiResponse ocrRecognize(@RequestParam("ocrNo") Long ocrNo,
                                   @RequestParam("imageCount") Integer imageCount,
                                   @RequestParam("ocrFileNo") Integer ocrFileNo,
                                   @RequestParam("bucketKey") String bucketKey,
                                   @RequestParam("bucketName") String bucketName);

    @PostMapping(value = "/external/v1/aws/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    CommonApiResponse<AwsFileVo> createOcrFile(@RequestPart(value = "file") MultipartFile file,
                                               @RequestParam("awsBucketType") AwsBucketType awsBucketType,
                                               @RequestParam("memberNo") Long memberNo);

}
