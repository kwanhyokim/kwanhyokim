package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.dto.recommend.ListDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.*;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 3:39
 */
public interface LikeService {
	PageImpl<?> getLikeListByLikeType(String likeType, Long characterNo, Pageable pageable);

	void addLike(LikeRequest request, Long characterNo);

	void deleteLike(LikeListRequest request, Long characterNo);

	void updateLike(LikeTypeIdListRequest request, Long characterNo);

	LikeYnResponse getLikeYn(String likeType, Long likeTypeId, Long characterNo);
}
