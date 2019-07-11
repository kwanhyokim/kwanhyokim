package com.sktechx.godmusic.personal.rest.service.impl;

import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.MemberChannelDto;
import com.sktechx.godmusic.personal.rest.repository.LikeMapper;
import com.sktechx.godmusic.personal.rest.repository.MemberChannelMapper;
import com.sktechx.godmusic.personal.rest.repository.MemberChannelTrackMapper;
import com.sktechx.godmusic.personal.rest.service.AFloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Slf4j
public class AFloServiceImpl implements AFloService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private MemberChannelMapper memberChannelMapper;

    @Autowired
    private MemberChannelTrackMapper memberChannelTrackMapper;


    @Override
    @Transactional
    public void migrateAFloCharacter(Long memberNo, Long fromCharacterNo, Long toCharacterNo){

        // 아티스트 좋아요
        int likeCount = likeMapper.countLikeByCharacterNo(toCharacterNo);
        if( likeCount == 0 )
            likeMapper.insertSelectLike(fromCharacterNo, toCharacterNo);

        // my chnl
        int chnlCount = memberChannelMapper.countMemberChannelIdList(memberNo, toCharacterNo);
        if( chnlCount == 0){
            List<Long> chnlIdList = memberChannelMapper.selectLimitedMemberChannelIdList(memberNo, fromCharacterNo);

            if(!ObjectUtils.isEmpty(chnlIdList)){
                List<MemberChannelDto> list = memberChannelMapper.selectMemberChannelList(chnlIdList);

                list.stream().forEach(item ->{
                    MemberChannelDto memberChannelDto = MemberChannelDto.builder()
                            .pinType(item.getPinType())
                            .pinTypeId(item.getPinTypeId())
                            .memberChannelName(item.getMemberChannelName())
                            .channelPriority(item.getChannelPriority())
                            .trackCount(item.getTrackCount())
                            .channelPlayTime(item.getChannelPlayTime())
                            .build();

                    if(!ObjectUtils.isEmpty(item.getAlbum())){
                        memberChannelDto.setAlbum(AlbumDto.builder().albumId(item.getAlbum().getAlbumId()).build());
                    }
                    memberChannelMapper.insertMemberChannel(memberNo, toCharacterNo, memberChannelDto);
                    memberChannelTrackMapper.insertSelectMemberChannelTrack(item.getMemberChannelId(), memberChannelDto.getMemberChannelId());
                });
            }

        }
    }
}
