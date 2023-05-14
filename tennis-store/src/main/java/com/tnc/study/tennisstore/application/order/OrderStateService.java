package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class OrderStateService {
    private final OrderRepository orderRepository;

    public void confirmOrder(Long orderId) {
        Order order = OrderServiceHelper.findExistingOrder(orderRepository, orderId);
        order.confirm();
    }

    public void cancelOrder(Long orderId) {
        Order order = OrderServiceHelper.findExistingOrder(orderRepository, orderId);
        order.cancel();
    }
}
