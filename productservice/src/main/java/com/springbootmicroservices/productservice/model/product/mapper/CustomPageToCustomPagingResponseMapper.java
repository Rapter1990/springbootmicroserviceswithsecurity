package com.springbootmicroservices.productservice.model.product.mapper;

import com.springbootmicroservices.productservice.model.common.CustomPage;
import com.springbootmicroservices.productservice.model.common.dto.response.CustomPagingResponse;
import com.springbootmicroservices.productservice.model.product.Product;
import com.springbootmicroservices.productservice.model.product.dto.response.ProductResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper interface named {@link CustomPageToCustomPagingResponseMapper} for converting {@link CustomPage<Product>} to {@link CustomPagingResponse<ProductResponse>}.
 */
@Mapper
public interface CustomPageToCustomPagingResponseMapper {

    ProductToProductResponseMapper productToProductResponseMapper = Mappers.getMapper(ProductToProductResponseMapper.class);

    /**
     * Converts a CustomPage<Product> object to CustomPagingResponse<ProductResponse>.
     *
     * @param productPage The CustomPage<Product> object to convert.
     * @return CustomPagingResponse<ProductResponse> object containing mapped data.
     */
    default CustomPagingResponse<ProductResponse> toPagingResponse(CustomPage<Product> productPage) {

        if (productPage == null) {
            return null;
        }

        return CustomPagingResponse.<ProductResponse>builder()
                .content(toProductResponseList(productPage.getContent()))
                .totalElementCount(productPage.getTotalElementCount())
                .totalPageCount(productPage.getTotalPageCount())
                .pageNumber(productPage.getPageNumber())
                .pageSize(productPage.getPageSize())
                .build();

    }

    /**
     * Converts a list of Product objects to a list of ProductResponse objects.
     *
     * @param products The list of Product objects to convert.
     * @return List of ProductResponse objects containing mapped data.
     */
    default List<ProductResponse> toProductResponseList(List<Product> products) {

        if (products == null) {
            return null;
        }

        return products.stream()
                .map(productToProductResponseMapper::map)
                .collect(Collectors.toList());

    }

    /**
     * Initializes and returns an instance of CustomPageToCustomPagingResponseMapper.
     *
     * @return Initialized CustomPageToCustomPagingResponseMapper instance.
     */
    static CustomPageToCustomPagingResponseMapper initialize() {
        return Mappers.getMapper(CustomPageToCustomPagingResponseMapper.class);
    }

}
