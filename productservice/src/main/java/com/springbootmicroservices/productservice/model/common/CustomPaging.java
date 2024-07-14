package com.springbootmicroservices.productservice.model.common;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * Represents pagination parameters for querying data as {@link CustomPaging}.
 */
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class CustomPaging {

    @Min(value = 1, message = "Page number must be bigger than 0")
    private Integer pageNumber;

    @Min(value = 1, message = "Page size must be bigger than 0")
    private Integer pageSize;

    /**
     * Returns the zero-based page number for internal processing.
     *
     * @return the zero-based page number derived from the specified page number
     */
    public Integer getPageNumber() {
        return pageNumber - 1;
    }

}