package com.sktechx.godmusic.personal.common.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

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

}
