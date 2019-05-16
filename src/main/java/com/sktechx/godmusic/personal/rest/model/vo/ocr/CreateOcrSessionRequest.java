package com.sktechx.godmusic.personal.rest.model.vo.ocr;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class CreateOcrSessionRequest {
    @Min(1)
    @Max(20)
    private int totalFileCnt;
}
