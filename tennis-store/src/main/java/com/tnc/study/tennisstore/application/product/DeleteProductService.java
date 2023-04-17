package com.tnc.study.tennisstore.application.product;

import com.tnc.study.tennisstore.domain.product.Product;
import com.tnc.study.tennisstore.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class DeleteProductService {
    private final ProductRepository productRepository;

    public void deleteProduct(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        Product product = optionalProduct.orElseThrow(() -> new NoProductException("해당 상품을 찾을 수 없습니다."));
        productRepository.delete(product);
    }
}
