package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.vo.aflo.NewMigrateAFloCharacterRes;

public interface AFloService {

    NewMigrateAFloCharacterRes migrateAFloCharacter(Long memberNo, Long fromCharacterNo, Long toCharacterNo);
}
