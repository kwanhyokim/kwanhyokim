package com.sktechx.godmusic.personal.rest.model.dto.member;

import com.sktechx.godmusic.lib.domain.code.OsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDvcDto {
    private Long memberDvcNo;
    private Long memberNo;
    private Long characterNo;
    private String dvcId;
    private String dvcToken;
    private OsType osType;
    private String useAppNm;
    private String useAppVersion;
    private String clientId;
    private String dvcModelNm;
    private String dvcVersion;
}
