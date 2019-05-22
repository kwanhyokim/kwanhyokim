package com.sktechx.godmusic.personal.rest.model.vo.ocr;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.common.domain.type.AnalsStatusType;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OcrAnalsVo {

    private Long ocrNo;
    private List<OcrAnalsResultVo> ocrAnalsResultList;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OcrAnalsResultVo{
        private Integer ocrFileNo;
        private YnType  completeJobYn;
        private AnalsStatusType analsStatusType;
        private List<OcrAnalsResultDetailVo> ocrAnalsResultDetailList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OcrAnalsResultDetailVo{
        @JsonIgnore
        private Long ocrAnalsResultNo;
        private Integer leftTopXAxis;
        private Integer leftTopYAxis;
        private Integer rightBottomXAxis;
        private Integer rightBottomYAxis;

        private YnType trackNmSkipYn;
        private YnType albumNmSkipYn;

        private YnType duplicateYn;

        private TrackDto track;
    }
}
