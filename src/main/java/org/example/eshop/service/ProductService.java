package org.example.eshop.service;

import org.example.eshop.InvalidProductLabelException;
import org.example.eshop.InvalidProductNameException;
import org.example.eshop.dto.ProductDto;
import org.example.eshop.entity.Label;
import org.example.eshop.entity.Product;
import org.example.eshop.repository.LabelRepository;
import org.example.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final LabelRepository   labelRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, LabelRepository labelRepository) {
        this.productRepository = productRepository;
        this.labelRepository = labelRepository;
    }

    public Product save(ProductDto productDto) {
        validateLabels(productDto);

        Product product = new Product();
        product.setName(productDto.name());
        product.setPrice(productDto.price());

        try {
            for (String label: productDto.labels()) {
                var productLabel = labelRepository
                    .findByName(label)
                    .orElseGet(() -> labelRepository.save(new Label(label)));
                product.addLabel(productLabel);
            }
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

    public List<ProductDto> findAll() {
        var products = productRepository.findAll();
        return products.stream().map(Product::toDto).toList();

    }

    public void delete(long id) {
        // We may want to do something if the product does not exist,
        // which this cannot do
        productRepository.deleteById(id);
    }
}
