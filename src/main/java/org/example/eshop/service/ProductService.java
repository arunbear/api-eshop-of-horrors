package org.example.eshop.service;

import org.example.eshop.dto.ProductDto;
import org.example.eshop.entity.Product;
import org.example.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public Product save(ProductDto productDto) {
        Product product = Product
                .builder()
                .name(productDto.name())
                .price(productDto.price())
                .build();
        return productRepository.save(product);
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}
