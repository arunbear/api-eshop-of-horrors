package org.example.eshop.dto;

import lombok.Builder;
import lombok.With;

import java.util.Set;

@Builder
@With
public record ProductDto(long productId, String name, double price, String addedAt, Set<String> labels) {
}
