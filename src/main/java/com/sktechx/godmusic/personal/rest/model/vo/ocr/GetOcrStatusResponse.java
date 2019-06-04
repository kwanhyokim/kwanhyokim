package com.sktechx.godmusic.personal.rest.model.vo.ocr;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetOcrStatusResponse {

    private Integer totalCount;

    private Integer completeJobCount;

    private YnType confrmYn;
}
