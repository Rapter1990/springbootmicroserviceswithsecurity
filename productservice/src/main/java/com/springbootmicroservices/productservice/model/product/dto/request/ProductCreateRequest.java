package com.springbootmicroservices.productservice.model.product.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

/**
 * Represents a request object for creating a new product as {@link ProductCreateRequest}.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCreateRequest {

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
