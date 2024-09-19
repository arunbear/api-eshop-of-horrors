package org.example.eshop.error;

import lombok.Builder;

import java.util.List;

@Builder
public record ErrorResponseModel(String type, List<ErrorModel> errors) {

}