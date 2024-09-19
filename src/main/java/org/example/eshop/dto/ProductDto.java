package org.example.eshop.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.With;

import java.util.Set;

@Builder
@With
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ProductDto(
        long productId,

        @NotBlank(message = "must be present")
        String name,
        double price,
        String addedAt,
        Set<String> labels
) {
}
