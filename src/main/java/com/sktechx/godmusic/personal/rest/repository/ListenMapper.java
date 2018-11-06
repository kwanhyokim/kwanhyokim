package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time PM 7:39
 */
@BaseMapper
public interface ListenMapper {
	int addListenHistByChannel(@Param("listenType") String listenType, @Param("listenTypeId") Long listenTypeId,
	                           @Param("memberNo") Long memberNo, @Param("characterNo") Long characterNo);
}
