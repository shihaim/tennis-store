package com.tnc.study.tennisstore.api.order;

import com.tnc.study.tennisstore.application.order.CreateOrderRequest;
import com.tnc.study.tennisstore.application.order.CreateOrderService;
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

    @PostMapping
    public ResponseEntity<ApiResponse> placeOrder(@Valid @RequestBody CreateOrderRequest request) {
        Long orderId = createOrderService.placeOrder(request);
        return ResponseEntity.created(URI.create("/api/shop/orders/%s".formatted(orderId)))
                .body(ApiResponse.OK);
    }
}
