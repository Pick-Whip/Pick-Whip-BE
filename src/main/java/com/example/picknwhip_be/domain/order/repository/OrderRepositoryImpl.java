package com.example.picknwhip_be.domain.order.repository;

import static com.example.picknwhip_be.domain.design.entity.QDesignGallery.designGallery;
import static com.example.picknwhip_be.domain.order.entity.QOrder.order;
import static com.example.picknwhip_be.domain.shop.entity.QShop.shop;

import com.example.picknwhip_be.domain.order.dto.req.OrderCursorReqDTO;
import com.example.picknwhip_be.domain.order.entity.Order;
import com.example.picknwhip_be.domain.order.entity.enums.PaymentStatus;
import com.example.picknwhip_be.domain.order.entity.enums.Status;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Order> findAllByCursor(Long userId, OrderCursorReqDTO req) {

        NumberExpression<Integer> statusScore = getStatusScore();

        return queryFactory
                .selectFrom(order)
                .leftJoin(order.shop, shop).fetchJoin()
                .leftJoin(order.designGallery, designGallery).fetchJoin()
                .where(
                        order.user.userId.eq(userId),
                        filterByType(req.getType()),
                        cursorCondition(req, statusScore))
                .orderBy(statusScore.asc(), order.pickupDatetime.asc(), order.id.asc())
                .limit(req.getLimit() + 1)
                .fetch();
    }

    private BooleanExpression filterByType(String type) {
        if ("REQUEST".equalsIgnoreCase(type)) {
            return order.status.in(Status.CONFIRM_WAIT, Status.PROD_CONFIRM, Status.IMPOSSIBLE);
        } else {
            return order.status.in(Status.MAKING, Status.PICKUP_WAIT, Status.COMPLETED);
        }
    }

    private BooleanExpression cursorCondition(
            OrderCursorReqDTO req, NumberExpression<Integer> statusScore) {
        if (req.getLastOrderId() == null) return null;

        return statusScore
                .gt(req.getLastStatusScore())
                .or(
                        statusScore
                                .eq(req.getLastStatusScore())
                                .and(order.pickupDatetime.gt(req.getLastPickupDatetime())))
                .or(
                        statusScore
                                .eq(req.getLastStatusScore())
                                .and(order.pickupDatetime.eq(req.getLastPickupDatetime()))
                                .and(order.id.gt(req.getLastOrderId())));
    }

    private NumberExpression<Integer> getStatusScore() {
        return new CaseBuilder()
                .when(order.status.eq(Status.CONFIRM_WAIT))
                .then(10)
                .when(
                        order.status.eq(Status.PROD_CONFIRM).and(order.paymentStatus.eq(PaymentStatus.WAITING)))
                .then(20)
                .when(
                        order.status.eq(Status.PROD_CONFIRM).and(order.paymentStatus.ne(PaymentStatus.WAITING)))
                .then(30)
                .when(order.status.eq(Status.IMPOSSIBLE))
                .then(30)
                .when(order.status.eq(Status.MAKING))
                .then(40)
                .when(order.status.eq(Status.PICKUP_WAIT))
                .then(50)
                .when(order.status.eq(Status.COMPLETED))
                .then(60)
                .otherwise(99);
    }
}