package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.ReadOnlyMapper;
import com.sktechx.godmusic.personal.rest.domain.badge.BadgeDto;

import java.util.List;

/**
 * 설명 : N/A
 *
 * @author Minkuk Jo / mingood92@gmail.com / https://velog.io/@mingood
 * @since 2020. 04. 01.
 */
@ReadOnlyMapper
public interface BadgeMapper {

    List<BadgeDto> findAll();

}
