/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.sktechx.godmusic.lib.mybatis.annotation.ReadOnlyMapper;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.preference.PreferSimilarArtistDto;

/**
 * 설명 :  아티스트 Repository
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 19.
 */
@ReadOnlyMapper
public interface ArtistMapper {
    //TODO : 추후 meta api 호출
    List<ArtistDto> getArtistList(@Param("artistIdList") List<Long> artistIdList);
    List<ArtistDto> selectArtistListByPreferArtist(@Param("characterNo") Long characterNo);

	List<ArtistDto> selectSeedArtistList(@Param("characterNo")Long characterNo, @Param("artistIdList")List<Long> artistIdList);

	List<PreferSimilarArtistDto> selectArtistListBySimilarArtist(@Param("characterNo")Long characterNo, @Param("artistIdList")List<Long> artistIdList);
	List<PreferSimilarArtistDto> selectArtistListBySimilarArtistOld(@Param("characterNo")Long characterNo, @Param("artistIdList")List<Long> artistIdList);
}
