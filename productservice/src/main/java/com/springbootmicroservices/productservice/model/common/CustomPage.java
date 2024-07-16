package com.springbootmicroservices.productservice.model.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Represents a paginated response named {@link CustomPage<T>} containing content and pagination details.
 *
 * @param <T> the type of content in the page
 */
@Getter
@Builder
@AllArgsConstructor
public class CustomPage<T> {
    private List<T> content;

    private Integer pageNumber;

    private Integer pageSize;

    private Long totalElementCount;

    private Integer totalPageCount;

    /**
     * Constructs a CustomPage instance from a domain model page.
     *
     * @param domainModels the list of domain models in the page
     * @param page         the Page object containing pagination information
     * @param <C>          the type of domain model in the page
     * @param <X>          the type of page
     * @return a CustomPage instance mapped from the provided Page
     */
    public static <C, X> CustomPage<C> of(final List<C> domainModels, final Page<X> page) {
        return CustomPage.<C>builder()
                .content(domainModels)
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalPageCount(page.getTotalPages())
                .totalElementCount(page.getTotalElements())
                .build();
    }

}
