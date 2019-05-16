package com.sktechx.godmusic.personal.rest.model.dto.ocr;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class OcrFileDto {
    private Long ocrNo;
    private Integer ocrFileNo;
    private YnType uploadYn;
    private YnType analsSuccYn;
    private Date analsStartDtime;
    private Date analsEndDtime;
    private String awsBucketNm;
    private String awsBucketKey;
    private Date sendPushDtime;
}
