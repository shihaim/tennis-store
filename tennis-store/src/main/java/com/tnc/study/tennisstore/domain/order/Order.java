package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.framework.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderLine> orderLines = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @Enumerated(EnumType.STRING)
    private OrderState state;

    public Order(Member member,
                 List<OrderLine> orderLines,
                 Address deliveryAddress,
                 Receiver deliveryReceiver,
                 String deliveryMessage) {

        if (orderLines == null || orderLines.isEmpty()) {
            throw new RequiredOrderLineException();
        }

        for (OrderLine orderLine : orderLines) {
            addOrderLine(orderLine);
        }

        setMember(member);

        this.delivery = new Delivery(deliveryAddress, deliveryReceiver, deliveryMessage, this);
        this.state = OrderState.ORDER_RECEIVED;
    }

    private void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    private void addOrderLine(OrderLine orderLine) {
        this.orderLines.add(orderLine);
        orderLine.setOrder(this);
    }

    public Money getTotalPrice() {
        return orderLines.stream()
                .map(OrderLine::getTotalPrice)
                .reduce(Money.ZERO, Money::add);
    }

    /**
     * 구매 확정
     */
    public void confirm() {
        if (state == OrderState.CANCELED) {
            throw new OrderStateException("취소 건은 구매 확정할 수 없습니다.");
        }

        this.state = OrderState.PURCHASE_CONFIRMATION;
    }

    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getState() != DeliveryState.PREPARING) {
            throw new AlreadyDeliveryException("이미 배송 중인 주문은 취소할 수 없습니다.");
        }

        if (state == OrderState.PURCHASE_CONFIRMATION) {
            throw new OrderStateException("구매 확정인 주문은 취소할 수 없습니다.");
        }

        for (OrderLine orderLine : orderLines) {
            orderLine.cancel();
        }

        this.state = OrderState.CANCELED;
    }
}
