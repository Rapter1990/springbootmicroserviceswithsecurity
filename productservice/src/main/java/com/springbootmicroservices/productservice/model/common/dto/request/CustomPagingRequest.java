package com.springbootmicroservices.productservice.model.common.dto.request;

import com.springbootmicroservices.productservice.model.common.CustomPaging;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 * Represents a request object named {@link CustomPagingRequest} for custom paging configuration.
 * Includes pagination details using CustomPaging.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class CustomPagingRequest {

    private CustomPaging pagination;

    /**
     * Converts CustomPagingRequest to a Pageable object used for pagination in queries.
     *
     * @return Pageable object configured with page number and page size.
     */
    public Pageable toPageable() {
        return PageRequest.of(
                Math.toIntExact(pagination.getPageNumber()),
                Math.toIntExact(pagination.getPageSize())
        );
    }

}
