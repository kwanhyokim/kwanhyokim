/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.model.dto.recommend;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.util.ObjectUtils;

/**
 * 설명 : Json list 형식 응답용
 *
 * @author 김관효(Kwanhyo Kim)/Music사업팀/SKTECH(kwanhyo.kim@sk.com)
 * @date 2018. 8. 1.
 */

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListDto<T> {

	private T list;

	public ListDto(T list){
		this.list = list;
	}

	public boolean isNotEmpty() {
		return !ObjectUtils.isEmpty(list);
	}
}
