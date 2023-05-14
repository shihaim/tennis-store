package com.tnc.study.tennisstore.domain.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying(clearAutomatically = true)
    @Query("""
        update Product p
        set p.price.amount = (p.price.amount * 11/10)
        where p.stockQuantity < 20
    """)
    int increasePriceBy10PerByStockQuantityLessThan20();
}
