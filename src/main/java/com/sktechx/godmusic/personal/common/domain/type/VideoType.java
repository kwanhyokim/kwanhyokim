package com.sktechx.godmusic.personal.common.domain.type;

import org.apache.ibatis.type.MappedTypes;

import com.fasterxml.jackson.annotation.JsonValue;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnum;
import com.sktechx.godmusic.lib.mybatis.code.CodeEnumTypeHandler;

public enum VideoType implements CodeEnum {

    MV {
        @Override
        public String getValue() {
            return "뮤직비디오";
        }
    },
    TEASER {
        @Override
        public String getValue() {
            return "티저 영상";
        }
    },
    LIVE {
        @Override
        public String getValue() {
            return "라이브 영상";
        }
    },
    INTERVIEW {
        @Override
        public String getValue() {
            return "인터뷰, 토크 영상";
        }
    },
    ETC {
        @Override
        public String getValue() {
            return "기타 영상";
        }
    }
    ;

    @Override
    @JsonValue
    public String getCode() {
        return this.name();
    }

    @Override
    public CodeEnum getDefault() {
        return null;
    }

    @MappedTypes(VideoType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<VideoType> {
        public TypeHandler() {
            super(VideoType.class);
        }
    }

    public abstract String getValue();

}