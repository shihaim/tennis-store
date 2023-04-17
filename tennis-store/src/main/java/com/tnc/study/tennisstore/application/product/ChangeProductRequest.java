package com.tnc.study.tennisstore.application.product;

import com.tnc.study.tennisstore.domain.product.ball.BallType;
import com.tnc.study.tennisstore.domain.product.shoes.GroundType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class ChangeProductRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String brand;
    @NotNull @PositiveOrZero
    private Long price;
    @NotNull @PositiveOrZero
    private Integer stockQuantity;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChangeRacquetRequest extends ChangeProductRequest {
        @NotNull @PositiveOrZero
        private Integer weight;
        @NotNull @PositiveOrZero
        private Integer headSize;
        @NotNull @PositiveOrZero
        private Integer balance;

        public ChangeRacquetRequest(String name, String description,String brand, Long price, Integer stockQuantity,
                                    Integer weight, Integer headSize, Integer balance) {
            super(name, description, brand, price, stockQuantity);
            this.weight = weight;
            this.headSize = headSize;
            this.balance = balance;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChangeShoesRequest extends ChangeProductRequest {
        @NotNull
        private GroundType groundType;

        public ChangeShoesRequest(String name, String description,String brand, Long price, Integer stockQuantity,
                                  GroundType groundType) {
            super(name, description, brand, price, stockQuantity);
            this.groundType = groundType;
        }
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChangeBallRequest extends ChangeProductRequest {
        @NotNull
        private BallType ballType;

        public ChangeBallRequest(String name, String description,String brand, Long price, Integer stockQuantity,
                                 BallType ballType) {
            super(name, description, brand, price, stockQuantity);
            this.ballType = ballType;
        }
    }
}
