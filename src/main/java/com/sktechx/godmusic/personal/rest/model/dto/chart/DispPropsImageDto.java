/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

package com.sktechx.godmusic.personal.rest.model.dto.chart;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import lombok.Data;

/**
 * 설명 : 차트 전시 정보 dto
 *
 * @author 김관효(Kwanhyo Kim)/서버개발팀/DreamusCompany(kwanhyo.kim@sk.com)
 * @date 2020-04-17
 */

@Data
public class DispPropsImageDto {
    private Long id;
    private List<ImageInfo> imgList;

    public static DispPropsImageDto getDefault(Long chartId){
        return (chartId == 1L ? TOP100_DEFAULT : KIDS100_DEFAULT);
    }

    @JsonIgnore
    static final DispPropsImageDto TOP100_DEFAULT = new DispPropsImageDto() {
        {
            List<ImageInfo> imageInfoList = new ArrayList<>();
            imageInfoList.add(new ImageInfo(750L,
                    "https://www3.music-flo.com/mmate/feapi/img/display/genre_rc/temp/main_panel.jpg"));
            setId(1L);
            setImgList(imageInfoList);
        }
    };

    @JsonIgnore
    static final DispPropsImageDto KIDS100_DEFAULT = new DispPropsImageDto() {
        {
            List<ImageInfo> imageInfoList = new ArrayList<>();
            imageInfoList.add(new ImageInfo(750L,
                    "https://www3.music-flo.com/mmate/feapi/img/display/genre_rc/temp/kids_panel"
                            + ".jpg"));
            setId(3571L);
            setImgList(imageInfoList);
        }
    };
}
