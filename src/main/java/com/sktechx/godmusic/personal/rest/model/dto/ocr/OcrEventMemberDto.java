package com.sktechx.godmusic.personal.rest.model.dto.ocr;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrEventMemberDto {
    private Long memberNo;
    private Long ocrNo;
    private Long memberChnlId;
}
