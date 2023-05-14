package com.tnc.study.tennisstore.api.order;

import com.tnc.study.tennisstore.application.order.CreateOrderRequest;
import com.tnc.study.tennisstore.application.order.CreateOrderService;
import com.tnc.study.tennisstore.application.order.OrderStateService;
import com.tnc.study.tennisstore.framework.web.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/shop/orders")
@RequiredArgsConstructor
public class ShopOrderController {

    private final CreateOrderService createOrderService;
    private final OrderStateService orderStateService;

    @PostMapping
    public ResponseEntity<ApiResponse> placeOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long orderId = createOrderService.placeOrder(request);
        return ResponseEntity.created(URI.create("/api/shop/orders/%s".formatted(orderId)))
                .body(ApiResponse.OK);
    }

    /**
     * 구매 확정
     *
     * @param id
     * @return
     */
    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse> confirmOrder(@PathVariable Long id) {
        orderStateService.confirmOrder(id);
        return ResponseEntity.ok(ApiResponse.OK);
    }

    /**
     * 구매 취소
     * @param id
     * @return
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        orderStateService.cancelOrder(id);
        return ResponseEntity.ok(ApiResponse.OK);
    }
}
