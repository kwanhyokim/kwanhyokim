/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.model.dto.ChnlDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.PreferGenrePopularChnlDto;
import com.sktechx.godmusic.personal.rest.service.ChannelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 설명 : 채널 (플레이리스트 ) 서비스
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 25.
 */
@Service
@Slf4j
public class ChannelServiceImpl implements ChannelService {

    //TODO : 인기채널 GET
    public List<ChnlDto> getHotplayChannelList(Integer size){
        return null;
    }

    public List<PreferGenrePopularChnlDto> selectPreferGenrePopularChannel(Long characterNo){
        return null;
    }

}
