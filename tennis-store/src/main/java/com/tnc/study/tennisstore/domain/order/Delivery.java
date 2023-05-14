package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.domain.Address;
import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.framework.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Delivery extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @Embedded
    private Address address;

    @Embedded
    private Receiver receiver;

    @Column(name = "delivery_message")
    private String message;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "delivery_fee"))
    private Money deliveryFee;

    @Enumerated(EnumType.STRING)
    private DeliveryState state;

    private String trackingNumber;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    Delivery(Address address, Receiver receiver, String message, Order order) {
        this.address = address;
        this.receiver = receiver;
        this.message = message;
        this.order = order;
        this.state = DeliveryState.PREPARING;
        this.deliveryFee = calculateDeliveryFee();
    }

    private Money calculateDeliveryFee() {
        Money totalPrice = order.getTotalPrice();
        Money freeDeliveryBaseAmount = Money.of(50000);

        if (totalPrice.isGreaterThan(freeDeliveryBaseAmount) ||
            totalPrice.isSame(freeDeliveryBaseAmount)) {
            return Money.ZERO;
        }

        return Money.of(3000);
    }
}
