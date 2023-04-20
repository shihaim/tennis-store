package com.tnc.study.tennisstore.api.order;

import com.tnc.study.tennisstore.application.order.FindOrderResponse;
import com.tnc.study.tennisstore.application.order.FindOrderResponse2;
import com.tnc.study.tennisstore.application.order.FindOrderService;
import com.tnc.study.tennisstore.framework.web.response.Content;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
public class AdminOrderController {

    private final FindOrderService findOrderService;

//    @GetMapping
    public ResponseEntity<Content<FindOrderResponse>> findOrders() {
        List<FindOrderResponse> orders = findOrderService.findOrders();
        Content<FindOrderResponse> content = Content.of(orders);

        return ResponseEntity.ok(content);
    }

//    @GetMapping
    public ResponseEntity<Page<FindOrderResponse>> findOrders(Pageable pageable) {
        Page<FindOrderResponse> orders = findOrderService.findOrdersWithPaging(pageable);

        return ResponseEntity.ok(orders);
    }

    @GetMapping
    public ResponseEntity<Page<FindOrderResponse2>> findOrders2(Pageable pageable) {
        Page<FindOrderResponse2> orders = findOrderService.findOrderResponse2(pageable);

        return ResponseEntity.ok(orders);
    }
}
