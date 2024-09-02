package org.example.eshop.service;

import org.example.eshop.dto.ProductDto;
import org.example.eshop.entity.Product;
import org.example.eshop.repository.ProductRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProductServiceTest {

    @Mock
    private ProductRepository mockProductRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void a_saved_DTO_is_saved_as_an_entity() {
        // Given
        ProductDto productDto = ProductDto
                .builder()
                .name("Acme T Shirt")
                .labels(Set.of())
                .build();
        // when
        productService.save(productDto);

        verify(mockProductRepository).save(any(Product.class));
    }

    @Test
    void a_product_repository_is_used_to_find_products() {
        // Given
        long productId = 1;

        // when
        productService.findById(productId);

        verify(mockProductRepository).findById(productId);
    }
}