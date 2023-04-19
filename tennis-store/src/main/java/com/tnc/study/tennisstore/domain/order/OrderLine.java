package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.framework.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderLine extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "order_line_id")
    private Long id;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @JoinColumn(name = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "order_price", precision = 10, scale = 2))
    private Money orderPrice;

    private int orderCount;

    public OrderLine(Product product, Money orderPrice, int orderCount) {
        if (orderCount <= 0) {
            throw new NotEnoughOrderCountException(product.getName());
        }

        this.product = product;
        this.orderPrice = orderPrice;
        this.orderCount = orderCount;

        product.removeStock(orderCount);
    }

     void setOrder(Order order) {
        this.order = order;
    }

    Money getTotalPrice() {
        return orderPrice.multiply(orderCount);
    }
}
