package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeListRequest;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeListResponse;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeRequest;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeTypeIdListRequest;

import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 3:39
 */
public interface LikeService {
	ListDto<List<LikeListResponse>> getLikeListByLikeType(String likeType, Long characterNo);

	void addLike(LikeRequest request, Long characterNo);

	void deleteLike(LikeListRequest request, Long characterNo);

	void updateLike(LikeTypeIdListRequest request, Long characterNo);
}
