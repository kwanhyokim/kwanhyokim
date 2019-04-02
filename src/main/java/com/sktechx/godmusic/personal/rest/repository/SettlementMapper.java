package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 정산 코드 처리를 위한 mapper
 *
 * @author 박상현/SKTECH (sanghyun.park.tx@sk.com)
 * @date 2019. 3. 11.
 */
@BaseMapper
public interface SettlementMapper {
	String selectServiceCode(@Param("memberNo")Long memberNo, @Param("playType")String playType);
	String selectServiceCodeByPrchsId(@Param("prchsId")Long prchsId, @Param("playType")String playType);
}
