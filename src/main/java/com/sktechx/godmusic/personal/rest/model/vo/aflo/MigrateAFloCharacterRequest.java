package com.sktechx.godmusic.personal.rest.model.vo.aflo;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class MigrateAFloCharacterRequest {
    @NotNull
    private Long memberNo;
    @NotNull
    private Long fromCharacterNo;
    @NotNull
    private Long toCharacterNo;
}
