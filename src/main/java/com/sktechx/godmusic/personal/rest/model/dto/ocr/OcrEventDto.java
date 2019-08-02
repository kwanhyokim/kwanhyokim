package com.sktechx.godmusic.personal.rest.model.dto.ocr;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OcrEventDto {
    private Long ocrEventId;
    private String eventUrl;
}
