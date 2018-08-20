package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.rest.model.vo.player.PlayToken;
import com.sktechx.godmusic.personal.rest.model.vo.player.StreamingStatus;
import com.sktechx.godmusic.personal.rest.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.08.09
 */

@Slf4j
@Service
public class PlayerServiceImpl implements PlayerService {

    @Autowired
    private RedisService redisService;

    private static final String PLAY_TOKEN_KEY_PREFIX = "godmusic.personalapi.playtoken:";
    private static final Long OVERLAP_STREAMING =  4015L;

    @Override
    public void putStreamingPermission(Long characterNo, Long memberDvcNo) {

        String RedisKey = PLAY_TOKEN_KEY_PREFIX + characterNo;

        PlayToken playToken = new PlayToken();
        playToken.setDeviceNo(memberDvcNo);
        playToken.setExpireTime(60);
        playToken.setTrackId(0L);

        redisService.setWithPrefix(RedisKey, playToken, 60);
    }

    @Override
    public StreamingStatus getStreamingPermission(Long characterNo, Long memberDvcNo) {

        String RedisKey = PLAY_TOKEN_KEY_PREFIX + characterNo;

        PlayToken redisPlayToken = redisService.getWithPrefix(RedisKey, PlayToken.class);

        if(redisPlayToken == null){
            return StreamingStatus.builder().resultCode(0L).status("none").build();
        } else if ( redisPlayToken.getDeviceNo().equals(memberDvcNo)){
            return StreamingStatus.builder().resultCode(0L).status("play").build();
        } else {
            return StreamingStatus.builder().resultCode(OVERLAP_STREAMING).status("play").build();
        }

    }

    @Override
    public void removeStreamingPermission(Long characterNo, Long memberDvcNo) {
        String RedisKey = PLAY_TOKEN_KEY_PREFIX + characterNo;

        PlayToken redisPlayToken = redisService.getWithPrefix(RedisKey, PlayToken.class);

        if(redisPlayToken != null &&  memberDvcNo.equals(redisPlayToken.getDeviceNo())){
            redisService.delWithPrefix(RedisKey);
        }
    }
}
