package com.sktechx.godmusic.personal.rest.model.dto.ocr;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrAnalsResultDto {
    private Long ocrAnalsResultNo;
    private Long ocrNo;
    private Integer ocrFileNo;

    private Integer leftTopXAxis;
    private Integer leftTopYAxis;
    private Integer rightBottomXAxis;
    private Integer rightBottomYAxis;

    private YnType trackNmSkipYn;
    private YnType albumNmSkipYn;

}
