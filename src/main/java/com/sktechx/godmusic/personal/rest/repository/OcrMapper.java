package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrEventMemberDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrFileDto;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrAnalsVo;
import org.apache.ibatis.annotations.Param;

@BaseMapper
public interface OcrMapper {

    void insertOcr(OcrDto ocrDto);
    void updateOcr(OcrDto ocrDto);

    void insertOcrFile(OcrFileDto ocrFileDto);
    void updateOcrFile(OcrFileDto ocrFileDto);

    OcrAnalsVo selectOcrAnals(@Param("characterNo")Long characterNo, @Param("ocrNo")Long ocrNo);

    int countDoneProcessionOcrFile(@Param("ocrNo") Long ocrNo);

    int countOcrFile(@Param("characterNo")Long characterNo, @Param("ocrNo") Long ocrNo);

    OcrFileDto selectOcrFile(@Param("characterNo")Long characterNo, @Param("ocrNo")Long ocrNo, @Param("ocrFileNo")Integer ocrFileNo);

    OcrDto selectOcr(@Param("ocrNo") Long ocrNo);

    void insertOcrEventMember(OcrEventMemberDto ocrEventMemberDto);

}

