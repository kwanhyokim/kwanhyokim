package com.sktechx.godmusic.personal.rest.service.home;

/*
 * Copyright (c) 2019 DreamusCompany
 * All right reserved.
 *
 * This software is the confidential and proprietary information of Dreamus Company.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with Dreamus Company.
 */

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferDispDto;
import com.sktechx.godmusic.personal.rest.model.dto.CharacterPreferGenreDto;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPanel;

public interface HomeMetaService {

    CompletableFuture<List<CharacterPreferGenreDto>> getCharacterPreferGenreList(Long characterNo);

    CompletableFuture<List<CharacterPreferDispDto>> getCharacterPreferDispList(Long characterNo);

    CompletableFuture<List<PersonalPanel>> getPersonalRecommendPanelMeta(Long characterNo, Boolean checkDispEndDate);

    CompletableFuture<List<PersonalPanel>> getPersonalRecommendPanelMgoMeta(Long characterNo);

}
