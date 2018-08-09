package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.vo.player.StreamingStatus;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.08.09
 */

public interface PlayerService {

    void putStreamingPermission(Long characterNo, Long memberDvcNo);

    StreamingStatus getStreamingPermission(Long characterNo, Long memberDvcNo);

    void removeStreamingPermission(Long characterNo, Long memberDvcNo);

}
