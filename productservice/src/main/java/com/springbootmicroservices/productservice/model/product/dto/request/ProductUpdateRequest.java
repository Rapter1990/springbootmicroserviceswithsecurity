package com.springbootmicroservices.productservice.model.product.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents a request object for updating an existing product as {@link ProductUpdateRequest}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {

    @Size(
            min = 1,
            message = "Product name can't be blank."
    )
    private String name;

    @DecimalMin(
            value = "0.0001",
            message = "Amount must be bigger than 0"
    )
    private BigDecimal amount;

    @DecimalMin(
            value = "0.0001",
            message = "Unit Price must be bigger than 0"
    )
    private BigDecimal unitPrice;

}
