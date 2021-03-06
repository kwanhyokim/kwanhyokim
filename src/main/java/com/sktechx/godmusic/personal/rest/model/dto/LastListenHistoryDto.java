package com.sktechx.godmusic.personal.rest.model.dto;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.*;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.08.16
 */

@Data
@Builder
@EqualsAndHashCode(of = "listenId")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"type", "id", "name", "trackCount", "updatedTrackCount", "lastListenDateTime", "imgList", "artistName"})
@ApiModel(value = "최근들은 컨텐츠")
public class LastListenHistoryDto {

    @JsonIgnore
    private String listenId;

    @ApiModelProperty(required = true, example = "MY", value = "컨텐츠 타입", notes = "MY:MY플레이리스트, DJ:DJ채널, ALBUM:앨범")
    @JsonProperty("type")
    private String listenType;

    @ApiModelProperty(required = true, example = "1", value = "아이디")
    @JsonProperty("id")
    private Long listenTypeId;

    @ApiModelProperty(required = true, example = "월간 인기 차트", value = "제목")
    @JsonProperty("name")
    private String contentTitle;

    private Long trackCount;

    @ApiModelProperty(required = true, example = "2018-07-16 13:52:23", value = "최근 들은 시간")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", timezone="Asia/Seoul")
    @JsonProperty("lastListenDateTime")
    private Date lastListenDtime;

    @ApiModelProperty(value = "이미지 리스트")
    private List<ImageInfo> imgList;

    @ApiModelProperty(example = "윤종신", value = "아티스트 명(앨범일 경우만 데이터 존재)")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AlbumDto album;

    @JsonIgnore
    private Integer renewTrackCnt;

    @JsonIgnore
    private Date renewDtime;

    @JsonIgnore
    private ImageInfo channelImage;

    @ApiModelProperty(value = "업데이트 여부")
    public YnType getRenewYn(){

        if(!"CHNL".equals(listenType)) return null;

        if(renewDtime == null) return YnType.N;

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE , -1);
        if(renewDtime.after(c.getTime())) return YnType.Y;
        else return YnType.N;

    }

    @ApiModelProperty(value = "업데이트 된 트랙 수")
    @JsonProperty("renewTrackCount")
    public Integer getRenewTrackCnt(){
        if(YnType.Y.equals(getRenewYn())){
            return this.renewTrackCnt;
        }else{
            return 0;
        }
    }

    public void setImgList(List<ImageInfo> imgList) {

        if (imgList != null) {
            imgList.sort(null);
        }

        this.imgList = imgList;
    }

    public void replacePlayListImageIfRepImageExists() {
        if (channelImage != null) {
            imgList = ImageInfoExpander.expandChannelImageByResizeOption(channelImage);
        }
    }
}
