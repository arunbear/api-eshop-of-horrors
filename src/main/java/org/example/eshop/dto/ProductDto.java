package org.example.eshop.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.With;

import java.util.Set;

@Builder
@With
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductDto(long productId, String name, double price, String addedAt, Set<String> labels) {
}
