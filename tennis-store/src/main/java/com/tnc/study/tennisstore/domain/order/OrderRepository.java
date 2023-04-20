package com.tnc.study.tennisstore.domain.order;

import com.tnc.study.tennisstore.application.order.FindOrderLineResponse;
import com.tnc.study.tennisstore.application.order.FindOrderResponse2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        select o 
        from Order o
        join fetch o.member
        join fetch o.orderLines
    """)
    List<Order> findOrdersUsingFetchJoin();

    @Query(
        value = """
            select o 
            from Order o
            join fetch o.member
        """,
        countQuery = """
            select count(o)
            from Order o
        """
    )
    Page<Order> findOrdersWithPaging(Pageable pageable);

    @Query("""
        select new com.tnc.study.tennisstore.application.order.FindOrderResponse2(
            o.id, m.id, m.name, o.createdDate, o.state
        )
        from Order o
        join o.member m
    """)
    Page<FindOrderResponse2> findOrderResponse2(Pageable pageable);

    @Query("""
        select new com.tnc.study.tennisstore.application.order.FindOrderLineResponse(
            ol.order.id, ol.id, ol.product.id, ol.orderPrice.amount, ol.orderCount
        )
        from OrderLine ol
        where ol.order.id in :orderIds
    """)
    List<FindOrderLineResponse> findOrderLineResponse(@Param("orderIds") List<Long> orderIds);
}
