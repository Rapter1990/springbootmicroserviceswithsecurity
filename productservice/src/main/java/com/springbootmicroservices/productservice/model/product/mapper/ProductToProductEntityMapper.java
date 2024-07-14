package com.springbootmicroservices.productservice.model.product.mapper;

import com.springbootmicroservices.productservice.model.common.mapper.BaseMapper;
import com.springbootmicroservices.productservice.model.product.Product;
import com.springbootmicroservices.productservice.model.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Mapper interface named {@link ProductToProductEntityMapper} for converting {@link Product} to {@link ProductEntity}.
 */
@Mapper
public interface ProductToProductEntityMapper extends BaseMapper<Product, ProductEntity> {

    /**
     * Maps Product to ProductEntity.
     *
     * @param source The Product object to map.
     * @return ProductEntity object containing mapped data.
     */
    @Override
    ProductEntity map(Product source);

    /**
     * Initializes and returns an instance of ProductToProductEntityMapper.
     *
     * @return Initialized ProductToProductEntityMapper instance.
     */
    static ProductToProductEntityMapper initialize() {
        return Mappers.getMapper(ProductToProductEntityMapper.class);
    }

}
