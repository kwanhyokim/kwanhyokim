package com.sktechx.godmusic.personal.rest.model.vo.listen;

import java.util.List;
import javax.validation.constraints.NotNull;

import lombok.Data;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time PM 7:18
 */
@Data
public class ListenDeleteRequest {
	@NotNull
	private List<ListenRequest> listenRequests;
}
