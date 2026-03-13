package io.github.yhugorocha.product.service;

import io.github.yhugorocha.product.dto.ProductRequest;
import io.github.yhugorocha.product.dto.ProductResponse;
import io.github.yhugorocha.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse save(ProductRequest request) {
        var product = productRepository.save(request.toEntity());
        return ProductResponse.fromEntity(product);
    }

    public ProductResponse findById(Long id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return ProductResponse.fromEntity(product);
    }

    public List<ProductResponse> findAllProducts() {
        var products = productRepository.findAll();
        return products.stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    public List<ProductResponse> validateProduct(List<Long> ids) {
        var products = productRepository.findAllById(ids);

        if(ids.size() != products.size()) {
            throw new RuntimeException("One or more products not found");
        }

        return products.stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }
}
