package org.example.eshop.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.*;
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

        @DecimalMin("0.10") // arbitrary, but avoids zero prices
        double price,

        String addedAt,
        Set<String> labels
) {
}
