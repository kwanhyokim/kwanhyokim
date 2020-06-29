package com.sktechx.godmusic.personal.rest.client.model;

import java.util.List;

import lombok.Getter;

/**
 * 설명 : XXXXXXXXXXX
 *
 * @author groot
 * @since 2019. 11. 20
 */
@Getter
public class GetTrackListRequest {

    private List<Long> trackIds;

    public GetTrackListRequest() {}

    public GetTrackListRequest(List<Long> trackIds) {
        this.trackIds = trackIds;
    }

}
