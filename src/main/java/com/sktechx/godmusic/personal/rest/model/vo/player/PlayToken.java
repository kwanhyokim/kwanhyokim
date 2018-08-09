package com.sktechx.godmusic.personal.rest.model.vo.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.08.09
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayToken {
    private Long deviceNo;
    private int expireTime;
    private Long trackId;
}
