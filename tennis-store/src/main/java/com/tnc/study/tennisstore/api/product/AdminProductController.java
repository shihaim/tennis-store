package com.tnc.study.tennisstore.api.product;

import com.tnc.study.tennisstore.application.product.*;
import com.tnc.study.tennisstore.framework.web.response.ApiResponse;
import com.tnc.study.tennisstore.framework.web.response.Content;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.tnc.study.tennisstore.application.product.ChangeProductRequest.*;
import static com.tnc.study.tennisstore.application.product.CreateProductRequest.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/products")
public class AdminProductController {

    private final CreateProductService createProductService;
    private final FindProductService findProductService;
    private final ChangeProductService changeProductService;
    private final DeleteProductService deleteProductService;

    @PostMapping("/racquets")
    public ResponseEntity<ApiResponse> createRacquet(@Valid @RequestBody CreateRacquetRequest request) {
        Long racquetId = createProductService.createRacquet(request);

        return ResponseEntity
                .created(URI.create("/api/admin/products/racquets/%s".formatted(racquetId)))
                .body(ApiResponse.OK);
    }

    @PostMapping("/shoes")
    public ResponseEntity<ApiResponse> createShoes(@Valid @RequestBody CreateShoesRequest request) {
        Long shoesId = createProductService.createShoes(request);

        return ResponseEntity
                .created(URI.create("/api/admin/products/shoes/%s".formatted(shoesId)))
                .body(ApiResponse.OK);
    }

    @PostMapping("/balls")
    public ResponseEntity<ApiResponse> createBall(@Valid @RequestBody CreateBallRequest request) {
        Long ballId = createProductService.createBall(request);

        return ResponseEntity
                .created(URI.create("/api/admin/products/balls/%s".formatted(ballId)))
                .body(ApiResponse.OK);
    }

    @PutMapping("/racquets/{id}")
    public ResponseEntity<ApiResponse> changeRacquetInfo(@PathVariable Long id, @Valid @RequestBody ChangeRacquetRequest request) {
        changeProductService.changeRacquetInfo(id, request);
        return ResponseEntity.ok(ApiResponse.OK);
    }

    @PutMapping("/shoes/{id}")
    public ResponseEntity<ApiResponse> changeShoesInfo(@PathVariable Long id, @Valid @RequestBody ChangeShoesRequest request) {
        changeProductService.changeShoesInfo(id, request);
        return ResponseEntity.ok(ApiResponse.OK);
    }

    @PutMapping("/balls/{id}")
    public ResponseEntity<ApiResponse> changeBallsInfo(@PathVariable Long id, @Valid @RequestBody ChangeBallRequest request) {
        changeProductService.changeBallInfo(id, request);
        return ResponseEntity.ok(ApiResponse.OK);
    }

    @GetMapping
    public ResponseEntity<Page<FindProductResponse>> findProducts(Pageable pageable) {
        Page<FindProductResponse> products = findProductService.findProducts(pageable);
        return ResponseEntity.ok(products);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long id) {
        deleteProductService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.OK);
    }
}
