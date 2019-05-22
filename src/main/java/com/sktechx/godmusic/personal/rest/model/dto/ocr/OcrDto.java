package com.sktechx.godmusic.personal.rest.model.dto.ocr;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrDto {

    private Long ocrNo;
    private Long memberNo;
    private Long characterNo;
    private Long memberDvcNo;
    private YnType confrmYn;

}
