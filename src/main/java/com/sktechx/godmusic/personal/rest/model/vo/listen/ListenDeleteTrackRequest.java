package com.sktechx.godmusic.personal.rest.model.vo.listen;

import java.util.Collections;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time AM 10:25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ListenDeleteTrackRequest {

	@NotEmpty
	@ApiModelProperty(name = "trackId", value = "곡 ID")
	private List<Long> trackIds = Collections.emptyList();

}
