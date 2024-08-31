package org.example.eshop.service;

import org.example.eshop.InvalidProductLabelException;
import org.example.eshop.InvalidProductNameException;
import org.example.eshop.dto.ProductDto;
import org.example.eshop.entity.Product;
import org.example.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

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
        validateLabels(productDto);

        Product product = Product
                .builder()
                .name(productDto.name())
                .price(productDto.price())
                .build();
        try {
            product = productRepository.save(product);
        }
        catch (DataIntegrityViolationException e) {
            // parsing error strings is brittle
            if (e.getMessage().contains("Value too long for column")) {
                throw new InvalidProductNameException("A product name cannot exceed 200 characters");
            }
            else if (e.getMessage().contains("Unique index or primary key violation")) {
                var message = String.format("A product with name <%s> already exists", productDto.name());
                throw new InvalidProductNameException(message);
            }
        }
        return product;
    }

    private void validateLabels(ProductDto productDto) {
        var allowedLabels = Set.of("food", "drink", "clothes", "limited");

        for (String label: productDto.labels()) {
            if (!allowedLabels.contains(label))
                throw new InvalidProductLabelException(label);
        }
    }

    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
}
