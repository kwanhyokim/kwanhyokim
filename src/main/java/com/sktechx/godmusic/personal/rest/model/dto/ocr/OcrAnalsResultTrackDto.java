package com.sktechx.godmusic.personal.rest.model.dto.ocr;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrAnalsResultTrackDto {
    private Long ocrAnalsResultNo;
    private Long trackId;
    private Float matchingRate;
}
