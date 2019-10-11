/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */
package com.sktechx.godmusic.personal.common.domain;

import com.sktechx.godmusic.lib.domain.code.YnType;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

/**
 * 설명 : 공통 List 응답
 * 
 *        totalCount : 총 리스트 개수
 *        currentPage : 현재 페이지
 *        lastPageYn : 마지막 페이지 여부
 *        list : 리스트 데이타
 *        
 * @author 오경무/Platform개발2팀/SKTECH(km.oh@sk.com)
 * @date 2017. 6. 22.
 *
 */
@Data
public class ListResponse {
	private long totalCount;
	private int currentPage;
	private YnType lastPageYn;
	private List<?> list = Collections.emptyList();
	
	public ListResponse(Page<?> page){
		this.totalCount = page.getTotalElements();
		this.currentPage = page.getNumber()+1;
		this.lastPageYn = page.isLast() ? YnType.Y : YnType.N;
		this.list = page.getContent();
	}
	
	public ListResponse(long totalCount, int currentPage, YnType lastPageYn, List<?> list){
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		this.lastPageYn = lastPageYn;
		this.list = list;
	}

	public static ListResponse of(Page<?> page) {
		return new ListResponse(page);
	}
	
}
