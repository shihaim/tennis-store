package com.tnc.study.tennisstore.application.order;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Email;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.Password;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import com.tnc.study.tennisstore.domain.order.*;
import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.domain.product.ProductRepository;
import com.tnc.study.tennisstore.domain.product.racquet.Racquet;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import com.tnc.study.tennisstore.domain.product.shoes.Shoes;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    CreateOrderService createOrderService;
    @Autowired
    OrderStateService orderStateService;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setup() {
        String address1 = "서울특별시 강남구 도곡로 117";
        String address2 = "12층";
        String zipcode = "06253";

        Email email = Email.of("hashi00518@tnctec.co.kr");
        Password password = Password.of("1234");
        String name = "하승완";

        Address address = new Address(address1, address2, zipcode);
        Member member = new Member(email, password, name, address);
        memberRepository.save(member);

        Racquet racquet = new Racquet(
                "프로스태프 RF 97",
                "페더러 라켓",
                "윌슨",
                Money.of(180000),
                10,
                290,
                97,
                315
        );

        Shoes shoes = new Shoes(
                "아식스 뭐시기",
                "조코비치 신발",
                "아식스",
                Money.of(150000),
                10,
                GroundType.HARD
        );

        productRepository.save(racquet);
        productRepository.save(shoes);

        // 주문 정보 생성
        String receiverPhone = "010-1234-5678";
        String deliveryMessage = "안전하게 와주세요";

        List<OrderLine> orderLines = Arrays.asList(
                new OrderLine(racquet, racquet.getPrice(), 2),
                new OrderLine(shoes, shoes.getPrice(), 5)
        );
        Receiver receiver = new Receiver(member.getName(), receiverPhone);

        Order order = new Order(member, orderLines, address, receiver, deliveryMessage);
        orderRepository.save(order);
    }

    @Test
    @DisplayName("주문 요청 테스트")
    void testPlaceOrder() {
        Member member = memberRepository.findAll().get(0);
        List<Product> products = productRepository.findAll();

        Long memberId = member.getId();
        Address address = member.getAddress();
        String receiverPhone = "010-1234-5678";
        String deliveryMessage = "안전하게 와주세요";

        Map<Long, CreateOrderRequest.OrderProduct> orderProducts = products.stream()
                .collect(Collectors.toMap(Product::getId, product -> new CreateOrderRequest.OrderProduct(2)));

        CreateOrderRequest createOrderRequest = new CreateOrderRequest(memberId,
                address.getAddress1(),
                address.getAddress2(),
                address.getZipcode(),
                member.getName(),
                receiverPhone,
                deliveryMessage,
                orderProducts);

        Long orderId = createOrderService.placeOrder(createOrderRequest);

        Order order = orderRepository.findById(orderId).get();
        List<Product> productList = productRepository.findAll();

        assertThat(order.getMember().getName()).isEqualTo(member.getName());
        assertThat(order.getState()).isEqualTo(OrderState.ORDER_RECEIVED);
        assertThat(productList).extracting("stockQuantity").containsExactly(6, 3);
        assertThat(order.getDelivery().getDeliveryFee()).isEqualTo(Money.ZERO);
        assertThat(order.getDelivery().getState()).isEqualTo(DeliveryState.PREPARING);
        assertThat(order.getDelivery().getMessage()).isEqualTo(deliveryMessage);
    }

    @Test
    @DisplayName("주문 확정 서비스")
    void testConfirmOrder() {
        Order order = orderRepository.findAll().get(0);

        orderStateService.confirmOrder(order.getId());
        Order findOrder = orderRepository.findById(order.getId()).get();

        assertThat(findOrder.getState()).isEqualTo(OrderState.PURCHASE_CONFIRMATION);
    }

    @Test
    @DisplayName("주문 취소 서비스")
    void testCancelOrder() {
        Order order = orderRepository.findAll().get(0);

        orderStateService.cancelOrder(order.getId());
        Order findOrder = orderRepository.findById(order.getId()).get();
        List<Product> products = productRepository.findAll();

        assertThat(findOrder.getState()).isEqualTo(OrderState.CANCELED);
        assertThat(products).extracting("stockQuantity")
                .containsExactly(10, 10);
    }
}