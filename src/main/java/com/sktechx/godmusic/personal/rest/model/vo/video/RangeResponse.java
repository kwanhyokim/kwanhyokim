/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.model.vo.video;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

@Getter
public class RangeResponse<T> {

    private long totalCount;
    private int currentPage;
    private String lastPageYn;
    private List<T> list = Collections.emptyList();

    private RangeResponse(long total, int currentPage, String lastPageYn, List<T> result) {
        this.totalCount = total;
        this.currentPage = currentPage + 1;
        this.lastPageYn = lastPageYn;
        this.list = Collections.unmodifiableList(result);
    }

    public static <T> RangeResponse<T> empty() {
        return new RangeResponse<T>(0, 0, YnType.Y.getCode(), Collections.emptyList());
    }

    public static <T> RangeResponse<T> of(long total, int currentPage, String lastPageYn, List<T> items) {
        return new RangeResponse<T>(total, currentPage, lastPageYn, items);
    }

    public static <T> RangeResponse<T> of(Page<T> page) {
        return new RangeResponse<T>(
                page.getTotalElements(),
                page.getNumber(),
                page.isLast() ? YnType.Y.getCode() : YnType.N.getCode(),
                page.getContent());
    }
}
