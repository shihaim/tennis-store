package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.application.member.MemberServiceHelper;
import com.tnc.study.tennisstore.application.product.NoProductException;
import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.domain.order.OrderLine;
import com.tnc.study.tennisstore.domain.order.OrderRepository;
import com.tnc.study.tennisstore.domain.order.Receiver;
import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.tnc.study.tennisstore.application.order.CreateOrderRequest.*;

@Transactional
@RequiredArgsConstructor
@Service
public class CreateOrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public Long placeOrder(CreateOrderRequest request) {
        Map<Long, OrderProduct> orderProducts = request.orderProducts();
        Set<Long> productIds = orderProducts.keySet();
        List<Product> products = productRepository.findAllById(productIds);

        if (productIds.size() != products.size()) {
            throw new NoProductException("요청한 상품은 구매할 수 없습니다.");
        }

        // 주문 상품 엔티티 생성
        List<OrderLine> orderLines = products.stream()
                .map(product -> new OrderLine(
                        product,
                        product.getPrice(),
                        orderProducts.get(product.getId()).orderCount()
                )).toList();

        // 회원 조회
        Member findMember = MemberServiceHelper.findExistingMember(memberRepository, request.memberId());

        // 값 타입 생성
        Address address = new Address(request.address1(), request.address2(), request.zipcode());
        Receiver receiver = new Receiver(request.receiverName(), request.receiverPhone());
        String deliveryMessage = request.deliveryMessage();

        Order order = new Order(findMember, orderLines, address, receiver, deliveryMessage);
        Order savedOrder = orderRepository.save(order);

        return savedOrder.getId();
    }

}
