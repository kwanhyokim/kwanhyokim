package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.PlayListDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.like.LikeVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

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
	int updateLikeDispSn(@Param("likeType") String likeType, @Param("characterNo") Long characterNo);
	int insertLike(@Param("likeType") String likeType, @Param("likeTypeId") Long likeTypeId, @Param("characterNo") Long characterNo);
	int deleteLike(@Param("likeType") String likeType, @Param("likeTypeId") List<Long> likeTypeId, @Param("characterNo") Long characterNo);
	void updateLikeListByLikeTypeId(@Param("likeType") String likeType, @Param("likeTypeId") Long likeTypeId, @Param("characterNo") Long characterNo,
	                                @Param("dispSn") int dispSn);

	List<TrackDto> getLikeTrackByLikeType(@Param("characterNo") Long characterNo, @Param("pageable") Pageable pageable);
	List<AlbumDto> getLikeAlbumByLikeType(@Param("characterNo") Long characterNo, @Param("pageable") Pageable pageable);
	List<ArtistDto> getLikeArtistByLikeType(@Param("characterNo") Long characterNo, @Param("pageable") Pageable pageable);
	List<PlayListDto> getLikePlaylistByLikeType(@Param("characterNo") Long characterNo, @Param("pageable") Pageable pageable);
}
