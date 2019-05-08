package com.sktechx.godmusic.personal.rest.model.dto.ocr;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrDto {

    private Long ocrNo;
    private Long memberNo;
    private Long characterNo;
    private Long memberDvcNo;

}
