package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.domain.order.OrderLine;
import com.tnc.study.tennisstore.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    private static FindOrderResponse convert(Order order) {
        List<OrderLine> orderLines = order.getOrderLines();
        List<OrderLineResponse> orderLineResponses = orderLines.stream()
                .map(orderLine -> new OrderLineResponse(
                        orderLine.getId(),
                        orderLine.getProduct().getId(),
                        orderLine.getOrderPrice().getAmount(),
                        orderLine.getOrderCount()
                )).toList();
        return new FindOrderResponse(
                order.getId(),
                order.getMember().getId(),
                order.getMember().getName(),
                order.getCreatedDate(),
                order.getState(),
                order.getState().getMessage(),
                orderLineResponses
        );
    }
}
