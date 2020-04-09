package com.sktechx.godmusic.personal.rest.model.vo.aflo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewMigrateAFloCharacterRes {

    private List<CopyChnlHistory> copyChnlHistoryList = new ArrayList<>();

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CopyChnlHistory {
        private Long fromChnlId;
        private Long toChnlId;
    }
}
