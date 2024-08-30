package org.example.eshop.dto;

import lombok.Builder;
import lombok.With;

@Builder
@With
public record ProductDto(long id, String name, double price) {
}
