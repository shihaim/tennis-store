package com.tnc.study.tennisstore.api.member;

import com.tnc.study.tennisstore.application.order.FindOrderResponse;
import com.tnc.study.tennisstore.application.order.FindOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shop/members")
@RequiredArgsConstructor
public class ShopMemberController {

    private final FindOrderService findOrderService;

//    @GetMapping("/{id}/orders")
    public ResponseEntity<Slice<FindOrderResponse>> findOrdersByMember(@PathVariable Long id, Pageable pageable) {
        Slice<FindOrderResponse> orders = findOrderService.findOrdersByMember(id, pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * 회원별 주문 정보 조회 ( No Offset )
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}/orders")
    public ResponseEntity<Slice<FindOrderResponse>> findOrdersByMemberIdNoOffset(@PathVariable Long id,
                                                                                 @Param("lastOrderId") Long lastOrderId,
                                                                                 Pageable pageable) {
        Slice<FindOrderResponse> orders = findOrderService.findOrdersByMemberIdNoOffset(id, lastOrderId, pageable);

        return ResponseEntity.ok(orders);
    }
}
