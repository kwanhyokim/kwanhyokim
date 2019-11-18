/*
 * Copyright (c) 2019 DREAMUS COMPANY.
 * All right reserved.
 * This software is the confidential and proprietary information of DREAMUS COMPANY.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with DREAMUS COMPANY.
 */

package com.sktechx.godmusic.personal.rest.service.event;

import com.sktechx.godmusic.personal.rest.model.vo.event.BestNineVo;

/**
 * 설명 :
 *
 * @author Daniel/DREAMUS COMPANY (daekwon.song@sk.com)
 * @date 2019. 11. 14.
 */
public interface BestNineEvent {

    /**
     * Best Nine promotion 이벤트시 필요한 image 정보와 tracks 정보를 반환한다.
     *
     * @param characterNo 캐릭터 번호
     * @param eventDate best nine 이벤트 년월 ex. 201912
     * @return Bestnine 이벤트의 앨범 이미지와 앨범의 트랙 목록을 반환한다.
     */
    BestNineVo getBestNineTracks(Long characterNo, String eventDate);

}
