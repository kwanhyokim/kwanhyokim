package com.sktechx.godmusic.personal.rest.model.vo.ocr;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOcrSessionResponse {
    private String sessionId;
}
