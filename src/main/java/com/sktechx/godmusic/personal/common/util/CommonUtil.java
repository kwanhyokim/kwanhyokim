package com.sktechx.godmusic.personal.common.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * Created by Kobe.
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 2.
 * @time PM 8:52
 */
public class CommonUtil {

	public static Boolean empty(Object obj) {
		if (obj instanceof String) return obj == null || "".equals(obj.toString().trim());
		else if (obj instanceof List) return obj == null || ((List) obj).isEmpty();
		else if (obj instanceof Map) return obj == null || ((Map) obj).isEmpty();
		else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
		else return obj == null;
	}

	public static Boolean notEmpty(Object obj) {
		return !empty(obj);
	}
}
