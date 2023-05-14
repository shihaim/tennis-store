package com.tnc.study.tennisstore.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Money {
    @Column
    private BigDecimal amount;

    public static final Money ZERO = Money.of(0);

    /**
     * 0보다 크면 true
     * @return
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * 0보다 작으면 true
     * @return
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * 금액이 같은지 비교
     * @param money
     * @return
     */
    public boolean isSame(Money money) {
        if (money == null) return false;
        return amount.compareTo(money.amount) == 0;
    }

    /**
     * 금액이 큰지 비교
     * @param money
     * @return
     */
    public boolean isGreaterThan(Money money) {
        if (money == null) return false;
        return amount.compareTo(money.amount) > 0;
    }

    /**
     * 금액이 작은지 비교
     * @param money
     * @return
     */
    public boolean isLessThan(Money money) {
        if (money == null) return false;
        return amount.compareTo(money.amount) < 0;
    }

    /**
     * 더하기
     * @param money
     * @return
     */
    public Money add(Money money) {
        return new Money(amount.add(money.amount));
    }

    /**
     * 빼기
     * @param money
     * @return
     */
    public Money subtract(Money money) {
        return new Money(amount.subtract(money.amount));
    }

    /**
     * 곱하기
     * @param money
     * @return
     */
    public Money multiply(Money money) {
        return new Money(amount.multiply(money.amount));
    }

    /**
     * 곱하기
     * @param count
     * @return
     */
    public Money multiply(int count) {
        return new Money(amount.multiply(BigDecimal.valueOf(count)));
    }

    /**
     * 나누기
     * @param money
     * @return
     */
    public Money divide(Money money, RoundingMode roundingMode) {
        return new Money(amount.divide(money.amount, roundingMode));
    }

    /**
     * 나누기
     * @param money
     * @return
     */
    public Money divide(Money money, int scale, RoundingMode roundingMode) {
        return new Money(amount.divide(money.amount, scale, roundingMode));
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return amount.compareTo(money.amount) == 0;
//        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money of(double amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    public static Money of(long unscaledVal, int scale) {
        return new Money(BigDecimal.valueOf(unscaledVal, scale));
    }

    @Override
    public String toString() {
        return amount.toString();
    }
}
