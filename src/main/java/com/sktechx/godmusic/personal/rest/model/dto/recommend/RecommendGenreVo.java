package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import java.util.Date;

import lombok.Data;

@Data
public class RecommendGenreVo {
    private String svcGenreNm;
    private Date dispStdStartDt;
    private Date createDtime;
}
