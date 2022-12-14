package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.domain.exception.ErrorDomain;
import com.sktechx.godmusic.personal.common.domain.type.AnalsStatusType;
import com.sktechx.godmusic.personal.common.domain.type.AwsBucketType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.rest.client.ExternalClient;
import com.sktechx.godmusic.personal.rest.model.dto.member.MemberDvcDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrEventDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrEventMemberDto;
import com.sktechx.godmusic.personal.rest.model.dto.ocr.OcrFileDto;
import com.sktechx.godmusic.personal.rest.model.vo.external.AwsFileVo;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.GetOcrStatusResponse;
import com.sktechx.godmusic.personal.rest.model.vo.ocr.OcrAnalsVo;
import com.sktechx.godmusic.personal.rest.repository.OcrMapper;
import com.sktechx.godmusic.personal.rest.service.MemberApiProxy;
import com.sktechx.godmusic.personal.rest.service.OcrHelperService;
import com.sktechx.godmusic.personal.rest.service.OcrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class OcrServiceImpl implements OcrService {

    @Autowired
    private ExternalClient externalApiProxy;

    @Autowired
    private MemberApiProxy memberApiProxy;

    @Autowired
    private OcrHelperService ocrHelperService;

    @Autowired
    private OcrMapper ocrMapper;

    @Override
    @Transactional
    public OcrDto createOcr(Long memberNo, Long characterNo, String deviceId, int totalFileCnt){

        MemberDvcDto memberDvcDto = getMemberDvc(memberNo, deviceId);

        OcrDto ocrDto = OcrDto.builder().memberNo(memberNo).characterNo(characterNo).memberDvcNo(memberDvcDto.getMemberDvcNo()).build();

        ocrMapper.insertOcr(ocrDto);

        for( int i = 0; i < totalFileCnt ; i++){
            ocrMapper.insertOcrFile(OcrFileDto.builder()
                    .ocrNo(ocrDto.getOcrNo())
                    .ocrFileNo(i+1)
                    .uploadYn(YnType.N)
                    .build());
        }

        return ocrDto;
    }

    @Override
    @Transactional(readOnly = true)
    public AwsFileVo uploadOcrFile(Long memberNo, Long characterNo, MultipartFile multipartFile, Long ocrNo, Integer ocrFileNo){

        if(ObjectUtils.isEmpty(memberNo) || ObjectUtils.isEmpty(characterNo))
            throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);

        OcrFileDto ocrFileDto = ocrMapper.selectOcrFile(characterNo, ocrNo, ocrFileNo);
        if(ObjectUtils.isEmpty(ocrFileDto)) {
            log.warn("OcrServiceImpl::uploadOcrFile - not found ocrFile! characterNo:{}, ocrNo:{}, ocrFileNo:{}", characterNo, ocrNo, ocrFileNo);
            throw new CommonBusinessException(PersonalErrorDomain.NOT_FOUND_OCR_FILE);
        }
        if(ocrFileDto.getUploadYn() == YnType.Y) {
            log.warn("OcrServiceImpl::uploadOcrFile - already upload ocrFile! ocrNo:{}, ocrFileNo:{}", ocrNo, ocrFileNo);
            throw new CommonBusinessException(PersonalErrorDomain.ALREADY_UPLOAD_OCR_FILE);
        }

        log.info(multipartFile.getOriginalFilename());
        AwsFileVo awsFileVo = null;
        try {
            awsFileVo = uploadFile(multipartFile, AwsBucketType.OCR, memberNo);
        }catch (Exception e){
            //??????????????? ?????? ????????? ????????? ??????????????? ????????? Y??? ???
            ocrHelperService.updateOcrFile(OcrFileDto.builder()
                    .ocrNo(ocrNo)
                    .ocrFileNo(ocrFileNo)
                    .analsStatusType(AnalsStatusType.FAIL_UPLOAD)
                    .build());
            throw new CommonBusinessException(PersonalErrorDomain.FAIL_UPLOAD_OCR_FILE);
        }

        return awsFileVo;
    }


    @Override
    @Transactional(readOnly = true)
    public void requestAnalysisToOcrServer(Long characterNo, Long ocrNo, Integer ocrFileNo, AwsFileVo awsFileVo){

        int fileCount = ocrMapper.countOcrFile(characterNo, ocrNo);
        ocrRecognize(ocrNo, ocrFileNo, fileCount, awsFileVo.getBucketKey(), awsFileVo.getBucket());

    }

    @Override
    @Transactional
    public void updateOcrFile(OcrFileDto ocrFileDto) {
        ocrMapper.updateOcrFile(ocrFileDto);
    }

    @Override
    @Transactional(readOnly = true)
    public OcrAnalsVo getOcrAnals(Long characterNo, Long ocrNo){

        OcrAnalsVo ocrResultVo = ocrMapper.selectOcrAnals(characterNo, ocrNo);

        if(ObjectUtils.isEmpty(ocrResultVo)){
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        // ?????? ??? duplicateYn ??????
        if(!ObjectUtils.isEmpty(ocrResultVo) && !ObjectUtils.isEmpty(ocrResultVo.getOcrAnalsResultList())){
            Set<Long> duplicateTrackIdSet = new HashSet<>();
            ocrResultVo.getOcrAnalsResultList().stream().forEach(i ->{
                if(!ObjectUtils.isEmpty(i.getOcrAnalsResultDetailList())){
                    i.getOcrAnalsResultDetailList().stream().forEach(r ->{
                        if(!ObjectUtils.isEmpty(r.getTrack()) && !ObjectUtils.isEmpty(r.getTrack().getTrackId())){
                            boolean nonDup = duplicateTrackIdSet.add(r.getTrack().getTrackId());
                            if(nonDup == false){
                                r.setDuplicateYn(YnType.Y);
                            }
                        }
                    });
                }
            });

        }

        return ocrResultVo;
    }

    @Override
    @Transactional(readOnly = true)
    public OcrEventDto getOcrEvent() {
        return ocrMapper.selectOcrEvent();
    }

    @Override
    @Transactional(readOnly = true)
    public GetOcrStatusResponse getOcrStatus(Long characterNo, Long ocrNo){

        int totalCount = ocrMapper.countOcrFile(characterNo, ocrNo);
        if(totalCount == 0){
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }
        int completeJobCount = ocrMapper.countDoneProcessionOcrFile(ocrNo);
        OcrDto ocrDto = ocrMapper.selectOcr(ocrNo);

        return GetOcrStatusResponse.builder()
                .totalCount(totalCount)
                .completeJobCount(completeJobCount)
                .confrmYn(ocrDto.getConfrmYn())
                .build();
    }

    @Override
    @Transactional
    public void noMorePush(Long characterNo, Long ocrNo){
        int totalCount = ocrMapper.countOcrFile(characterNo, ocrNo);
        if(totalCount > 0)
            ocrMapper.updateOcr(OcrDto.builder().ocrNo(ocrNo).confrmYn(YnType.Y).build());
    }


    private AwsFileVo uploadFile(MultipartFile file, AwsBucketType awsBucketType, Long memberNo) {

        log.debug("Aws FileUpload start:");
        CommonApiResponse<AwsFileVo> response = externalApiProxy.createOcrFile(file, awsBucketType, memberNo);
        log.debug("Aws FileUpload end");

        inspect4xxClientError(response);

        if(StringUtils.isEmpty(response) || StringUtils.isEmpty(response.getCode())
                || !"2000000".equals(response.getCode()) || CommonUtils.empty(response.getData())) throw new CommonBusinessException("file upload fail");

        return response.getData();
    }


    private void inspect4xxClientError(CommonApiResponse<AwsFileVo> response) {
        boolean is4xxClientError = Optional.ofNullable(response)
                .map(CommonApiResponse::getErrorDomain)
                .map(ErrorDomain::getHttpStatus)
                .map(HttpStatus::is4xxClientError)
                .orElse(false);

        if (is4xxClientError) {
            throw new CommonBusinessException(response.getErrorDomain());
        }
    }


    private MemberDvcDto getMemberDvc(Long memberNo, String deviceId) {

        log.debug("OcrServiceImpl::getMemberDvc start");
        CommonApiResponse<MemberDvcDto> response = memberApiProxy.getMemberDvc(memberNo, deviceId);
        log.debug("OcrServiceImpl::getMemberDvc end");

        if(StringUtils.isEmpty(response) || StringUtils.isEmpty(response.getCode())
                || !"2000000".equals(response.getCode()) || CommonUtils.empty(response.getData())) throw new CommonBusinessException("member-server getMemberDvc fail");

        return response.getData();
    }

    private void ocrRecognize( Long ocrNo, Integer ocrFileNo, Integer imageCount, String bucketKey, String bucketName){

        try {
            log.debug("ocrRecognize start:");
            CommonApiResponse<AwsFileVo> response = externalApiProxy.ocrRecognize(ocrNo, imageCount, ocrFileNo, bucketKey, bucketName);
            log.debug("ocrRecognize end");

            if (StringUtils.isEmpty(response) || StringUtils.isEmpty(response.getCode())
                    || !"2000000".equals(response.getCode()))
                throw new CommonBusinessException(PersonalErrorDomain.OUT_OF_OCR_SERVICE);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CommonBusinessException(PersonalErrorDomain.OUT_OF_OCR_SERVICE);
        }
    }

    @Override
    @Transactional
    public void createOcrEventMember(OcrEventMemberDto ocrEventMemberDto) {
        ocrMapper.insertOcrEventMember(ocrEventMemberDto);
    }

}
