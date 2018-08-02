package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 4:38
 */
@BaseMapper
public interface LikeMapper {
	int getLikeCountByLikeTypeAndLikeTypeId(@Param("likeType") String likeType, @Param("likeTypeId") Long likeTypeId, @Param("characterNo") Long characterNo);
	int getLikeCountByLikeType(@Param("likeType") String likeType, @Param("characterNo") Long characterNo);
	int insertLike(@Param("likeType") String likeType, @Param("likeTypeId") Long likeTypeId, @Param("characterNo") Long characterNo);
	int deleteLike(@Param("likeType") String likeType, @Param("likeTypeId") Long likeTypeId, @Param("characterNo") Long characterNo);
	void updateLikeListByLikeTypeId(@Param("likeType") String likeType, @Param("likeTypeId") Long likeTypeId, @Param("characterNo") Long characterNo);
}
