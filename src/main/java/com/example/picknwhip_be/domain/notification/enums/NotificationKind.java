package com.example.picknwhip_be.domain.notification.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationKind {
  // ORDER
  ORDER_SHEET_CHECKING(
      NotificationType.ORDER, true, "주문서가 전달되었습니다", "${storeName}에서 주문서를 확인 후 알려드릴게요!"),
  ORDER_PAYMENT_REQUESTED(
      NotificationType.ORDER, true, "결제가 완료되면 주문이 확정됩니다", "${storeName}에서 주문을 확정했어요.\n결제를 진행해주세요."),
  ORDER_MAKING(
      NotificationType.ORDER,
      true,
      "사장님이 결제를 확인했어요",
      "${storeName}에서 케이크 제작을 시작했어요. 조금만 기다려주시면 정성껏 준비해드릴게요!"),
  ORDER_REJECTED(
      NotificationType.ORDER, true, "주문 제작이 어려워요", "${storeName}에서 주문서를 확인했습니다. 사장님과 채팅으로 상담해보세요."),
  ORDER_REJECTED_PAYMENT_FAILED(
      NotificationType.ORDER,
      false,
      "결제가 정상적으로 처리되지 않았어요",
      "결제가 완료되지 않았거나, 결제 과정에 오류가 있을 수 있으니 다시 결제를 진행해주세요."),
  ORDER_PICKUP_READY(
      NotificationType.ORDER,
      true,
      "케이크 픽업이 준비되었습니다",
      "${storeName}에서 케이크가 준비되었어요. 픽업 시간을 확인해주세요."),

  // REVIEW
  REVIEW_REPLY_CREATED(
      NotificationType.REVIEW, true, "사장님이 답변을 남겼어요", "작성하신 리뷰에 ${storeName} 사장님이 답변을 남겼습니다."),
  REVIEW_WRITE_REQUESTED(
      NotificationType.REVIEW, false, "리뷰를 작성해주세요", "케이크는 어떠셨나요? 소중한 후기를 남겨주세요!"),

  // ETC
  REPORT_RECEIVED(NotificationType.ETC, false, "신고가 접수되었습니다", "3-5 영업일 내에 검토하여 답변 드리겠습니다."),
  REPORT_REPLIED(NotificationType.ETC, false, "접수하신 신고에 대해 안내드립니다", "신고하신 내용에 대한 답변 드립니다."),
  EVENT(NotificationType.ETC, false, "크리스마스 특별 할인", "12월 한정! 크리스마스 케이크 10% 할인 이벤트가 진행중이에요.");

  private final NotificationType type;
  private final boolean requiredStoreName;
  private final String titleTemplate;
  private final String contentTemplate;
}
