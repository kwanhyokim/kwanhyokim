package com.sktechx.godmusic.personal.common.domain.type;

import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import org.aspectj.apache.bcel.classfile.Code;

import java.time.DayOfWeek;
import java.util.Arrays;

/**
 * 설명 : XXXXXXXXX
 *
 * @author 남재우(Peter)/njw0619@sk.com
 * @date 2018.08.16
 */

public enum DayType implements CodeEnum{
    SUN("SUN", DayOfWeek.SUNDAY),
    MON("MON", DayOfWeek.MONDAY),
    TUE("TUE", DayOfWeek.TUESDAY),
    WED("WED", DayOfWeek.WEDNESDAY),
    THU("THU", DayOfWeek.THURSDAY),
    FRI("FRI", DayOfWeek.FRIDAY),
    SAT("SAT", DayOfWeek.SATURDAY);

    private String code;
    private DayOfWeek value;

    DayType(String code, DayOfWeek value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String getCode() {
        return code;
    }

    public static DayType findDayOfWeek(DayOfWeek dayOfWeek){
        return Arrays.stream(DayType.values())
                .filter(n -> n.getValue().equals(dayOfWeek))
                .findAny()
                .orElse(SUN);
    }

    public DayOfWeek getValue() {
        return value;
    }

    @Override
    public CodeEnum getDefault() {
        return null;
    }
}
