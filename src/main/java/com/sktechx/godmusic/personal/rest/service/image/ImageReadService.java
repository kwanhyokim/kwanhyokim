package com.sktechx.godmusic.personal.rest.service.image;

/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

import java.util.List;
import java.util.Optional;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.personal.rest.model.dto.ImageManagementDto;
import com.sktechx.godmusic.personal.rest.model.dto.chart.DispPropsImageDto;

public interface ImageReadService {

    Optional<DispPropsImageDto> getChartDetailThumbnailImage(
            Long chartId,
            OsType osType
    );

    DispPropsImageDto getChartBackgroundImage(
            Long chartId,
            OsType osType
    );

    DispPropsImageDto getMixChartBackgroundImage(
            Long svcGenreId,
            Long chartId,
            OsType osType
    );

    List<ImageManagementDto> getImageManagementList(
            String imgContentType,
            Long imgContentId,
            String shortcutType
    );

}
