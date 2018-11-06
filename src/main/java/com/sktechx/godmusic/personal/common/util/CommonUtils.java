package com.sktechx.godmusic.personal.common.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 3.
 * @time AM 10:59
 */
public class CommonUtils {

	public static Boolean empty(Object obj) {
		if (obj == null) return true;
		else if (obj instanceof String) return "".equals(obj.toString().trim());
		else if (obj instanceof List) return ((List) obj).isEmpty();
		else if (obj instanceof Map) return ((Map) obj).isEmpty();
		else if (obj instanceof Object[]) return Array.getLength(obj) == 0;

		return false;
	}

	public static Boolean notEmpty(Object obj) {
		return !empty(obj);
	}

	/**
	 * 입력된 길이만큼 랜덤 문자 가져오기
	 * default론 1개 반환
	 * @param randomLen
	 * @return
	 */
	public static String getRandomStr(int randomLen){
		if(randomLen < 1){
			randomLen = 1;
		}

		String targetStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

		Random ran = new Random();
		StringBuffer randomStr = new StringBuffer();
		int strLen = targetStr.length() - 1;

		for(int i=0; i<randomLen; i++){
			int randomIdx = ran.nextInt(strLen);
			randomStr.append(targetStr.substring(randomIdx, randomIdx+1));
		}

		return randomStr.toString();
	}

}
