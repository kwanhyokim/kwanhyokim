package com.sktechx.godmusic.personal.rest.model.dto.ocr;

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.AnalsStatusType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class OcrFileDto {
    private Long ocrNo;
    private Integer ocrFileNo;
    private YnType uploadYn;
    private AnalsStatusType analsStatusType;
    private Date analsStartDtime;
    private Date analsEndDtime;
    private String awsBucketNm;
    private String awsBucketKey;
    private Date sendPushDtime;
}
