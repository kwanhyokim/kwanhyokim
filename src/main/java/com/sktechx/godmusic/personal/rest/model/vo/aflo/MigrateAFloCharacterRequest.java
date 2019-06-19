package com.sktechx.godmusic.personal.rest.model.vo.aflo;

import lombok.Data;

@Data
public class MigrateAFloCharacterRequest {
    private Long memberNo;
    private Long fromCharacterNo;
    private Long toCharacterNo;
}
