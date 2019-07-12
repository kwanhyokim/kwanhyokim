package com.sktechx.godmusic.personal.rest.repository;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;

@BaseMapper
public interface AfloMapper {
    Date selectAfloCharacterNo(@Param("characterNo") Long characterNo);
}
