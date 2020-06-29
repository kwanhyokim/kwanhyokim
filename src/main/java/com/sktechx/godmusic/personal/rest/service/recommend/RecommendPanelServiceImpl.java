/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.recommend;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.collections4.ListUtils;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.mybatis.autoconfigure.MyBatisDatasourceConfig;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.type.ArtistType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.RecommendPanelResponse;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.assembly.v2.AfloPanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import com.sktechx.godmusic.personal.rest.service.recommend.read.RcmmdReadServiceFactory;
import lombok.extern.slf4j.Slf4j;


/**
 * 설명 : 추천 패널 데이터 생성
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Service("recommendPanelService")
@Slf4j
public class RecommendPanelServiceImpl implements RecommendPanelService {

    private final SqlSessionTemplate sqlSessionTemplate;

    private final PersonalRecommendPhaseService personalRecommendPhaseService;

    private final RecommendPanelAssemblyFactory recommendPanelAssemblyFactory;

    private final RecommendMapper recommendMapper;
    private final RecommendReadMapper recommendReadMapper;

    private final AfloPanelAssembly afloPanelAssembly;

    @Value("${personal.prefer.artist.panel.addPreferArtistPanel.instrumentalTrackRegexPattern}")
    private String instrumentalTrackRegexPattern;

    private final RedisService redisService;

    private final RcmmdReadServiceFactory rcmmdReadServiceFactory;

    @Autowired
    public RecommendPanelServiceImpl(
            AfloPanelAssembly afloPanelAssembly,
            RecommendReadMapper recommendReadMapper,
            RecommendMapper recommendMapper,
            RecommendPanelAssemblyFactory recommendPanelAssemblyFactory,
            PersonalRecommendPhaseService personalRecommendPhaseService,
            SqlSessionTemplate sqlSessionTemplate,
            RedisService redisService,
            RcmmdReadServiceFactory rcmmdReadServiceFactory
    ) {

        this.afloPanelAssembly = afloPanelAssembly;
        this.recommendReadMapper = recommendReadMapper;
        this.recommendMapper = recommendMapper;
        this.recommendPanelAssemblyFactory = recommendPanelAssemblyFactory;
        this.personalRecommendPhaseService = personalRecommendPhaseService;
        this.sqlSessionTemplate = sqlSessionTemplate;
        this.redisService = redisService;
        this.rcmmdReadServiceFactory = rcmmdReadServiceFactory;
    }

    @Override
    public List<Panel> createRecommendPanelList(Long characterNo , OsType osType, String appVer) {
        List<Panel> panelList = null;
        PersonalPhaseMeta personalPhaseMeta = null;
        PanelAssembly panelAssembly = null;
        try{

            personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType, appVer);
            panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly(personalPhaseMeta.getFirstPhaseType());
            panelList = panelAssembly.makeHomePanelListForMainTop(personalPhaseMeta);

            if(!ObjectUtils.isEmpty(personalPhaseMeta.getAfloCharacterExpireDtime())){
                List<Panel> afloPanelList =  afloPanelAssembly.appendAfloChannelPanelList(
                        personalPhaseMeta.getCharacterNo(), personalPhaseMeta.getOsType(),2);

                if(!CollectionUtils.isEmpty(afloPanelList)){
                    panelList = ListUtils.union(afloPanelList.stream().limit(2).collect(Collectors.toList()), panelList);
                }
            }

        }catch(CommonBusinessException cbex){
            log.error("createRecommendPanel business exception : {}", cbex.getDisplayMessage());
        }catch(Exception ex){
            ex.printStackTrace();
            log.error("createRecommendPanel not catched exception : {}",ex.getMessage());
        }finally{
            if(CollectionUtils.isEmpty(panelList)){
                if(panelAssembly == null)
                    panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly();
                try{
                    panelList = panelAssembly.makeHomePanelListForMainTop(personalPhaseMeta);
                }catch(Exception e){
                    log.error("createRecommendPanel recovery not catched exception : {}",e.getMessage());
                }
            }
        }

        return panelList;
    }

    @Override
    public RecommendPanelResponse createRecommendV2PanelList(Long characterNo, OsType osType, String appVer) {

        List<Panel> recommendPanelList = null;
        PersonalPhaseMeta personalPhaseMeta = null;
        PanelAssembly panelAssembly = null;

        try{
            personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType, appVer);
            panelAssembly = recommendPanelAssemblyFactory.getV2RecommendPanelAssembly(personalPhaseMeta);
            recommendPanelList = panelAssembly.makeHomePanelListForMainTop(personalPhaseMeta);

        }catch(CommonBusinessException cbex){
            if(cbex.getErrorDomain() == PersonalErrorDomain.HOME_PANNEL_CREATION_FAILED){

                log.error("createRecommendPanelV2 home panel creation failed : {} {}",
                        characterNo, cbex.getExtraAction());
                personalPhaseMeta =
                        personalRecommendPhaseService.getPersonalRecommendPhaseMetaExcept(characterNo
                                , osType, appVer, (RecommendPanelContentType)cbex.getExtraAction());
                panelAssembly = recommendPanelAssemblyFactory.getV2RecommendPanelAssembly(personalPhaseMeta);
                recommendPanelList = panelAssembly.makeHomePanelListForMainTop(personalPhaseMeta);
            }else {
                log.error("createRecommendPanelV2 business exception : {}", cbex.getDisplayMessage());
            }

        }catch(Exception ex){
            log.error("createRecommendPanelV2 not catched exception : {}", ex.getMessage());
        }finally{
            if(CollectionUtils.isEmpty(recommendPanelList)){
                // 추천 패널 응답이 비어서 내려온 경우, 비정상 상황으로 판단하여 비로그인 패널로 생성
                try{
                    recommendPanelList = recommendPanelAssemblyFactory.getRecommendPanelAssembly().makeHomePanelListForMainTop(personalPhaseMeta);

                }catch(Exception e){
                    e.printStackTrace();
                    log.error("createRecommendPanelV2 recovery not catched exception : {}",e.getMessage());
                }
            }

            if(!CollectionUtils.isEmpty(recommendPanelList)){
                recommendPanelList.forEach(panel -> {
                    if (!ObjectUtils.isEmpty(panel.getContent())) {
                        panel.getContent().setOsType(osType);
                    }
                });
            }
        }

        return new RecommendPanelResponse(
                Optional.ofNullable(
                        recommendPanelList
                ).orElseThrow( () -> new CommonBusinessException(CommonErrorDomain.EMPTY_DATA))
        );
    }

    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelTrackList(
            Long characterNo, RecommendPanelContentType recommendPanelContentType,
            Long panelContentId) {

        return new ListDto<>(
                rcmmdReadServiceFactory.getRcmmdReadService(recommendPanelContentType)
                .getRecommendTrackListByCharacterNoAndRcmmdId(characterNo, panelContentId)
        );
    }

    @Override
    @Transactional(MyBatisDatasourceConfig.SERVICE_SQL_TRANSACTION_BEAN_NAME)
    public void addPreferArtistPanel(Long characterNo) {
        List<CharacterPreferArtistGenreDto> characterPreferArtistGenreDtos = recommendReadMapper.selectCharacterPreferArtistGenre(characterNo);

        if (CollectionUtils.isEmpty(characterPreferArtistGenreDtos)) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        characterPreferArtistGenreDtos.sort(new GenreCountCompare());

        List<CharacterPreferArtistGenreDto> genreDtos = new ArrayList<>();
        int genreCnt = 0;

        for (CharacterPreferArtistGenreDto c : characterPreferArtistGenreDtos) {
            if (genreCnt == 0) genreCnt = c.getGenreCnt();
            if (genreCnt > c.getGenreCnt()) break;
            if (genreCnt == c.getGenreCnt()) genreDtos.add(c);
        }

        Collections.shuffle(genreDtos);

        // 캐릭터가 선정한 아티스트 목록
        List<CharacterPreferArtistDto> characterPreferArtistDtoList = recommendReadMapper.selectCharacterPreferArtist(characterNo, genreDtos.get(0).getGenreId());

        if (CollectionUtils.isEmpty(characterPreferArtistDtoList)) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        int count = 0;
        List<RecommendArtistListDto> recommendArtistListDto = new ArrayList<>();
        for (CharacterPreferArtistDto c : characterPreferArtistDtoList) {
            recommendArtistListDto.add(RecommendArtistListDto.builder().artistId(c.getArtistId()).artistType(ArtistType.REPRSNT).dispSn(count++).build());
        }

        List<Long> ids = recommendArtistListDto.stream().map(RecommendArtistListDto::getArtistId).collect(Collectors.toList());

        // 부족한 아티스트는 유사 아티스트 랜덤하게 추가
        if (recommendArtistListDto.size() > 2 && recommendArtistListDto.size() < 5) // 선호 아티스트가 3, 4명일 경우 유사 아티스트로 5명까지 채움
            fillSimilarArtist(count, recommendArtistListDto, ids);

        // 선호 아티스트가 3~5명 까지는 명당 2 명씩 추가 나머진 5명씩 추가
        count = 2;
        if (characterPreferArtistDtoList.size() < 3) count = 5;

        // 정책에 따른 유사 아티스트 추가
        addSimilarArtist(count, recommendArtistListDto, ids);

        // 전체 곡 각 2곡씩 꺼내기
        List<RecommendArtistTrackListDto> recommendArtistTrackListDto = recommendReadMapper.selectSimilarArtistTrack(ids);

        if (CommonUtils.empty(recommendArtistTrackListDto)) return;
        // 모든 곡의 아티스트가 연달아 나오지 않게 정렬
        notDuplicateList(recommendArtistTrackListDto);

        // added by Bob 2019.01.09
        // 연주곡 추천 제외 로직 추가
        recommendArtistTrackListDto = recommendArtistTrackListDto.stream().
                filter(x-> !(x.getTrackNm().matches(instrumentalTrackRegexPattern))).collect(Collectors.toList());

        if (CommonUtils.empty(recommendArtistTrackListDto)) {
            return;
        }

        // 기존 패널 종료시간을 지금시간으로 업데이트
        recommendMapper.updateRcmmdArtistDispStdEndDt(characterNo);
        RecommendArtistDto recommendArtistDto = RecommendArtistDto.builder()
                .characterNo(characterNo)
                .dispSn(1)
                .build();

        recommendMapper.insertRcmmdArtist(recommendArtistDto);

        Map<String, Object> batchParam = new HashMap<>();

        try(SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)){
            IntStream.range(0, recommendArtistListDto.size())
                    .forEach(index ->
                            {
                                batchParam.clear();
                                batchParam.put("rcmmdArtistId", recommendArtistDto.getRcmmdArtistId());
                                batchParam.put("artistId", recommendArtistListDto.get(index).getArtistId());
                                batchParam.put("artistType", recommendArtistListDto.get(index).getArtistType());
                                batchParam.put("dispSn", index);
                                log.info("recommendArtistListDto batchParam : " + batchParam.toString());
                                sqlSession.update("insertRcmmdArtistList", batchParam);
                            }
                    );

            List<RecommendArtistTrackListDto> finalRecommendArtistTrackListDto = recommendArtistTrackListDto;

            IntStream.range(0, recommendArtistTrackListDto.size())
                    .forEach(index ->
                            {
                                batchParam.clear();
                                batchParam.put("rcmmdArtistId", recommendArtistDto.getRcmmdArtistId());
                                batchParam.put("trackId", finalRecommendArtistTrackListDto.get(index).getTrackId());
                                batchParam.put("dispSn", index);
                                log.info("recommendArtistTrackListDto batchParam : " + batchParam.toString());
                                sqlSession.update("insertRcmmdArtistTrackList", batchParam);
                            }
                    );
            sqlSession.flushStatements();
            sqlSession.commit();
        } catch(Exception e) {
            e.printStackTrace();
            log.error("recommend :: recommend artist :: Error Message {}", e.getMessage());
            throw new CommonBusinessException(PersonalErrorDomain.PREFER_ARTIST_PANEL_FAIL);
        }
    }

    @Override
    @Transactional(MyBatisDatasourceConfig.SERVICE_SQL_TRANSACTION_BEAN_NAME)
    public void addPreferGenrePanel(Long characterNo) {
        // 캐릭터가 선정한 장르별 곡 목록
        List<PreferGenreTrackDto> preferGenreTrackDtoList = getPreferGenreTrackDtos(characterNo);

        List<Long> ids = preferGenreTrackDtoList.stream().map(PreferGenreTrackDto::getTrackId).collect(Collectors.toList());

        // 장르별 1곡에 대한 유사곡 조회
        List<SimilarTrackDto> similarTrackDtoList = getSimilarTrackDtoList(ids);

        // 기존 패널의 disp_std_end_dt 를 now() 로 업데이트
        recommendMapper.updateRcmmdPreferGenreSimilarTrack(characterNo);

        // 트랙 패널 생성
        List<RecommendPreferGenreSimilarTrackDto> recommendPreferGenreSimilarTrackDtoList = getRecommendPreferGenreSimilarTrackDtos(characterNo, preferGenreTrackDtoList);

        // 패널당 곡 리스트
        Map<String, Object> batchParam = new HashMap<>();
        try(SqlSession sqlSession = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false)){
            for (RecommendPreferGenreSimilarTrackDto r : recommendPreferGenreSimilarTrackDtoList) {
                for (SimilarTrackDto s : similarTrackDtoList) {
                    if (r.getTrackId().equals(s.getTrackId())) {
                        IntStream.range(0, s.getSimilarTrackIds().size())
                                .forEach(index ->
                                        {
                                            batchParam.clear();
                                            batchParam.put("rcmmdPreferGenreSimilarTrackId", r.getRcmmdPreferGenreSimilarTrackId());
                                            batchParam.put("trackId", s.getSimilarTrackIds().get(index));
                                            batchParam.put("dispSn", index);
                                            log.info("recommendPreferGenreSimilarTrackDtoList batchParam : " + batchParam.toString());
                                            sqlSession.update("insertRcmmdPreferGenreSimilarTrackList", batchParam);
                                        }
                                );
                    }
                }
            }
            sqlSession.flushStatements();
            sqlSession.commit();
        } catch(Exception e) {
            log.error("recommend :: recommend Genre :: Error Message {}", e.getMessage());
            throw new CommonBusinessException(PersonalErrorDomain.PREFER_GENRE_PANEL_FAIL);
        }
    }

    @Override
    public List<Panel> getRecommendPanelList(Long characterNo,
            RecommendPanelContentType recommendPanelType, OsType osType, String appVersion) {

        PanelAssembly panelAssembly = recommendPanelAssemblyFactory
                .getV2RecommendPanelAssembly(recommendPanelType);

        try {

            return Optional.ofNullable(
                    panelAssembly.makeHomePanelListForMainMiddle(characterNo, osType)
            )
                    .orElseGet(Collections::emptyList)
                    .stream()
                    .filter(panel ->
                            Optional.ofNullable(panel).isPresent() &&
                                    Optional.ofNullable(panel.getContent()).isPresent())
                    .collect(Collectors.collectingAndThen(
                            Collectors.toCollection(ArrayList::new),

                            panels -> {
                                for (Panel panel : panels) {
                                    panel.getContent().setOsType(osType);
                                }

                                return panels;
                            }

                    ))
                    ;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<RecommendPreferGenreSimilarTrackDto> getRecommendPreferGenreSimilarTrackDtos(Long characterNo, List<PreferGenreTrackDto> preferGenreTrackDtoList) {
        int dispSn = 0;
        List<RecommendPreferGenreSimilarTrackDto> recommendPreferGenreSimilarTrackDtoList = new ArrayList<>();
        // TB_RCMMD_PREFER_GENRE_SIMILAR_TRACK 에 넣을 객체
        for (PreferGenreTrackDto p : preferGenreTrackDtoList) {
            recommendPreferGenreSimilarTrackDtoList.add(RecommendPreferGenreSimilarTrackDto.builder().characterNo(characterNo)
                    .svcGenreId(p.getSvcGenreId()).dispSn(dispSn++).trackId(p.getTrackId()).build());
        }

        for (RecommendPreferGenreSimilarTrackDto r : recommendPreferGenreSimilarTrackDtoList) {
            recommendMapper.insertRcmmdPreferGenreSimilarTrack(r);
        }
        return recommendPreferGenreSimilarTrackDtoList;
    }

    private List<SimilarTrackDto> getSimilarTrackDtoList(List<Long> ids) {
        List<SimilarTrackDto> similarTrackDtoList = recommendReadMapper.selectSimilarTrackListByIdList(ids);

        similarTrackDtoList.removeIf((SimilarTrackDto s) -> CollectionUtils.isEmpty(s.getSimilarTrackIds()) || s.getSimilarTrackIds().size() < 15);

        if (CollectionUtils.isEmpty(similarTrackDtoList)) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        // 30개를 초과할경우를 제외해줌
        List<Long> similarTrackIds;
        for (SimilarTrackDto s : similarTrackDtoList) {
            similarTrackIds = new ArrayList<>();
            for (int i = 0; i < s.getSimilarTrackIds().size(); i++) {
                if (i < 30) {
                    similarTrackIds.add(s.getSimilarTrackIds().get(i));
                }
            }
            s.setSimilarTrackIds(similarTrackIds);
        }
        return similarTrackDtoList;
    }

    private List<PreferGenreTrackDto> getPreferGenreTrackDtos(Long characterNo) {
        List<PreferGenreTrackDto> metaPreferGenreTrackDtoList = recommendReadMapper.selectPreferGenreTrack(characterNo);

        if (CollectionUtils.isEmpty(metaPreferGenreTrackDtoList)) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        // 장르별 1곡씩 꺼내기
        Long svcGenreId = -1L;
        List<PreferGenreTrackDto> preferGenreTrackDtoList = new ArrayList<>();
        for (PreferGenreTrackDto p : metaPreferGenreTrackDtoList) {
            if (!svcGenreId.equals(p.getSvcGenreId())) {
                svcGenreId = p.getSvcGenreId();
                preferGenreTrackDtoList.add(p);
            }
        }
        return preferGenreTrackDtoList;
    }

    private void notDuplicateList(List<RecommendArtistTrackListDto> recommendArtistTrackListDto) {
        List<RecommendArtistTrackListDto> oddList = new ArrayList<>();
        List<RecommendArtistTrackListDto> evenList = new ArrayList<>();
        int forCount = 0;
        for (RecommendArtistTrackListDto t : recommendArtistTrackListDto) {
            if (forCount % 2 == 0) evenList.add(t);
            else oddList.add(t);
            forCount++;
        }
        Collections.shuffle(oddList);
        Collections.shuffle(evenList);

        if (!CollectionUtils.isEmpty(evenList)
                && oddList.get(oddList.size() - 1).getArtistId().equals(evenList.get(0).getArtistId())) {
            if (evenList.size() > 1) Collections.swap(evenList, 0, 1);
            else if (oddList.size() > 1) Collections.swap(oddList, oddList.size() - 1, oddList.size() - 2);
        }

        recommendArtistTrackListDto.clear();
        recommendArtistTrackListDto.addAll(oddList);
        recommendArtistTrackListDto.addAll(evenList);
    }

    private void addSimilarArtist(int count, List<RecommendArtistListDto> recommendArtistListDto, List<Long> ids) {
        List<SimilarArtistDto> similarArtistDtoList = recommendReadMapper.selectSimilarArtistByIdList(ids);
        Long beforeArtistId = null;
        int checkCount = 0;
        if (!CollectionUtils.isEmpty(similarArtistDtoList)) {
            Collections.shuffle(similarArtistDtoList);
            for (SimilarArtistDto similarArtistDto : similarArtistDtoList) {
                if (beforeArtistId == null)
                    beforeArtistId = similarArtistDto.getArtistId();
                if (!beforeArtistId.equals(similarArtistDto.getArtistId())) {
                    checkCount = 0;
                    beforeArtistId = similarArtistDto.getArtistId();
                }
                if (checkCount < count && !ids.contains(similarArtistDto.getSimilarArtistId())) {
                    recommendArtistListDto.add(RecommendArtistListDto.builder()
                            .artistId(similarArtistDto.getSimilarArtistId())
                            .artistType(ArtistType.SIMILAR).build());
                    ids.add(similarArtistDto.getSimilarArtistId());
                    checkCount++;
                }
            }
        }
    }

    private void fillSimilarArtist(int count, List<RecommendArtistListDto> recommendArtistListDto, List<Long> ids) {
        List<SimilarArtistDto> similarArtistList = recommendReadMapper.selectSimilarArtistByIdList(ids);

        if (!CollectionUtils.isEmpty(similarArtistList)) {
            Collections.shuffle(similarArtistList);
            for (SimilarArtistDto s : similarArtistList) {
                if (count++ > 4) break;

                recommendArtistListDto.add(RecommendArtistListDto.builder()
                        .artistId(s.getSimilarArtistId())
                        .artistType(ArtistType.SIMILAR).build());
                ids.add(s.getSimilarArtistId());
            }
        }
    }

    class GenreCountCompare implements Comparator<CharacterPreferArtistGenreDto> {
        @Override
        public int compare(CharacterPreferArtistGenreDto arg0, CharacterPreferArtistGenreDto arg1) {
            return Integer.compare(arg1.getGenreCnt(), arg0.getGenreCnt());
        }
    }

}