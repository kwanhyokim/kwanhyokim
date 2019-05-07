/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.common.util;

import java.io.Serializable;
import java.util.Comparator;

/**
 * A Comparator for Boolean objects that can sort either true or false first.
 *
 * @author Keith Donald
 * @since 1.2.2
 */
public final class BooleanComparator implements Comparator, Serializable {

	/**
	 * A shared default instance of this comparator, treating true lower
	 * than false.
	 */
	public static final BooleanComparator TRUE_LOW = new BooleanComparator(true);

	/**
	 * A shared default instance of this comparator, treating true higher
	 * than false.
	 */
	public static final BooleanComparator TRUE_HIGH = new BooleanComparator(false);


	private final boolean trueLow;


	/**
	 * Create a BooleanComparator that sorts boolean values based on
	 * the provided flag.
	 * <p>Alternatively, you can use the default shared instances:
	 * <code>BooleanComparator.TRUE_LOW</code> and
	 * <code>BooleanComparator.TRUE_HIGH</code>.
	 * @param trueLow whether to treat true as lower or higher than false
	 * @see #TRUE_LOW
	 * @see #TRUE_HIGH
	 */
	public BooleanComparator(boolean trueLow) {
		this.trueLow = trueLow;
	}


	public int compare(Object o1, Object o2) {
		boolean v1 = ((Boolean) o1).booleanValue();
		boolean v2 = ((Boolean) o2).booleanValue();
		return (v1 ^ v2) ? ((v1 ^ this.trueLow) ? 1 : -1) : 0;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BooleanComparator)) {
			return false;
		}
		return (this.trueLow == ((BooleanComparator) obj).trueLow);
	}

	public int hashCode() {
		return (this.trueLow ? -1 : 1) * getClass().hashCode();
	}

	public String toString() {
		return "BooleanComparator: " + (this.trueLow ? "true low" : "true high");
	}

}
