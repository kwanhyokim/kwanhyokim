/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.event;

import com.sktechx.godmusic.personal.rest.model.dto.event.BestNineTrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.event.BestNineVo;
import com.sktechx.godmusic.personal.rest.repository.EventMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.util.Objects.requireNonNull;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 14.
 */
@Slf4j
@Service
public class BestNineEventImpl implements BestNineEvent {

    private EventConfig eventConfig;
    private EventMapper eventMapper;

    public BestNineEventImpl(EventConfig eventConfig, EventMapper eventMapper) {
        this.eventConfig = eventConfig;
        this.eventMapper = eventMapper;
    }

    /**
     * 베스트나인 이미지 앨범과 트랙 목록을 반환한다.
     * Note. 베스트나인 이미지(앨범이미지, SNS공유이미지)와 곡은 배치에서 미리 생성한다. 그러나 해당 정보를 요청하는 시점에
     *       트랙이 미권리곡으로 내려갈 수 있기 때문에 실제 배치가 생성한 곡 목록과 요청시 반환되는 곡 목록에는 차이가 생길 수 있다.
     */
    @Override
    public BestNineVo getBestNineTracks(Long characterNo, String eventDate) {
        requireNonNull(characterNo);
        requireNonNull(eventDate);

        BestNineTrackDto bestNineTrackDto = eventMapper.findBestNineTracks(characterNo, eventDate);
        if (bestNineTrackDto == null) {
            return null;
        }

        // 전시 순서로 트랙 목록 정렬
        bestNineTrackDto.sortTrackByDisplayOrderAsc();

        BestNineVo result = BestNineVo.from(bestNineTrackDto, eventConfig.getBaseURL());

        return result;
    }

    @PostConstruct
    private void loggingConfig() {
        log.info("BaseNine Event baseURL ... {}", eventConfig.getBaseURL());
    }
}
