package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import lombok.Data;

import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 22.
 * @time PM 3:01
 */
@Data
public class PreferGenreTrackDto {
	private Long svcGenreId;
	private Long trackId;
	private List<Long> similarTrackIds;
}
