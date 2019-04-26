package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.personal.rest.model.vo.ocr.CreateOcrSessionRequest;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrTrackListVo;
import org.springframework.web.multipart.MultipartFile;


public interface OcrService {

    String createOcrSession(CreateOcrSessionRequest request);

    void uploadOcrFile(Long memberNo, MultipartFile file, String sessionId, Integer index);

    OcrTrackListVo getOcrTrackList(String sessionId);
}
