package org.example.eshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.eshop.dto.ProductDto;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, length = 200)
    private String name;

    private double price;

    @CreationTimestamp
    @Column(columnDefinition = "DATE")
    private LocalDate addedAt;

    @ManyToMany
    private Set<Label> labels = new HashSet<>();

    public String getAddedAtAsString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return addedAt.format(formatter);
    }

    public void addLabel(Label label) {
        labels.add(label);
    }

    public ProductDto toDto() {
        return ProductDto.builder()
            .productId(id)
            .name(name)
            .price(price)
            .addedAt(getAddedAtAsString())
            .labels(labels.stream().map(Label::getName).collect(Collectors.toSet()))
            .build();
    }
}