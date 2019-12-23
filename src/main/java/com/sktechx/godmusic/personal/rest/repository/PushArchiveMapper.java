/*
 * Copyright (c) 2019 Dreamus.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus.
 *
 * @project personal-api
 * @author dave(djin.chung@sk.com)
 * @date 19. 12. 19. 오후 2:26
 */

package com.sktechx.godmusic.personal.rest.repository;


import com.sktechx.godmusic.lib.mybatis.annotation.ArchiveMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 설명 :
 *
 * @author dave(djin.chung @ sk.com)
 * @date 2019. 12. 19.
 */

@ArchiveMapper
public interface PushArchiveMapper {
    /**
     * group id에 따른 push 통계에 click count 값을 증가
     */
    void updatePushClickCount(@Param("groupId") Long groupId);
}
