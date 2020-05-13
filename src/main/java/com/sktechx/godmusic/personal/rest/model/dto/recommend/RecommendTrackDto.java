/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import java.util.Date;
import java.util.List;

import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Builder;
import lombok.Data;

/**
 * 설명 : 추천 트랙 DTO
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */

@Builder
@Data
public class RecommendTrackDto {
    private Long characterNo;
    private Date dispStdStartDt;
    private Date dispStdEndDt;
    private Long svcGenreId;
    private Long rcmmdId;
    private Integer dispSn;

    private Date rcmmdCreateDtime;
    private Date rcmmdUpdateDtime;

    private List<TrackDto> trackList;
    private int trackCount;

    private ServiceGenreDto svcGenreDto;

    private List<ImageInfo> imgList;

    public void setImgList(List<ImageInfo> imgList) {

        if (imgList != null) {
            imgList.sort(null);
        }

        this.imgList = imgList;
    }

    public int getTrackCount(){

        if(trackCount > 0){
            return trackCount;
        }

        return this.trackList == null ? 0 : this.trackList.size();
    }

}
