package com.tnc.study.tennisstore.domain.product;

import com.tnc.study.tennisstore.domain.Money;
import com.tnc.study.tennisstore.framework.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "product_type")
public abstract class Product extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_type", insertable = false, updatable = false)
    private String productType;

    private String name;

    private String description;

    private String brand;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", precision = 10, scale = 2))
    private Money price;

    private int stockQuantity;

    protected Product(String name, String description, String brand, Money price, int stockQuantity) {
        if (price.isNegative()) {
            throw new ProductPriceException("상품 가격은 음수일 수 없습니다.");
        }

        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    protected void changeProductInfo(String name, String description, String brand, Money price, int stockQuantity) {
        if (price.isNegative()) {
            throw new ProductPriceException("상품 가격은 음수일 수 없습니다.");
        }

        this.name = name;
        this.description = description;
        this.brand = brand;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException(this.name);
        }
        this.stockQuantity = restStock;
    }

    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }
}
