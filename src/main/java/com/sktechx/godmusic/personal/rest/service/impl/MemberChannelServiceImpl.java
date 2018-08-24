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

import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEvent;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventTarget;
import com.sktechx.godmusic.personal.common.amqp.domain.UserEventType;
import com.sktechx.godmusic.personal.common.amqp.service.AmqpService;
import com.sktechx.godmusic.personal.common.domain.type.AppNameType;
import com.sktechx.godmusic.personal.common.domain.type.FixedSize;
import com.sktechx.godmusic.personal.common.exception.CommonErrorMessage;
import com.sktechx.godmusic.personal.common.exception.InternalException;
import com.sktechx.godmusic.personal.common.exception.NotFoundException;
import com.sktechx.godmusic.personal.common.exception.ValidationException;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.MemberChannelDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistRetriveAllResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackCreateResponse;
import com.sktechx.godmusic.personal.rest.model.vo.myplaylist.MyPlaylistTrackRetrieveAllResponse;
import com.sktechx.godmusic.personal.rest.repository.MemberChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.MemberChannelTrackMapper;
import com.sktechx.godmusic.personal.rest.service.MemberChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

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
    AmqpService amqpService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Override
    @Transactional(readOnly = true)
    public MyPlaylistRetriveAllResponse getMemberChannelPageImpl(Long memberNo, Long characterNo, Long channelId, Pageable pageable) {
        List<Long> idList = memberChannelMapper.selectMemberChannelIdList(memberNo, characterNo, channelId, pageable);

        if (CollectionUtils.isEmpty(idList)) {
            throw new CommonBusinessException(CommonErrorMessage.EMPTY_DATA);
        }

        List<MemberChannelDto> list = memberChannelMapper.selectMemberChannelList(idList);
        int totalCount = memberChannelMapper.selectMemberChannelTotalCount(memberNo, characterNo);

        return new MyPlaylistRetriveAllResponse(new PageImpl<>(list, pageable, totalCount));
    }

    @Override
    @Transactional(readOnly = true)
    public MyPlaylistTrackRetrieveAllResponse getMemberChannelTrackList(Long memberNo, Long characterNo, Long memberChannelId, Pageable pageable) {
        List<TrackDto> list = memberChannelTrackMapper.selectMemberChannelTrackList(memberNo, characterNo, memberChannelId, pageable);

        if (CollectionUtils.isEmpty(list)) {
            throw new CommonBusinessException(CommonErrorMessage.EMPTY_DATA);
        }

        long totalCount = memberChannelTrackMapper.selectMemberChannelTrackListCount(memberNo, characterNo, memberChannelId);

        return new MyPlaylistTrackRetrieveAllResponse(new PageImpl<>(list, pageable, totalCount));
    }

    @Override
    @Transactional(readOnly = true)
    public MemberChannelDto getMemberChannel(Long memberNo, Long characterNo, Long memberChannelId) {
        return memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId);
    }

    @Override
    public MemberChannelDto createMemberChannel(Long memberNo, Long characterNo, String memberChannelName) {
        // 1000 채널 초과 체크
        if (memberChannelMapper.selectMemberChannelCount(memberNo, characterNo) >= FixedSize.MY_CHANNEL.getSize()) {
            throw new ValidationException(CommonErrorMessage.MY_CHANNEL_OVER_CREATE);
        }

        MemberChannelDto memberChannel = new MemberChannelDto();

        int duplicateChannelNameCount = memberChannelMapper.selectMemberChannelEqualsName(memberNo, characterNo, memberChannelName);

        if (duplicateChannelNameCount > 0) {
            String likeCondition = new String(memberChannelName).concat("(%)");
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
            throw new InternalException(CommonErrorMessage.INTERNAL_SERVER_ERROR);
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
            throw new ValidationException(CommonErrorMessage.MY_CHANNEL_DUPLICATED_NAME);
        }

        memberChannelMapper.updateMemberChannel(memberNo, characterNo, memberChannelId, memberChannelName);
    }

    @Override
    public MemberChannelDto removeTrackList(AppNameType appName, Long memberNo, Long characterNo, Long memberChannelId, List<Long> trackIdList) {

        if (ObjectUtils.isEmpty(memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId))) {
            throw new NotFoundException(CommonErrorMessage.MY_CHANNEL_NOT_FOUND);
        }

        memberChannelTrackMapper.deleteTrack(memberChannelId, trackIdList);

        // 사용자 이벤트 전송
        for(Long trackId : trackIdList){
            UserEvent userEvent = UserEvent.newBuilder()
                    .setPlayChnl(appName)
                    .setEvent(UserEventType.UNPICK)
                    .setMemberNo(characterNo)
                    .setTargetId(trackId)
                    .setTargetType(UserEventTarget.TRACK)
                    .build();
            amqpService.deliverUserEvent(userEvent);
        }

        // 앨범 아이디 및 trackCount 업데이트
        Long albumId = getMemberChannelAlbumId(memberChannelId);
        memberChannelMapper.updateMemberChannelList(memberNo, characterNo, memberChannelId, null, albumId, true, true, new Date());

        return memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId);
    }

    @Override
    public MyPlaylistTrackCreateResponse addTrackList(AppNameType appName, Long memberNo, Long characterNo, Long memberChannelId, List<Long> trackIdList){

        if(ObjectUtils.isEmpty(memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId))){
            throw new NotFoundException(CommonErrorMessage.MY_CHANNEL_NOT_FOUND);
        }

        int trackTotalCnt = memberChannelTrackMapper.selectTrackTotalCount(memberChannelId);
        if(trackTotalCnt >= FixedSize.MY_CHANNEL_TRACK.getSize()
                || (trackTotalCnt + trackIdList.size()) > FixedSize.MY_CHANNEL_TRACK.getSize()){
            throw new ValidationException(CommonErrorMessage.MY_CHANNEL_TRACK_OVER_ADD);
        }

        List<Long> duplicateKeyIdList = new ArrayList<>();
        List<Long> dataIntegrityIdList = new ArrayList<>();
        List<Long> successfulIdList = new ArrayList<>();

        int maxViewPriority = memberChannelTrackMapper.selectMaxTrackViewPriority(memberChannelId);
        AtomicInteger viewPriority = new AtomicInteger(maxViewPriority+1);

        for(Long trackId : trackIdList){
            try{
                memberChannelTrackMapper.insertTrackMemberChannel(memberChannelId, trackId, viewPriority.getAndIncrement());
                successfulIdList.add(trackId);

                // 사용자 이벤트 전송
                UserEvent userEvent = UserEvent.newBuilder()
                        .setPlayChnl(appName)
                        .setEvent(UserEventType.PICK)
                        .setMemberNo(memberNo)
                        .setCharactorNo(characterNo)
                        .setTargetId(trackId)
                        .setTargetType(UserEventTarget.TRACK)
                        .build();
                amqpService.deliverUserEvent(userEvent);
            }catch (Exception e){
                // 중복 등록 - 키 중복 에러로 체크
                if(e instanceof DuplicateKeyException){
                    duplicateKeyIdList.add(trackId);
                }
                // 비존재 트랙(실제로는 Meta 비동기 상태이지만 권리사 미제공으로 정리) - 외래키 에러로 체크
                else if(e instanceof DataIntegrityViolationException){
                    dataIntegrityIdList.add(trackId);
                }
            }
        }

        //앨범 아이디 및 trackCount 업데이트
        Long albumId = getMemberChannelAlbumId(memberChannelId);
        memberChannelMapper.updateMemberChannelList(memberNo, characterNo, memberChannelId, null, albumId, true, true, new Date());
        //TODO 업데이트 한 member channel의 sn 순서를 1로 변경 해준다.

        MemberChannelDto myChannel = getMemberChannel(memberNo, characterNo, memberChannelId);

        return MyPlaylistTrackCreateResponse.builder()
                .memberChannel(myChannel)
                .successfulCnt(successfulIdList.size())
                .duplicatedYn(duplicateKeyIdList.size() > 0 ? YnType.Y : YnType.N)
                .agencyCancelYn(dataIntegrityIdList.size() > 0 ? YnType.Y : YnType.N)
                .build();
    }

    @Override
    public MemberChannelDto modifyTrackList(Long memberNo, Long characterNo, Long memberChannelId, List<Long> modifyTrackIdList){
        if(ObjectUtils.isEmpty(memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId))){
            throw new NotFoundException(CommonErrorMessage.MY_CHANNEL_NOT_FOUND);
        }

        List<Long> memberChannelTrackIdList = memberChannelTrackMapper.selectMemberChannelTrackIdList(memberNo, characterNo, memberChannelId);
        int memberChannlAllTrackIdCount = memberChannelTrackIdList.size();
        int modifyTrackIdCount = modifyTrackIdList.size();

        //같으면 전체 업데이트, 다르면 변경된 부분만 잘라서 업데이트
        List<Long> remainderTrackIdList = memberChannelTrackIdList.subList(modifyTrackIdCount, memberChannlAllTrackIdCount);
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
            throw new InternalException(CommonErrorMessage.INTERNAL_SERVER_ERROR);
        }
        // 첫번째 이미지 트랙으로 채널 이미지 변경
        memberChannelMapper.updateMemberChannelImg(memberChannelId, 1);

        return memberChannelMapper.selectMemberChannel(memberNo, characterNo, memberChannelId);
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
}