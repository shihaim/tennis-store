package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.domain.order.Delivery;
import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.domain.order.OrderLine;
import com.tnc.study.tennisstore.domain.order.OrderRepository;
import com.tnc.study.tennisstore.domain.order.query.FindOrderCondition;
import com.tnc.study.tennisstore.domain.order.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tnc.study.tennisstore.application.order.FindOrderResponse.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindOrderService {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    public List<FindOrderResponse> findOrders() {
        return orderRepository.findOrdersUsingFetchJoin().stream()
                .map(FindOrderService::convert).toList();
    }

    public Page<FindOrderResponse> findOrdersWithPaging(Pageable pageable) {
        return orderRepository.findOrdersWithPaging(pageable)
                .map(FindOrderService::convert);
    }

    public Page<FindOrderResponse2> findOrderResponse2(Pageable pageable) {
        // Order 정보들을 조회한다.
        Page<FindOrderResponse2> orders = orderRepository.findOrderResponse2(pageable);

        // Order 정보들에서 Id만 뽑아낸다.
        List<Long> orderIds = orders.getContent().stream()
                .map(FindOrderResponse2::getOrderId)
                .toList();

        // OrderIds에 포함되는 OrderLine들을 뽑아내고 그 정보들을 orderId별로 그룹핑 한다.
        Map<Long, List<FindOrderLineResponse>> orderLineMap = orderRepository.findOrderLineResponse(orderIds).stream()
                .collect(Collectors.groupingBy(FindOrderLineResponse::getOrderId));

        // OrderId로 그룹핑된 OrderLine 정보들을 각 주문 정보에 Set 한다.
        orders.getContent()
                .forEach(order -> order.setOrderLines(orderLineMap.get(order.getOrderId())));

        return orders;
    }

    public Page<FindOrderResponse> findTotalOrders(Pageable pageable) {
        return orderRepository.findTotalOrders(pageable).map(FindOrderService::convert);
    }

    public Slice<FindOrderResponse> findOrdersByMember(Long memberId, Pageable pageable) {
        return orderRepository.findOrdersByMemberId(memberId, pageable)
                .map(FindOrderService::convert);
    }

    /**
     * 전체 주문 조회 (검색 조건)
     *
     * @param pageable
     * @return
     */
    public Page<FindOrderResponse> findOrdersByCondition(FindOrderCondition condition, Pageable pageable) {
        return orderQueryRepository.findOrdersByCondition(condition, pageable)
                .map(FindOrderService::convert);
    }

    /**
     * 회원별 주문 조회 ( No Offset )
     *
     * @param memberId
     * @param pageable
     * @return
     */
    public Slice<FindOrderResponse> findOrdersByMemberIdNoOffset(Long memberId, Long lastOrderId, Pageable pageable) {
        return orderQueryRepository.findOrdersByMemberIdNoOffset(memberId, lastOrderId, pageable)
                .map(FindOrderService::convert);
    }

    private static FindOrderResponse convert(Order order) {
        List<OrderLine> orderLines = order.getOrderLines();
        List<OrderLineResponse> orderLineResponses = orderLines.stream()
                .map(orderLine -> new OrderLineResponse(
                        orderLine.getId(),
                        orderLine.getProduct().getId(),
                        orderLine.getOrderPrice().getAmount(),
                        orderLine.getOrderCount()
                )).toList();

        Delivery delivery = order.getDelivery();

        DeliveryResponse deliveryResponse = new DeliveryResponse(
                delivery.getId(),
                delivery.getAddress(),
                delivery.getReceiver(),
                delivery.getMessage(),
                delivery.getDeliveryFee().getAmount(),
                delivery.getTrackingNumber()
        );

        return new FindOrderResponse(
                order.getId(),
                order.getMember().getId(),
                order.getMember().getName(),
                order.getCreatedDate(),
                order.getState(),
                order.getState().getMessage(),
                order.getTotalPrice().getAmount(),
                orderLineResponses,
                deliveryResponse
        );
    }
}
