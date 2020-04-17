package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import java.util.Date;

import lombok.Data;

@Data
public class RecommendForMeDto {
    private String svcGenreNm;
    private Date dispStdStartDt;
    private Date createDtime;
}
