package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.listen.PurchasePassDto;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time PM 2:44
 */
@BaseMapper
public interface PurchaseMapper {
	String selectPssrlCd(@Param("memberNo")Long memberNo);
	PurchasePassDto selectInUsePurchaseIdByMemberNo(@Param("memberNo")Long memberNo);
}
