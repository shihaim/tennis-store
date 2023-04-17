package com.tnc.study.tennisstore.application.product;

import com.tnc.study.tennisstore.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindProductService {
    private final ProductRepository productRepository;

    /**
     * 전체 상품 조회
     * @return
     */
    public List<FindProductResponse> findProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> new FindProductResponse(
                        product.getId(),
                        product.getProductType(),
                        product.getName(),
                        product.getDescription(),
                        product.getBrand(),
                        product.getPrice().getAmount(),
                        product.getStockQuantity()
                ))
                .toList();
    }
}
