package com.tnc.study.tennisstore.domain.product.racquet;

import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.domain.product.Product;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_racquet")
@DiscriminatorValue("RACQUET")
public class Racquet extends Product {

    private int weight;
    private int headSize;
    private int balance;

    public Racquet(String name, String description, String brand, Money price, int stockQuantity,
                   int weight, int headSize, int balance) {
        super(name, description, brand, price, stockQuantity);
        this.weight = weight;
        this.headSize = headSize;
        this.balance = balance;
    }

    public void changeRacquetInfo(String name, String description, String brand, Money price, int stockQuantity,
                                  int weight, int headSize, int balance) {
        changeProductInfo(name, description, brand, price, stockQuantity);
        this.weight = weight;
        this.headSize = headSize;
        this.balance = balance;
    }
}
