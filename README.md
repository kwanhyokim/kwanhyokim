# 청취로그 정산정보
## SourceType.STRM
### 일반 스트리밍의 경우
sttToken 활용 (무료곡일 경우 MCP에서 serviceId 조회)
* sttToken != null 경우
```java
sourcePlayLogBuilder
    .pssrlCd(sttToken.getServiceId())
    .serviceId(sttToken.getServiceId())
    .prchsId(sttToken.getPurchaseId())
    .goodsId(sttToken.getGoodsId());
```

* sttToken == null 이고, 무료곡일 경우 (memberNo와 SourceType.playType으로 <code>settlementInfo(정산 정보)</code>를 조회(tb_prchs_pass)
```java
#Case1 settlementInfo != null
sourcePlayLogBuilder
    .pssrlCd(serviceId)
    .serviceId(serviceId)
    .prchsId(purchaseId)
    .goodsId(goodsId);

#Case2 settlementInfo == null
sourcePlayLogBuilder
    .pssrlCd(serviceId)
    .serviceId(serviceId)
    .prchsId(null)
    .goodsId(null);
```

* sttToken == null 이고, 무료곡이 아닐 경우 (memberNo와 SourceType.playType으로 <code>settlementInfo(정산 정보)</code>를 조회(tb_prchs_pass)
```java
#Case1 settlementInfo == null
정산 정보 조회 실패 예외 발생

#Case2 settlementInfo != null
serviceId = settlementService.evaluateServiceIdByResourcePlayLogRequest 메소드를 통해 serviceId GET;
sourcePlayLogBuilder
    .pssrlCd(serviceId)
    .serviceId(serviceId)
    .prchsId(settlementInfo.getPrchsId())
    .goodsId(settlementInfo.getGoodsId());
```

### 캐시드 스트리밍의 경우
cachedStreamingToken에 의존 (옵션: freeCachedStreamingToken)
* 캐시드 스트리밍이고, 무료곡이 아닐 경우 (cachedStreamingToken만 활용)
```java
sourcePlayLogBuilder
    .prchsId(cachedStreamingToken.getPrchsId())
    .goodsId(cachedStreamingToken.getGoodsId())
    .pssrlCd(cachedStreamingToken.getSvdId())
    .serviceId(cachedStreamingToken.getSvdId());
```

* 캐시드 스트리밍이고, 무료곡일 경우 (cachedStreamingToken, freeCachedStreamingToken 활용)
```java
sourcePlayLogBuilder
    .prchsId(cachedStreamingToken.getPrchsId())
    .goodsId(cachedStreamingToken.getGoodsId())
    .pssrlCd(freeCachedStreamingToken.getServiceId())
    .serviceId(freeCachedStreamingToken.getServiceId());
```

---

## SourceType.DN
ownerToken에 의존
```java
sourcePlayLogBuilder
    .drmMemberNo(ownerTokenClaim.getMemberNo())
    .drmPssrlCd(ownerTokenClaim.getPssrlCode())
    .drmServiceId(ownerTokenClaim.getServiceId())
    .drmPrchsId(ownerTokenClaim.getPurchaseId())
    .drmGoodsId(ownerTokenClaim.getGoodsId());
```

---

## SourceType.VIDEO_MV
sttToken에 의존
```java
sourcePlayLogBuilder
    .pssrlCd(sttToken.getServiceId())
    .serviceId(sttToken.getServiceId())
    .prchsId(sttToken.getPurchaseId())
    .goodsId(sttToken.getGoodsId());
```

