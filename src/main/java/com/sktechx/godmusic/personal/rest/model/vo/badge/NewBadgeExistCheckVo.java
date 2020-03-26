package com.sktechx.godmusic.personal.rest.model.vo.badge;

import lombok.Getter;
import lombok.ToString;

/**
 * 설명 : N/A
 *
 * @author Minkuk Jo / mingood92@gmail.com / https://velog.io/@mingood
 * @since 2020. 03. 25.
 */
@Getter
@ToString
public class NewBadgeExistCheckVo {
    private boolean isExistNewBadge = true;

    public NewBadgeExistCheckVo(boolean isExistNewBadge) {
        this.isExistNewBadge = isExistNewBadge;
    }
}
