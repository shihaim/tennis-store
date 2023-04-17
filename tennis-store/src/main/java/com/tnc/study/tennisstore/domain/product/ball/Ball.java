package com.tnc.study.tennisstore.domain.product.ball;

import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_ball")
@DiscriminatorValue("BALL")
public class Ball extends Product {

    @Enumerated(EnumType.STRING)
    private BallType ballType;

    public Ball(String name, String description, String brand, Money price, int stockQuantity,
                BallType ballType) {
        super(name, description, brand, price, stockQuantity);
        this.ballType = ballType;
    }

    public void changeBallInfo(String name, String description, String brand, Money price, int stockQuantity,
                               BallType ballType) {
        changeProductInfo(name, description, brand, price, stockQuantity);
        this.ballType = ballType;
    }
}
