package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import java.util.Date;
import java.util.List;

import com.sktechx.godmusic.personal.rest.model.dto.ServiceGenreDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RecommendForMeDto implements RecommendDto{

    private Long rcmmdMforuId;
    private String svcGenreNm;
    private Date dispStdStartDt;
    private Date createDtime;

    private int trackCount;
    private List<Long> trackIdList;
    private List<TrackDto> trackDtoList;
    private ServiceGenreDto svcGenreDto;

    public static RecommendForMeDto from (RecommendTrackDto recommendTrackDto){
        return RecommendForMeDto.builder()
                .rcmmdMforuId(recommendTrackDto.getRcmmdId())
                .createDtime(recommendTrackDto.getRcmmdCreateDtime())
                .dispStdStartDt(recommendTrackDto.getDispStdStartDt())
                .trackDtoList(recommendTrackDto.getTrackList())
                .trackCount(recommendTrackDto.getTrackCount())
                .svcGenreDto(recommendTrackDto.getSvcGenreDto())
                .svcGenreNm(recommendTrackDto.getSvcGenreDto().getSvcGenreNm())
                .build();
    }
}
