package com.sktechx.godmusic.personal.rest.model.vo.ocr;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrRefiningResultVo {

    private Integer ocrFileNo;
    private Integer leftTopXAxis;
    private Integer leftTopYAxis;
    private Integer rightBottomXAxis;
    private Integer rightBottomYAxis;

    private YnType duplicateYn;

    private Long trackId;

}
