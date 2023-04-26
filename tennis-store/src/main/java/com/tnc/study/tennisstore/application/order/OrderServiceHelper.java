package com.tnc.study.tennisstore.application.order;


import com.tnc.study.tennisstore.application.member.NoMemberException;
import com.tnc.study.tennisstore.domain.member.Member;
import com.tnc.study.tennisstore.domain.member.MemberRepository;
import com.tnc.study.tennisstore.domain.order.Order;
import com.tnc.study.tennisstore.domain.order.OrderRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderServiceHelper {

    public static Order findExistingOrder(OrderRepository repository, Long orderId) {
        return repository.findById(orderId).orElseThrow(NoOrderException::new);
    }
}
