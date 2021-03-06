package com.sktechx.godmusic.personal.rest.model.vo.like;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.personal.rest.model.dto.AlbumDto;
import com.sktechx.godmusic.personal.rest.model.dto.TrackDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 9. 3.
 * @time PM 7:05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({"totalCount", "currentPage", "lastPageYn", "list"})
public class LikeAlbumListResponse {
	private long totalCount;
	private int currentPage;
	private YnType lastPageYn;
	private List<AlbumDto> list = Collections.emptyList();

	public LikeAlbumListResponse(Page<AlbumDto> page){
		this.totalCount = page.getTotalElements();
		this.currentPage = page.getNumber() + 1;
		this.lastPageYn = page.isLast() ? YnType.Y : YnType.N;
		this.list = page.getContent();
	}
}
