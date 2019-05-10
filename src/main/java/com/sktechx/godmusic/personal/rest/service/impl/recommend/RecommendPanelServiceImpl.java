/*
 * Copyright (c) 2018 SK TECHX.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SK TECHX.
 * You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SK TECHX.
 */

package com.sktechx.godmusic.personal.rest.service.impl.recommend;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.sktechx.godmusic.lib.domain.CommonApiResponse;
import com.sktechx.godmusic.lib.domain.code.OsType;
import com.sktechx.godmusic.lib.domain.code.YnType;
import com.sktechx.godmusic.lib.domain.exception.CommonBusinessException;
import com.sktechx.godmusic.lib.domain.exception.CommonErrorDomain;
import com.sktechx.godmusic.lib.mybatis.autoconfigure.MyBatisDatasourceConfig;
import com.sktechx.godmusic.lib.redis.service.RedisService;
import com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant;
import com.sktechx.godmusic.personal.common.domain.constant.RedisKeyConstant;
import com.sktechx.godmusic.personal.common.domain.type.ArtistType;
import com.sktechx.godmusic.personal.common.domain.type.RecommendPanelContentType;
import com.sktechx.godmusic.personal.common.exception.PersonalErrorDomain;
import com.sktechx.godmusic.personal.common.util.CommonUtils;
import com.sktechx.godmusic.personal.rest.model.dto.ArtistDto;
import com.sktechx.godmusic.personal.rest.model.dto.recommend.*;
import com.sktechx.godmusic.personal.rest.model.vo.ImageInfo;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.panel.Panel;
import com.sktechx.godmusic.personal.rest.model.vo.recommend.phase.PersonalPhaseMeta;
import com.sktechx.godmusic.personal.rest.repository.ArtistMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendMapper;
import com.sktechx.godmusic.personal.rest.repository.RecommendReadMapper;
import com.sktechx.godmusic.personal.rest.repository.TrackMapper;
import com.sktechx.godmusic.personal.rest.service.MetaApiProxy;
import com.sktechx.godmusic.personal.rest.service.recommend.RecommendPanelService;
import com.sktechx.godmusic.personal.rest.service.recommend.panel.PanelAssembly;
import com.sktechx.godmusic.personal.rest.service.recommend.phase.PersonalRecommendPhaseService;
import lombok.extern.slf4j.Slf4j;

import static com.sktechx.godmusic.personal.common.domain.constant.RecommendConstant.RCMMD_TRACK_PANEL_DETAIL_SUB_TITLE;

/**
 * 설명 : 추천 패널 데이터 생성
 *
 * @author 오경무/SKTECHX (km.oh@sk.com)
 * @date 2018. 07. 09.
 */
@Service
@Slf4j
public class RecommendPanelServiceImpl implements RecommendPanelService {

    private final int recommendPanelDefaultImageExpiredSec = 3600;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private ArtistMapper artistMapper;

    @Autowired
    private TrackMapper trackMapper;

    @Autowired
    private PersonalRecommendPhaseService personalRecommendPhaseService;

    @Autowired
    private RecommendPanelAssemblyFactory recommendPanelAssemblyFactory;

    @Autowired
    private RecommendMapper recommendMapper;
    @Autowired
    private RecommendReadMapper recommendReadMapper;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private RedisService redisService;

    @Autowired
    private MetaApiProxy metaApiProxy;

    @Value("${personal.prefer.artist.panel.addPreferArtistPanel.instrumentalTrackRegexPattern}")
    private String instrumentalTrackRegexPattern;

    @Override
    public List<Panel> createRecommendPanelList(Long characterNo , OsType osType) {
        List<Panel> panelList = null;
        PersonalPhaseMeta personalPhaseMeta = null;
        PanelAssembly panelAssembly = null;
        try{

            personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);
            panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly(personalPhaseMeta.getFirstPhaseType());
            panelList = panelAssembly.assembleRecommendPanel(personalPhaseMeta);

        }catch(CommonBusinessException cbex){
            log.error("createRecommendPanel business exception : {}", cbex.getDisplayMessage());
        }catch(Exception ex){
            log.error("createRecommendPanel not catched exception : {}",ex.getMessage());
        }finally{
            if(CollectionUtils.isEmpty(panelList)){
                if(panelAssembly == null)
                    panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly();
                try{
                    panelList = panelAssembly.assembleRecommendPanel(personalPhaseMeta);
                }catch(Exception e){
                    log.error("createRecommendPanel recovery not catched exception : {}",e.getMessage());
                }
            }
        }

        return panelList;
    }
    @Override
    public List<Panel> createRecommendV2PanelList(Long characterNo, OsType osType) {
        List<Panel> panelList = null;
        PersonalPhaseMeta personalPhaseMeta = null;
        PanelAssembly panelAssembly = null;
        try{

            personalPhaseMeta = personalRecommendPhaseService.getPersonalRecommendPhaseMeta(characterNo, osType);

            panelAssembly = recommendPanelAssemblyFactory.getRecommendV2PanelAssembly(personalPhaseMeta);

            panelList = panelAssembly.assembleRecommendPanel(personalPhaseMeta);

        }catch(CommonBusinessException cbex){
            log.error("createRecommendPanel business exception : {}", cbex.getDisplayMessage());
        }catch(Exception ex){
            log.error("createRecommendPanel not catched exception : {}",ex.getMessage());
        }finally{
            if(CollectionUtils.isEmpty(panelList)){
                if(panelAssembly == null)
                    panelAssembly = recommendPanelAssemblyFactory.getRecommendPanelAssembly();
                try{
                    panelList = panelAssembly.assembleRecommendPanel(personalPhaseMeta);
                }catch(Exception e){
                    log.error("createRecommendPanel recovery not catched exception : {}",e.getMessage());
                }
            }
        }

        return panelList;
    }

    private List<ImageInfo> makePanelBackGroundImageList(String url){
        List<ImageInfo> artistImgList = new ArrayList<>();
        ImageInfo image = new ImageInfo();
        image.setSize(new Long(75));
        image.setUrl(url);

        artistImgList.add(image);
        image = new ImageInfo();
        image.setSize(new Long(140));
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageInfo();
        image.setSize(new Long(200));
        image.setUrl(url);
        artistImgList.add(image);


        image = new ImageInfo();
        image.setSize(new Long(350));
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageInfo();
        image.setSize(new Long(500));
        image.setUrl(url);
        artistImgList.add(image);

        image = new ImageInfo();
        image.setSize(new Long(1000));
        image.setUrl(url);
        artistImgList.add(image);

        return artistImgList;
    }


    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelPopularTrackList(Long characterNo, Long rcmmdArtistId) {
        return getTrackList(trackMapper.selectRecommendPanelPopularTrackList(characterNo, rcmmdArtistId));
    }
    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelSimilarTrackList(Long characterNo, Long rcmmdTrackId) {
        return getTrackList(trackMapper.selectRecommendPanelSimilarTrackList(characterNo, rcmmdTrackId));
    }
    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelGenreTrackList(Long characterNo, Long rcmmdGenreId) {
        return getTrackList(trackMapper.selectRecommendPanelGenreTrackList(characterNo, rcmmdGenreId));
    }
    @Override
    public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelCfTrackList(Long characterNo, Long rcmmdMforuId) {
        return getTrackList(trackMapper.selectRecommendPanelCfTrackList(characterNo, rcmmdMforuId));
    }

	@Override
	public ListDto<List<RecommendPanelTrackDto>> getRecommendPanelTrackList(Long characterNo, RecommendPanelContentType recommendPanelContentType, Long panelContentId) {

		ListDto<List<RecommendPanelTrackDto>> trackList = null;

    	switch (recommendPanelContentType){
		    // 아티스트
		    case RC_ATST_TR:
			    trackList = getRecommendPanelPopularTrackList(characterNo, panelContentId);
			    break;
		    // 선호 유사
		    case RC_SML_TR:
			    trackList = getRecommendPanelSimilarTrackList(characterNo, panelContentId);
			    break;
		    // 유사 장르
		    case RC_GR_TR:
			    trackList = getRecommendPanelGenreTrackList(characterNo, panelContentId);
			    break;
		    // 추천
		    case RC_CF_TR:
			    trackList = getRecommendPanelCfTrackList(characterNo, panelContentId);
			    break;
	    }

		return trackList;
	}

	private List<ImageInfo> getRecommendPanelInfoBgImage(RecommendPanelContentType recommendPanelContentType,Long panelContentId, OsType osType , int dispSn){

        String imgUrl = recommendReadMapper.selectRecommendPanelInfoBgImageUrl(recommendPanelContentType, panelContentId, osType , dispSn);

        if(StringUtils.isEmpty(imgUrl)) {
            List<ImageInfo> imageInfoList = getRecommendPanelDefaultImageList(osType);

            imgUrl = imageInfoList.get(0).getUrl();
        }

        return makePanelBackGroundImageList(imgUrl);
    }

    @Override
    public RecommendPanelInfoDto getRecommendPanelInfo(Long characterNo, RecommendPanelContentType recommendPanelContentType,
            Long panelContentId, OsType osType) {

        RecommendPanelInfoDto panel = null;

        ListDto<List<RecommendPanelTrackDto>> trackList = getRecommendPanelTrackList(characterNo, recommendPanelContentType, panelContentId);

        int trackCount = 0;

        if( trackList != null && !CollectionUtils.isEmpty(trackList.getList())){
            trackCount = trackList.getList().size();
        }



        switch (recommendPanelContentType){
            // 아티스트
            case RC_ATST_TR:

                RecommendArtistDto recommendArtistDto= recommendReadMapper.selectRecommendArtistById(panelContentId);

                List<ArtistDto> artistDtoList;

                if(recommendArtistDto == null || CollectionUtils.isEmpty(recommendArtistDto.getArtistList())){
                    artistDtoList = artistMapper.getArtistList(Arrays.asList(12L,14L,15L));
                }else {
                    artistDtoList = recommendArtistDto.getArtistList();
                }

                List<String> artistNameList = artistDtoList.stream().map(x -> x.getArtistName()).limit(5).collect(
		                Collectors.toList());

                // 아티스트 이미지가 default 이미지인것은 목록에서 뒷 부분으로 위치를 변경한다
                List<ArtistDto> normalImageArtistList = new LinkedList<>();
                List<ArtistDto> defaultImageArtistList = new LinkedList<>();
                artistDtoList.stream().forEach(x ->  {
                    if( x.hasDefaultImage() ) {
                        defaultImageArtistList.add(x);
                    } else {
                        normalImageArtistList.add(x);
                    }
                });
                if(defaultImageArtistList.size() > 0)   {
                    normalImageArtistList.addAll(defaultImageArtistList);
                }

                // 아티스트의 첫 이미지를 배경 이미지로 사용
                panel =  new RecommendPanelInfoDto.Builder()
                        .title(RecommendConstant.ARTIST_PANEL_TITLE)
                        .subTitle(String.join(",", artistNameList))
                        .imgList((artistDtoList == null || artistDtoList.get(0) == null? null : artistDtoList.get(0).getImgList()))
                        .artistList(normalImageArtistList)
                        .artistCount(artistDtoList.size())
                        .newYn(YnType.Y)
                        .build();
                break;
            // 선호 유사
            case RC_SML_TR:

                List<RecommendTrackDto> similarTrackList =
                        recommendReadMapper.selectRecommendSimilarTrackListByIdList(Arrays.asList(panelContentId), 1,
                                1, osType);

                int dispSn = 1;
                if(!CollectionUtils.isEmpty(similarTrackList)){
                    RecommendTrackDto recommendTrackDto = similarTrackList.get(0);
                    if(recommendTrackDto != null){
                        dispSn = recommendTrackDto.getDispSn();
                    }
                }
                panel = new RecommendPanelInfoDto.Builder()
                        .title(RecommendConstant.SIMILAR_TRACK_PANEL_TITLE)
                        .subTitle(RecommendConstant.SIMILAR_TRACK_PANEL_DETAIL_SUB_TITLE)
                        .imgList(getRecommendPanelInfoBgImage(recommendPanelContentType, panelContentId, osType , dispSn))
                        .trackCount(trackCount)
                        .newYn(YnType.Y)
                        .renewDtime(new Date())
                        .build();
                break;
            // 유사 장르
            case RC_GR_TR:
                panel = new RecommendPanelInfoDto.Builder()
                        .title(RecommendConstant.PREFER_GENRE_SIMILAR_TRACK_PANEL_TITLE)
                        .subTitle(RecommendConstant.PREFER_GENRE_SIMILAR_TRACK_PANEL_DETAIL_SUB_TITLE)
                        .imgList(getRecommendPanelInfoBgImage(recommendPanelContentType, panelContentId, osType , 0) )
                        .trackCount(trackCount)
                        .newYn(YnType.Y)
                        .renewDtime(new Date())
                        .build();

                break;
            // 추천

            case RC_CF_TR:
                RecommendGenreVo recommendGenreVo = recommendReadMapper.selectRecommendGenreByRcmmdId(panelContentId);
                String genreNm = "";
                Date createDTime = new Date();
                YnType newYn = YnType.N;

                if(!ObjectUtils.isEmpty(recommendGenreVo)){
                    genreNm = recommendGenreVo.getSvcGenreNm();
                    createDTime = recommendGenreVo.getDispStdStartDt();

                    Date stdDate = new Date((System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)));
                    if(stdDate.before(createDTime)){
                        newYn = YnType.Y;
                    }
                }

                panel = new RecommendPanelInfoDto.Builder()
                        .title(RecommendConstant.RCMMD_TRACK_PANEL_TITLE)
                        .subTitle(String.format(RCMMD_TRACK_PANEL_DETAIL_SUB_TITLE,(genreNm)))
                        .imgList(getRecommendPanelInfoBgImage(recommendPanelContentType, panelContentId, osType , 0))
                        .trackCount(trackCount)
                        .newYn(newYn)
                        .renewDtime(createDTime)
                        .build();
                break;
        }

        return panel;
    }

    public List<ImageInfo> getRecommendPanelDefaultImageList(OsType osType){

        List<ImageInfo> imgList = null;

        try{

            imgList = redisService.getListWithPrefix(RedisKeyConstant.RECOMMEND_PANEL_DEFAULT_IMGLIST_KEY,ImageInfo.class);
        }catch(Exception e){
            log.error("getRecommendPanelDefaultImageList error : {}",e.getMessage());
        }finally {
            if(CollectionUtils.isEmpty(imgList)){
                imgList = recommendReadMapper.selectRecommendPanelDefaultImageList();
                if(!CollectionUtils.isEmpty(imgList)){
                    redisService.setWithPrefix(RedisKeyConstant.RECOMMEND_PANEL_DEFAULT_IMGLIST_KEY, imgList, recommendPanelDefaultImageExpiredSec);
                }
            }
        }

        if(!CollectionUtils.isEmpty(imgList)){
            Collections.shuffle(imgList);

            return Arrays.asList(imgList.stream().filter(imageInfo -> osType.equals(imageInfo.getOsType())).findFirst().orElse(null));
        }
        return null;
    }

	private ListDto<List<RecommendPanelTrackDto>> getTrackList(List<Long> trackIdList){

        if(CollectionUtils.isEmpty(trackIdList)){
            return null;
        }

//        URI uri = UriComponentsBuilder.newInstance().scheme("http").host("meta-api")
//                .path("meta/v1/track/list")
//                .queryParam("trackIdList", trackIdList.toArray(new Long[0]))
//                .build().encode().toUri();
//
//
//        CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> response = restTemplate.exchange(
//                uri,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>>>() {}).getBody();
//
//
        // feign 으로 변경
        // edited by Bob 2018.09.05
        CommonApiResponse<ListDto<List<RecommendPanelTrackDto>>> response = metaApiProxy.recommendPanelTracks(trackIdList.toArray(new Long[0]));

        return response.getData();

    }

    @Override
    @Transactional(MyBatisDatasourceConfig.SERVICE_SQL_TRANSACTION_BEAN_NAME)
    public void addPreferArtistPanel(Long characterNo) {
        List<CharacterPreferArtistGenreDto> characterPreferArtistGenreDtos = recommendReadMapper.selectCharacterPreferArtistGenre(characterNo);

        if (CollectionUtils.isEmpty(characterPreferArtistGenreDtos)) throw new CommonBusinessException(CommonErrorDomain.EMPTY_DATA);

        Collections.sort(characterPreferArtistGenreDtos, new GenreCountCompare());

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
            log.error("Recommend :: recommend artist :: Error Message", e.getMessage());
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
            log.error("Recommend :: recommend Genre :: Error Message", e.getMessage());
            throw new CommonBusinessException(PersonalErrorDomain.PREFER_GENRE_PANEL_FAIL);
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

            for (int i = 0; i < similarArtistDtoList.size(); i++) {
                if (beforeArtistId == null) beforeArtistId = similarArtistDtoList.get(i).getArtistId();
                if (!beforeArtistId.equals(similarArtistDtoList.get(i).getArtistId())) {
                    checkCount = 0;
                    beforeArtistId = similarArtistDtoList.get(i).getArtistId();
                }
                if (checkCount < count && !ids.contains(similarArtistDtoList.get(i).getSimilarArtistId())) {
                    recommendArtistListDto.add(RecommendArtistListDto.builder()
                            .artistId(similarArtistDtoList.get(i).getSimilarArtistId())
                            .artistType(ArtistType.SIMILAR).build());
                    ids.add(similarArtistDtoList.get(i).getSimilarArtistId());
                    checkCount++;
                }
            }
        }
    }

    private void fillSimilarArtist(int count, List<RecommendArtistListDto> recommendArtistListDto, List<Long> ids) {
        List<SimilarArtistDto> similaArtistList = recommendReadMapper.selectSimilarArtistByIdList(ids);

        if (!CollectionUtils.isEmpty(similaArtistList)) {
            Collections.shuffle(similaArtistList);
            for (SimilarArtistDto s : similaArtistList) {
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