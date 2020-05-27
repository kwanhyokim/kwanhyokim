/*
 *
 *  * Copyright (c) 2018 SK TECHX.
 *  * All right reserved.
 *  *
 *  * This software is the confidential and proprietary information of SK TECHX.
 *  * You shall not disclose such Confidential Information and
 *  * shall use it only in accordance with the terms of the license agreement
 *  * you entered into with SK TECHX.
 *
 */

package com.sktechx.godmusic.personal.rest.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.sktechx.godmusic.personal.common.util.DateUtil;
import com.sktechx.godmusic.personal.rest.client.PersonalMongoClient;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.util.Strings;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.*;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.rest.model.dto.*;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistRetriveAllResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackCreateResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackRetrieveAllResponse;
import com.sktechx.godmusic.personal.rest.repository.*;
import com.sktechx.godmusic.personal.rest.service.MemberChannelService;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendImageManagementService;
import lombok.extern.slf4j.Slf4j;

/**
 * 설명 :
 *
 * @author 안영현/SKTECHX (younghyun.ahn@sk.com)
 * @date 2018. 8. 6.
 */
@Service
@Transactional
@Slf4j
public class MemberChannelServiceImpl implements MemberChannelService {
    private final String CHANNEL_NAME_NUMBER_REGEX = "^(.*)\\(([0-9]+)\\)$";

    @Autowired
    MemberChannelMapper memberChannelMapper;

    @Autowired
    MemberChannelTrackMapper memberChannelTrackMapper;

    @Autowired
    ChannelMapper channelMapper;

    @Autowired
    ChartMapper chartMapper;

    @Autowired
    TrackMapper trackMapper;

    @Autowired
    MemberChannelImageMapper memberChannelImageMapper;

    @Autowired
    RecommendImageManagementMapper recommendImageManagementMapper;

    @Autowired
    RecommendImageManagementService recommendImageManagementService;

    @Autowired
    AmqpService amqpService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    PersonalMongoClient personalMgoClient;


    @Override
    @Transactional(readOnly = true)
    public MyPlaylistRetriveAllResponse getMemberChannelPageImpl(Long memberNo, Long characterNo, Long channelId, Pageable pageable) {
        List<Long> idList = memberChannelMapper.selectMemberChannelIdList(memberNo, characterNo, channelId, pageable);

        if (CollectionUtils.isEmpty(idList)) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        List<MemberChannelDto> list = memberChannelMapper.selectMemberChannelList(idList);
        int totalCount = memberChannelMapper.selectMemberChannelTotalCount(memberNo, characterNo);

        for (MemberChannelDto memberChannelDto : list) {
            List<ImageInfo> recommendImageList = getRecommendImageList(ImageDisplayType.RCT_DTL, memberChannelDto);

            if (memberChannelDto.getAlbum() == null) { // 추천 이미지 처리를 위해 더미로 생성
                memberChannelDto.setAlbum(AlbumDto.builder().albumId(0L).build());
                memberChannelDto.setChannelPlayTime("0");
                memberChannelDto.setTrackCount(0);
            }

            if (!CollectionUtils.isEmpty(recommendImageList)) {
                memberChannelDto.getAlbum().setImgList(recommendImageList);
            }
        }

        return new MyPlaylistRetriveAllResponse(new PageImpl<>(list, pageable, totalCount));
    }

    @Override
    @Transactional(readOnly = true)
    public MyPlaylistTrackRetrieveAllResponse getMemberChannelTrackList(Long memberNo, Long characterNo, Long memberChannelId, Pageable pageable) {
        List<TrackDto> list = memberChannelTrackMapper.selectMemberChannelTrackList(memberNo, characterNo, memberChannelId, pageable);

        if (CollectionUtils.isEmpty(list)) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        }

        // MemberChannelDto memberChannelDto = getMemberChannel(memberNo, characterNo, memberChannelId);

        // Note. pinTpype 이 존재하는 채널에도 곡을 추가할 수 있기 때문에
        //       일반채널과 동일하게 update_dtime_desc + track_sn_asc 로 정렬하도록 한다.
        // if (memberChannelDto.getPinType() != null) {
        //    list = list.stream().sorted(Comparator.comparing(TrackDto::getTrackSn)).collect(Collectors.toList());
        // }

        long totalCount = memberChannelTrackMapper.selectMemberChannelTrackListCount(memberNo, characterNo, memberChannelId);

        return new MyPlaylistTrackRetrieveAllResponse(new PageImpl<>(list, pageable, totalCount));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberChannelDto getMemberChannel(Long memberNo, Long characterNo, Long memberChannelId) {
        MemberChannelDto memberChannelDto = memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId);

        List<ImageInfo> recommendImageList = getRecommendImageList(ImageDisplayType.RCT_DTL, memberChannelDto);

        if (memberChannelDto.getAlbum() == null) { // 추천 이미지 처리를 위해 더미로 생성
            memberChannelDto.setAlbum(AlbumDto.builder().albumId(0L).build());
            memberChannelDto.setChannelPlayTime("0");
            memberChannelDto.setTrackCount(0);
        }

        if (!CollectionUtils.isEmpty(recommendImageList)) {
            memberChannelDto.getAlbum().setImgList(recommendImageList);
        }

        return memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId);
    }

    @Override
    public MyPlaylistTrackCreateResponse pinMemberChannel(Long memberNo, Long characterNo, PinType pinType, Long pinTypeId) {
        // 1000 채널 초과 체크
        if (memberChannelMapper.selectMemberChannelCount(memberNo, characterNo) >= FixedSize.MY_CHANNEL.getSize()) {
            throw new CommonBusinessException(PersonalErrorDomain.MY_CHANNEL_OVER_CREATE);
        }

        String memberChannelName = Strings.EMPTY;
        List<Long> trackIdList = null;
        List<ImageManagementDto> recommendImageList = Collections.EMPTY_LIST;

        if (PinType.CHNL == pinType || PinType.MY_CHNL == pinType || PinType.FLAC == pinType || PinType.AFLO == pinType) {
            ChnlDto chnlDto = channelMapper.selectChannelById(pinTypeId);
            memberChannelName = chnlDto.getChnlNm(); // TODO : chnlDispNm?
            trackIdList = chnlDto.getTrackList().stream().map(x -> x.getTrackId()).collect(Collectors.toList());

            if(PinType.FLAC == pinType || PinType.AFLO == pinType){
            	pinType = PinType.CHNL;
            }

        } if (PinType.RC_SML_TR == pinType) {
            memberChannelName = pinType.getTitle();
            trackIdList = trackMapper.selectRecommendPanelSimilarTrackList(characterNo, pinTypeId);
            recommendImageList = recommendImageManagementMapper.selectRecommendImageManagementList(RecommendPanelContentType.fromCode(pinType.getCode()), pinTypeId, null, null);

        } if (PinType.RC_GR_TR == pinType) {
            memberChannelName = pinType.getTitle();
            trackIdList = trackMapper.selectRecommendPanelGenreTrackList(characterNo, pinTypeId);
            recommendImageList = recommendImageManagementMapper.selectMappingImageRecommendImageList(RecommendPanelContentType.fromCode(pinType.getCode()), pinTypeId, null, null);

        } if (PinType.RC_CF_TR == pinType) {
            memberChannelName = pinType.getTitle();
            trackIdList = trackMapper.selectRecommendPanelCfTrackList(characterNo, pinTypeId);
            recommendImageList = recommendImageManagementMapper.selectMappingImageRecommendImageList(RecommendPanelContentType.fromCode(pinType.getCode()), pinTypeId, null, null);

        } if (PinType.RC_ATST_TR == pinType) {
            memberChannelName = pinType.getTitle();
            trackIdList = trackMapper.selectRecommendPanelPopularTrackList(characterNo, pinTypeId);
            recommendImageList = recommendImageManagementMapper.selectFixedRecommendImageList(RecommendPanelContentType.fromCode(pinType.getCode()), pinTypeId, null, null);
        }
        if (PinType.RC_LIKE_SML_TR == pinType) {
            memberChannelName = pinType.getTitle();
            trackIdList = personalMgoClient.getLikeRelatedRecommendTrackIds(characterNo, pinTypeId, pinType.getCode());
            recommendImageList = recommendImageManagementMapper.selectAdaptivePanelImageList(RecommendPanelContentType.RC_LIKE_SML_TR, ImageDisplayType.RCT_DTL, null);
        }
        /*} else if (PinType.CHART == pinType) {
            ChartDto chartDto = chartMapper.selectChartMusicContentList(pinId);
            memberChannelName = chartDto.getChartNm();
            trackIdList = chartDto.getTrackList().stream().map(x -> x.getTrackId()).collect(Collectors.toList());
        }*/

        if (StringUtils.isEmpty(memberChannelName) || CollectionUtils.isEmpty(trackIdList)) {
            throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);
        } else {
            memberChannelName = LocalDate.now().getMonthValue() + "월 " + LocalDate.now().getDayOfMonth() + "일 " + memberChannelName;
        }

        String appName = GMContext.getContext().getAppName();
        AppNameType appNameType = AppNameType.fromCode(appName);

        MemberChannelDto memberChannelDto = createMemberChannel(memberNo, characterNo, memberChannelName, pinType, pinTypeId);

        if (!CollectionUtils.isEmpty(recommendImageList)) {
            for (ImageManagementDto imageManagementDto : recommendImageList) {
                memberChannelImageMapper.insertMemberChannelImage(memberChannelDto.getMemberChannelId(),
                        imageManagementDto.getImgDispType(),
                        imageManagementDto.getOsType(),
                        imageManagementDto.getImgSize(),
                        imageManagementDto.getImgUrl());
            }
        }

        return addTrackList(appNameType, memberNo, characterNo, memberChannelDto.getMemberChannelId(), trackIdList);
    }

    @Override
    public MemberChannelDto createMemberChannel(Long memberNo, Long characterNo, String memberChannelName) {
        return createMemberChannel(memberNo, characterNo, memberChannelName, null, null);
    }

    @Override
    public MemberChannelDto createMemberChannel(Long memberNo, Long characterNo, String memberChannelName, PinType pinType, Long pinTypeId) {
        // 1000 채널 초과 체크
        if (memberChannelMapper.selectMemberChannelCount(memberNo, characterNo) >= FixedSize.MY_CHANNEL.getSize()) {
            throw new CommonBusinessException(PersonalErrorDomain.MY_CHANNEL_OVER_CREATE);
        }

        MemberChannelDto memberChannel = new MemberChannelDto();
        memberChannel.setPinType(pinType);
        memberChannel.setPinTypeId(pinTypeId);

        int duplicateChannelNameCount = memberChannelMapper.selectMemberChannelEqualsName(memberNo, characterNo, memberChannelName);

        if (duplicateChannelNameCount > 0) {
            String likeCondition = memberChannelName.concat("(%)");
            List<String> likeConditionChannelList = memberChannelMapper.selectMemberChannelLikeNameList(memberNo, characterNo, likeCondition);
            int channelNumber = getMemberChannelNameNumbering(memberChannelName, likeConditionChannelList);
            memberChannel.setMemberChannelName(memberChannelName + "(" + channelNumber + ")");
        } else {
            memberChannel.setMemberChannelName(memberChannelName);
        }

        memberChannel.setAlbum(AlbumDto.builder().albumId(0L).build());
        memberChannel.setChannelPlayTime("0");
        memberChannel.setTrackCount(0);

        memberChannelMapper.insertMemberChannel(memberNo, characterNo, memberChannel);

        List<Long> updateMemberChannelIdList = new ArrayList<>();
        updateMemberChannelIdList.add(memberChannel.getMemberChannelId());

        List<Long> myChannelIdList = memberChannelMapper.selectMemberChannelIdList(memberNo, characterNo, memberChannel.getMemberChannelId(), null);
        if (!CollectionUtils.isEmpty(myChannelIdList)) {
            updateMemberChannelIdList.addAll(myChannelIdList);
        }

        modifyMemberChannelList(memberNo, characterNo, updateMemberChannelIdList);
        return memberChannel;
    }

    @Override
    public void modifyMemberChannelList(Long memberNo, Long characterNo, List<Long> viewPriorityChannelIdList){
        AtomicInteger atomicInteger = new AtomicInteger(0);

        try(SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)){
            Map<String, Object> batchParam = new HashMap<>();
            IntStream.range(0, viewPriorityChannelIdList.size())
                    .forEach(index ->
                            {
                                batchParam.clear();
                                batchParam.put("memberNo",memberNo);
                                batchParam.put("characterNo",characterNo);
                                batchParam.put("channelId",viewPriorityChannelIdList.get(index));
                                batchParam.put("viewPriority", atomicInteger.getAndIncrement()+1);
                                batchParam.put("albumId", null);
                                batchParam.put("updateTrackCount", false);
                                batchParam.put("updatePlayTime", false);
                                batchParam.put("updateDateTime", null);
                                sqlSession.update("updateMemberChannelList", batchParam);
                            }
                    );
            sqlSession.flushStatements();
            sqlSession.commit();
            sqlSession.close();
        }catch(Exception e){
            log.error("MyChannel :: track id list order modify :: Error Message", e.getMessage());
            throw new CommonBusinessException(CommonErrorDomain.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public void removeMemberChannel(Long memberNo, Long characterNo, List<Long> memberChannelIdList){
        memberChannelTrackMapper.deleteMapMemberChannelTrack(memberNo, characterNo, memberChannelIdList);
        memberChannelMapper.deleteMemberChannel(memberNo, characterNo, memberChannelIdList);
    }

    @Override
    public void modifyMemberChannel(Long memberNo, Long characterNo, Long memberChannelId, String memberChannelName) {
        int duplicateChannelNameCount = memberChannelMapper.selectMemberChannelEqualsName(memberNo, characterNo, memberChannelName);

        if (duplicateChannelNameCount > 0) {
            throw new CommonBusinessException(PersonalErrorDomain.MY_CHANNEL_DUPLICATED_NAME);
        }

        memberChannelMapper.updateMemberChannel(memberNo, characterNo, memberChannelId, memberChannelName);
    }

    @Override
    public MemberChannelDto removeTrackList(AppNameType appName, Long memberNo, Long characterNo, Long memberChannelId, List<Long> trackIdList) {

        if (ObjectUtils.isEmpty(memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId))) {
            throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
        }

        memberChannelTrackMapper.deleteTrack(memberChannelId, trackIdList);

        // 앨범 아이디 및 trackCount 업데이트
        Long albumId = getMemberChannelAlbumId(memberChannelId);
        memberChannelMapper.updateMemberChannelList(memberNo, characterNo, memberChannelId, null, albumId, true, true, new Date());

        return getMemberChannel(memberNo, characterNo, memberChannelId);
    }

    @Override
    public MyPlaylistTrackCreateResponse addTrackList(AppNameType appName, Long memberNo, Long characterNo, Long memberChannelId, List<Long> trackIdList){

        if(ObjectUtils.isEmpty(memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId))){
            throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
        }

        int trackTotalCnt = memberChannelTrackMapper.selectTrackTotalCount(memberChannelId);
        if(trackTotalCnt >= FixedSize.MY_CHANNEL_TRACK.getSize()
                || (trackTotalCnt + trackIdList.size()) > FixedSize.MY_CHANNEL_TRACK.getSize()){
            throw new CommonBusinessException(PersonalErrorDomain.MY_CHANNEL_TRACK_OVER_ADD);
        }

        List<Long> duplicateKeyIdList = new ArrayList<>();
        List<Long> dataIntegrityIdList = new ArrayList<>();
        List<Long> successfulIdList = new ArrayList<>();

        int maxViewPriority = memberChannelTrackMapper.selectMaxTrackViewPriority(memberChannelId);
        AtomicInteger viewPriority = new AtomicInteger(maxViewPriority+1);

        // displayYn이 N인 경우는 로드되지 않게 하기 위함.
        List<TrackDto> trackDtoList = getTrackList(trackIdList);

        if (!ObjectUtils.isEmpty(trackDtoList)) {

            Date now = DateUtil.asDate(LocalDateTime.now().withNano(0));

            for (TrackDto trackDto : trackDtoList) {
                try{

                    if (trackDto.isNotStreamable() && trackDto.isNotDownloadable()) {
                        dataIntegrityIdList.add(trackDto.getTrackId());
                        continue;
                    }

                    memberChannelTrackMapper.insertTrackMemberChannel(memberChannelId, trackDto.getTrackId(), viewPriority.getAndIncrement(), now);
                    successfulIdList.add(trackDto.getTrackId());

                }
                catch (Exception e) {
                    // 중복 등록 - 키 중복 에러로 체크
                    if(e instanceof DuplicateKeyException){
                        duplicateKeyIdList.add(trackDto.getTrackId());
                    }
                    // 비존재 트랙(실제로는 Meta 비동기 상태이지만 권리사 미제공으로 정리) - 외래키 에러로 체크
                    else if(e instanceof DataIntegrityViolationException){
                        dataIntegrityIdList.add(trackDto.getTrackId());
                    }
                }
            }
        }

        //앨범 아이디 및 trackCount 업데이트
        Long albumId = getMemberChannelAlbumId(memberChannelId);
        
        try {
            memberChannelMapper.updateMemberChannelList(memberNo, characterNo, memberChannelId, null, albumId, true, true, new Date());
        } catch (Exception e) {
            log.warn("Update Database Log : memberNo={}, characterNo={}, memberChannelId={}, albumId={}",
                    memberNo, characterNo, memberChannelId, albumId);
            throw e;
        }
    
        //TODO 업데이트 한 member channel의 sn 순서를 1로 변경 해준다.
        MemberChannelDto myChannel = getMemberChannel(memberNo, characterNo, memberChannelId);

        return MyPlaylistTrackCreateResponse.builder()
                .memberChannel(myChannel)
                .successfulCnt(successfulIdList.size())
                .duplicatedYn(duplicateKeyIdList.size() > 0 ? YnType.Y : YnType.N)
                .agencyCancelYn(dataIntegrityIdList.size() > 0 ? YnType.Y : YnType.N)
                .build();
    }

    private List<TrackDto> getTrackList(List<Long> trackIdList){

        if(CollectionUtils.isEmpty(trackIdList)){
            return null;
        }

        // TODO feign 으로 변경 필요
        return trackMapper.selectTrackList(trackIdList);
    }

    @Override
    public MemberChannelDto modifyTrackList(Long memberNo, Long characterNo, Long memberChannelId, List<Long> modifyTrackIdList){

        MemberChannelDto memberChannelDto = memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId);

        if(ObjectUtils.isEmpty(memberChannelDto)){
            throw new CommonBusinessException(CommonErrorDomain.BAD_REQUEST);
        }

        List<Long> memberChannelTrackIdList = memberChannelTrackMapper.selectMemberChannelTrackIdList(memberNo, characterNo, memberChannelId);
        int memberChannlAllTrackIdCount = memberChannelTrackIdList.size();
        int modifyTrackIdCount = modifyTrackIdList.size();

        //같으면 전체 업데이트, 다르면 변경된 부분만 잘라서 업데이트
        List<Long> remainderTrackIdList = memberChannelTrackIdList.subList(Math.min(modifyTrackIdCount, memberChannlAllTrackIdCount), memberChannlAllTrackIdCount);
        modifyTrackIdList.addAll(remainderTrackIdList);

        AtomicInteger atomicInteger = new AtomicInteger(0);
        Date updateDateTime = new Date();

        try(SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)){
            Map<String, Object> batchParam = new HashMap<>();
            IntStream.range(0, modifyTrackIdList.size())
                    .forEach(index ->
                            {
                                batchParam.clear();
                                batchParam.put("channelId", memberChannelId);
                                batchParam.put("trackId", modifyTrackIdList.get(index));
                                batchParam.put("viewPriority", atomicInteger.getAndIncrement()+1);
                                batchParam.put("updateDateTime", updateDateTime);
                                sqlSession.update("updateTrack", batchParam);
                            }
                    );
            sqlSession.flushStatements();
            sqlSession.commit();
            sqlSession.close();
        }catch(Exception e){
            log.error("MyChannel :: track id list order modify :: Error Message", e.getMessage());
            throw new CommonBusinessException(CommonErrorDomain.INTERNAL_SERVER_ERROR);
        }

        // 첫번째 이미지 트랙으로 채널 이미지 변경
        memberChannelMapper.updateMemberChannelImg(memberChannelId, 1);

        return getMemberChannel(memberNo, characterNo, memberChannelId);
    }

    private Long getMemberChannelAlbumId(Long memberChannelId){
        Long albumId = memberChannelMapper.selectMemberChannelAlbumId(memberChannelId);
        if(albumId == null){
            albumId = 0L;
        }
        return albumId;
    }

    private int getMemberChannelNameNumbering(String originalChannelName, List<String> likeConditionChannelList){

        List<Integer> validChannelNumberList = getValidChannelNumberFilter(originalChannelName,likeConditionChannelList);
        Collections.sort(validChannelNumberList);

        return getNewChannelNumber(validChannelNumberList);
    }

    private List<Integer> getValidChannelNumberFilter(String originalChannelName,List<String> likeConditionChannelList){
        Pattern pattern = Pattern.compile(CHANNEL_NAME_NUMBER_REGEX);
        Set<Integer> validChannelNumberSet = new HashSet<>();
        for (String likeConditionChannelName : likeConditionChannelList) {
            Matcher matcher = pattern.matcher(likeConditionChannelName);
            if(matcher.matches()){
                String matchingChannelName = matcher.group(1);
                if(originalChannelName.equals(matchingChannelName)){
                    String channelNumber = matcher.group(2);
                    validChannelNumberSet.add(Integer.valueOf(channelNumber));
                }
            }
        }
        return new ArrayList<>(validChannelNumberSet);
    }

    private Integer getNewChannelNumber(List<Integer> numberArray) {
        if( CollectionUtils.isEmpty(numberArray) || numberArray.get(0) > 1)
            return 1;
        for (int i = 0;  i < numberArray.size()-1; i++) {
            if(numberArray.get(i)+1 < numberArray.get(i+1)){
                return numberArray.get(i)+1;
            }
        }
        return numberArray.get(numberArray.size()-1)+1;
    }

    private List<ImageInfo> getRecommendImageList(ImageDisplayType imageDisplayType, MemberChannelDto memberChannelDto) {
        List<ImageInfo> recommendImageList = Collections.EMPTY_LIST;
        Long recommendId = memberChannelDto.getPinTypeId();

        if (recommendId != null) {
            RecommendPanelContentType recommendType = RecommendPanelContentType.fromCode(memberChannelDto.getPinType().getCode());

            if (recommendType != null) {
                OsType personalOsType = GMContext.getContext().getOsType();
                com.sktechx.godmusic.personal.common.domain.type.OsType osType = com.sktechx.godmusic.personal.common.domain.type.OsType.fromCode(personalOsType.getCode());

//                List<ImageManagementDto> imageList = recommendImageManagementService.getRecommendImageList(recommendType, recommendId, imageDisplayType, osType);
                List<MemberChannelImageDto> imageList = memberChannelImageMapper.selectMemberChannelImageList(memberChannelDto.getMemberChannelId(), imageDisplayType.getCode(), osType.getCode());

                if (!CollectionUtils.isEmpty(imageList)) {
                    recommendImageList = imageList.stream().map(x -> new ImageInfo(x.getImgSize(), x.getImgUrl())).collect(Collectors.toList());
                }
            }
        }

        return recommendImageList;
    }
}