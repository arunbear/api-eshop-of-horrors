package org.example.eshop.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.With;

import java.util.List;

@Builder
@With
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record CartDto(long cartId, boolean checkedOut, List<CartItemDto> products) {
}
