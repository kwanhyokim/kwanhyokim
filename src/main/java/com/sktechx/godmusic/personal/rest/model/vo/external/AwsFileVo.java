package com.sktechx.godmusic.personal.rest.model.vo.external;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AwsFileVo {
    private String bucket;
    private String bucketKey;
}
