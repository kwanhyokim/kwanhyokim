package com.sktechx.godmusic.personal.rest.model.vo.ocr;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
public class OcrTrackListVo {

    private String sessionId;
    private String index;
    private List<OcrVo> ocrList;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OcrVo{
        private Integer x;
        private Integer Y;
        List<OcrTrackVo> trackList;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OcrTrackVo{

        private Long TrackYn;
        private Integer matchRate;
        private YnType titleEllipsisYn;
        private YnType artistEllipsisYn;
    }
}
