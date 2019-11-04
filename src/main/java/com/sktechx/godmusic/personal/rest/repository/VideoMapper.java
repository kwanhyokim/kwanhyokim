/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.MostWatchedVideoDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 10. 13.
 */
@BaseMapper
public interface VideoMapper {

    /**
     * 캐릭터의 최근 본 영상 목록 조회
     */
    List<MostWatchedVideoDto> selectRecentVideoList(@Param("characterNo") Long characterNo, @Param("pageable") Pageable pageable);

    /**
     * 캐릭터의 최근 본 영상 Total Count
     */
    long selectRecentVideoTotalCount(@Param("characterNo") Long characterNo);

    /**
     * 캐릭터의 최근 본 영상 목록 삭제
     */
    void deleteRecentVideoList(@Param("characterNo") Long characterNo, @Param("videoIds") List<Long> videoIds);
}
