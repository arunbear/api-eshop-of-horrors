package org.example.eshop;

import org.example.eshop.dto.ProductDto;
import org.example.eshop.entity.Product;
import org.example.eshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {

        var savedProduct   = productService.save(productDto);
        var createdProduct = productDto
            .withId(savedProduct.getId())
            .withAddedAt(savedProduct.getAddedAtAsString());

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdProduct.id())
            .toUri();

        return ResponseEntity.created(uri).body(createdProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> retrieve(@PathVariable long id) {
        Optional<Product> products = productService.findById(id);
        if (products.isEmpty()) {
             return ResponseEntity.notFound().build();
        }
        else {
            return ResponseEntity.ok(products.get().toDto());
        }
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> retrieveAll() {
        List<ProductDto> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

}
