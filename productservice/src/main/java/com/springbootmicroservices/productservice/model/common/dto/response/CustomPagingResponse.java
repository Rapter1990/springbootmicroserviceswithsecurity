package com.springbootmicroservices.productservice.model.common.dto.response;

import com.springbootmicroservices.productservice.model.common.CustomPage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Represents a response object for custom paging named {@link CustomPagingResponse<T>}.
 * Contains content list, page number, page size, total element count, and total page count.
 *
 * @param <T> Type of content in the response.
 */
@Getter
@Builder
public class CustomPagingResponse<T> {

    private List<T> content;

    private Integer pageNumber;

    private Integer pageSize;

    private Long totalElementCount;

    private Integer totalPageCount;

    /**
     * Builder class for CustomPagingResponse, allowing initialization from a CustomPage object.
     *
     * @param <T> Type of content in the response.
     */
    public static class CustomPagingResponseBuilder<T> {

        /**
         * Initializes the builder with data from a CustomPage object.
         *
         * @param customPage CustomPage object containing pagination data.
         * @return CustomPagingResponseBuilder initialized with pagination details.
         */
        public <C> CustomPagingResponseBuilder<T> of(final CustomPage<C> customPage) {
            return CustomPagingResponse.<T>builder()
                    .pageNumber(customPage.getPageNumber())
                    .pageSize(customPage.getPageSize())
                    .totalElementCount(customPage.getTotalElementCount())
                    .totalPageCount(customPage.getTotalPageCount());
        }

    }

}
