package com.sktechx.godmusic.personal.rest.model.vo;

import org.springframework.lang.Nullable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sktechx.godmusic.lib.domain.code.OsType;
import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 10.
 * @time PM 4:11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ImageInfo implements Comparable<ImageInfo> {
	private Long size;
	private String url;

	private OsType osType;
	private Integer dispSn;
	public ImageInfo() {

	}
	public ImageInfo(Long size , String url){
		this.size = size;
		this.url = url;
	}

	@Override
	public int compareTo(@Nullable ImageInfo o) {

		if (o == null) return -1;

		long thisSize = size == null ? Long.MIN_VALUE : size;
		long otherSize = o.size == null ? Long.MIN_VALUE : o.size;

		return Long.compare(thisSize, otherSize);
	}
}
