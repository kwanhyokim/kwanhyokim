/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import java.util.Date;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.*;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import lombok.Data;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 정덕진(Deockjin Chung)/Music사업팀/SKTECH(djin.chung@sk.com)
 * @date 2018.07.03
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(value = {"id","name"}, alphabetic = true)
public class RecommendPanelTrackDto {
    private Long memberNo;

    private Integer listenCount;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date lastListenDateTime;

    private YnType likeYn;

    @JsonProperty("id")
    private Long trackId;
    @JsonProperty("name")
    private String trackName;

    private String playTime;

    private YnType adultAuthYn;

    private String subtractQty;

    private YnType holdbackYn;

    private YnType displayYn;

    private YnType titleYn;

    @JsonProperty("priority")
    private Integer trackSn;


    private Long agencyId;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ArtistDto> artistList;

    @JsonProperty("representationArtist")
    private ArtistDto artist;

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date createDateTime;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    private Date updateDateTime;

    private AlbumDto album;

    private YnType svcStreamingYn;

    private YnType svcDrmYn;

    private YnType freeYn;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<RecommendPanelTrackArtistDto> trackArtistList;

    public ArtistDto getArtist(){
        if(this.artist != null){
            return artist;
        }else{
            if(!CollectionUtils.isEmpty(artistList)){
                return artistList.get(0);
            }
        }
        return null;
    }
}
