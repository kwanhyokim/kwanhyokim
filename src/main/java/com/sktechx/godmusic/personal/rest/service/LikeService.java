package com.sktechx.godmusic.personal.rest.service;

import org.springframework.data.domain.Pageable;

import com.sktechx.godmusic.personal.rest.model.vo.like.*;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 3:39
 */
public interface LikeService {
	LikePlaylistListResponse getPlayListLikeListByLikeType(Long characterNo, String appVersion, Pageable pageable);
	LikeAlbumListResponse getAlbumLikeListByLikeType(Long characterNo, Pageable pageable);
	LikeArtistListResponse getArtistLikeListByLikeType(Long characterNo, Pageable pageable);
	LikeTrackListResponse getTrackLikeListByLikeType(Long characterNo, Pageable pageable);

	void addLike(LikeRequest request, Long characterNo);

	void deleteLike(LikeTypeIdListRequest request, Long characterNo);

	void updateLike(LikeTypeIdListRequest request, Long characterNo);

	LikeYnResponse getLikeYn(String likeType, Long likeTypeId, Long characterNo);
}
