package com.springbootmicroservices.productservice.model.product.mapper;

import com.springbootmicroservices.productservice.model.common.mapper.BaseMapper;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductCreateRequest;
import com.springbootmicroservices.productservice.model.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface named {@link ProductCreateRequestToProductEntityMapper} for converting {@link ProductCreateRequest} to {@link ProductEntity}.
 */
@Mapper
public interface ProductCreateRequestToProductEntityMapper extends BaseMapper<ProductCreateRequest, ProductEntity> {

    /**
     * Maps ProductCreateRequest to ProductEntity for saving purposes.
     *
     * @param productCreateRequest The ProductCreateRequest object to map.
     * @return ProductEntity object containing mapped data.
     */
    @Named("mapForSaving")
    default ProductEntity mapForSaving(ProductCreateRequest productCreateRequest) {
        return ProductEntity.builder()
                .amount(productCreateRequest.getAmount())
                .name(productCreateRequest.getName())
                .unitPrice(productCreateRequest.getUnitPrice())
                .build();
    }

    /**
     * Initializes and returns an instance of ProductCreateRequestToProductEntityMapper.
     *
     * @return Initialized ProductCreateRequestToProductEntityMapper instance.
     */
    static ProductCreateRequestToProductEntityMapper initialize() {
        return Mappers.getMapper(ProductCreateRequestToProductEntityMapper.class);
    }

}
