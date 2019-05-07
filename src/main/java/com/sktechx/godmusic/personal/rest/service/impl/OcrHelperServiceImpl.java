package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrFileDto;
import com.sktechx.godmusic.personal.rest.repository.OcrMapper;
import com.sktechx.godmusic.personal.rest.service.OcrHelperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OcrHelperServiceImpl implements OcrHelperService {

    @Autowired
    private OcrMapper ocrMapper;


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateOcrFile(OcrFileDto ocrFileDto){
        ocrMapper.updateOcrFile(ocrFileDto);
    }

}
