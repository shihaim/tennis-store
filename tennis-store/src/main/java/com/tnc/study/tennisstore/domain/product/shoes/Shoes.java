package com.tnc.study.tennisstore.domain.product.shoes;

import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.product.Product;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_shoes")
@DiscriminatorValue("SHOES")
public class Shoes extends Product {

    @Enumerated(EnumType.STRING)
    private GroundType groundType;

    public Shoes(String name, String description, String brand, Money price, int stockQuantity,
                 GroundType groundType) {
        super(name, description, brand, price, stockQuantity);
        this.groundType = groundType;
    }

    public void changeShoesInfo(String name, String description, String brand, Money price, int stockQuantity,
                                GroundType groundType) {
        changeProductInfo(name, description, brand, price, stockQuantity);
        this.groundType = groundType;
    }
}
