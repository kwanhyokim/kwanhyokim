package com.sktechx.godmusic.personal.rest.repository;

import com.sktechx.godmusic.lib.mybatis.annotation.BaseMapper;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrFileDto;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrAnalsVo;
import org.apache.ibatis.annotations.Param;

@BaseMapper
public interface OcrMapper {

    void insertOcr(OcrDto ocrDto);
    void insertOcrFile(OcrFileDto ocrFileDto);
    void updateOcrFile(OcrFileDto ocrFileDto);

    OcrAnalsVo selectOcrAnals(@Param("ocrNo")Long ocrNo);

    int countDoneProcessionOcrFile(@Param("ocrNo") Long ocrNo);

    int countOcrFile(@Param("ocrNo") Long ocrNo);

}

