package com.tnc.study.tennisstore.application.product;

import com.tnc.study.tennisstore.domain.product.ProductRepository;
import com.tnc.study.tennisstore.domain.product.query.FindProductCondition;
import com.tnc.study.tennisstore.domain.product.query.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindProductService {
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;

    /**
     * 전체 상품 조회
     * @return
     */
    public Page<FindProductResponse> findProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> new FindProductResponse(
                        product.getId(),
                        product.getProductType(),
                        product.getName(),
                        product.getDescription(),
                        product.getBrand(),
                        product.getPrice().getAmount(),
                        product.getStockQuantity()
                ));
    }

    /**
     * 상품 조회 (검색 조건)
     *
     * @param condition
     * @param pageable
     * @return
     */
    public Page<FindProductResponse> findProductsByCondition(FindProductCondition condition, Pageable pageable) {
        return productQueryRepository.findProductByCondition(condition, pageable);
    }
}
