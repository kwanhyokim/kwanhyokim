package com.sktechx.godmusic.personal.rest.model.vo.like;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.base.Enums;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 7. 31.
 * @time PM 3:56
 */
@Data
public class LikeRequest {

	public enum LikeType implements CodeEnum {

		ALBUM {
			@Override
			public String getCode() {
				return this.name();
			}
		},

		ARTIST {
			@Override
			public String getCode() {
				return this.name();
			}
		},

		TRACK {
			@Override
			public String getCode() {
				return this.name();
			}
		};

		public static LikeType fromValue(String code) {
			return Arrays.stream(LikeType.values())
					.filter(e -> e.getCode().equals(code))
					.findFirst().orElse(null);
		}

		public static boolean contains(String code) {
			return Arrays.stream(LikeType.values())
					.anyMatch(e -> e.getCode().equals(code));
		}

		@Override
		public CodeEnum getDefault() {
			return null;
		}
	}

	@Length(max = 10)
	@NotBlank
	@ApiModelProperty(name = "likeType", value = "좋아하는 타입(CHNL: 채널, ALBUM: 앨범, CHART: 차트, ARTIST: 아티스트, TRACK: 곡)",
			allowableValues = "CHNL, ALBUM, CHART, ARTIST, TRACK, FLAC")
	private String likeType;

	@Max(Long.MAX_VALUE)
	@NotNull
	@ApiModelProperty(name = "likeTypeId", value = "좋아하는 타입에 맞는 ID")
	private Long likeTypeId;

	public boolean typeIsTrackArtistAlbum() {
		return LikeType.contains(this.likeType);
	}

}
