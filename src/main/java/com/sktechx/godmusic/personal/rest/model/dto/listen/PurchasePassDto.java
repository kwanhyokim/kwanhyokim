package com.sktechx.godmusic.personal.rest.model.dto.listen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by Kobe.
 *
 * 실제론 Purchase 에서 처리해야하지만 청취로그 특성상 빈번한 호출이 예상되어 일단 필요한부분 구현
 *
 * @author Kobe/최훈영/SKTECHX (hunyoung.choi@sk.com)
 * @date 2018. 8. 9.
 * @time PM 2:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchasePassDto {
	private Long prchsId;            // 구매아이디
//	private Long memberNo;            // 회원번호
	private Long goodsId;            // 상품아이디
//	private Long passId;            // 이용권아이디
//	private GoodsStatusType goodsStatus;        // 상품상태
//	private GoodsType goodsType;    // 상품타입
//	private Date useStartDtime;        // 사용시작일시
//	private Date useEndDtime;        // 사용종료일시
//	private Integer remainTrackQty;        // 잔여트랙수량
//	private YnType tmbrshipApplyYn;    // 티멤법십적용여부
//	private String billingKey;        // 빌링키
//	private PaymentMethodType paymentMethodType;    // 결제수단
//	private PurchaseChannelType prchsChnl;        // 구매채널
//	private Integer paymentAmount;    // 결제금액
//	private Date paymentDtime;        // 결제일시
//	private Date cancelDtime;        // 취소일시
//	private Long pgLinkHistId;        // PG연동이력아이디
//	private Long r2kLinkId;            // R2K연동아이디
//	private Integer promotionId;    // 프로모션아이디
//	private Date rcmmdCreateDtime;        // 생성일시
//	private Date rcmmdUpdateDtime;        // 수정일시
//	private Date repaymentValidDtime; //재결제 가능 일시
//	private YnType autoPaymentGoodsInYn;
//	private String inappPaymentAmount;
//	private String inappPaymentExfeeAmount;
//	private String goodsDispNm;
}
