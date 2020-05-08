/*
 * Copyright (c) 2020 DREAMUS COMPANY.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.dto;



import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 설명 :
 *
 * @author N/A
 * @date 2020. 04. 27.
 */
public final class ImageInfoExpander {

    public static List<ImageInfo> expandChannelImageByResizeOption(ImageInfo originalImage) {

        if (originalImage == null) return Collections.emptyList();

        String imageUrlFormat = "{0}/dims/resize/{1}/quality/90";

        return Arrays.stream(ImageResizeOption.values())
                .map(imageResizeOption ->
                        new ImageInfo(
                                imageResizeOption.getSize(),
                                MessageFormat.format(
                                        imageUrlFormat, originalImage.getUrl(),
                                        imageResizeOption.getWidthHeightSameResizeOption())))
                .collect(Collectors.toList());
    }

    private enum ImageResizeOption {

        _75(75L),
        _140(140L),
        _200(200L),
        _350(350L),
        _500(500L),
        _1000(1000L)
        ;

        private Long size;

        ImageResizeOption(Long size) {
            this.size = size;
        }

        Long getSize() {
            return this.size;
        }

        String getWidthHeightSameResizeOption() {
            return this.size + "x" + this.size;
        }
    }
}
