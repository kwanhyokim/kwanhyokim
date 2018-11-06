package com.sktechx.godmusic.personal.rest.service;

import com.sktechx.godmusic.lib.domain.GMContext;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenRequest;
import com.sktechx.godmusic.personal.rest.model.vo.listen.ListenTrackRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 8.
 * @time PM 7:26
 */
public interface ListenService {
	void addListenHistByChannel(ListenRequest request, Long memberNo, Long characterNo);

	void addListenHistByTrack(ListenTrackRequest request, GMContext currentContext, HttpServletRequest httpServletRequest);
}
