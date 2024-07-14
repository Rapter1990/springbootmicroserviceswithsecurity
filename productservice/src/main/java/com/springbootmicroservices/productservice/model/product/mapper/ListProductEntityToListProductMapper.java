package com.springbootmicroservices.productservice.model.product.mapper;

import com.springbootmicroservices.productservice.model.product.Product;
import com.springbootmicroservices.productservice.model.product.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper interface named {@link ListProductEntityToListProductMapper} for converting {@link List<ProductEntity>} to {@link List<Product>}.
 */
@Mapper
public interface ListProductEntityToListProductMapper {

    ProductEntityToProductMapper productEntityToProductMapper = Mappers.getMapper(ProductEntityToProductMapper.class);

    /**
     * Converts a list of ProductEntity objects to a list of Product objects.
     *
     * @param productEntities The list of ProductEntity objects to convert.
     * @return List of Product objects containing mapped data.
     */
    default List<Product> toProductList(List<ProductEntity> productEntities) {

        if (productEntities == null) {
            return null;
        }

        return productEntities.stream()
                .map(productEntityToProductMapper::map)
                .collect(Collectors.toList());

    }

    /**
     * Initializes and returns an instance of ListProductEntityToListProductMapper.
     *
     * @return Initialized ListProductEntityToListProductMapper instance.
     */
    static ListProductEntityToListProductMapper initialize() {
        return Mappers.getMapper(ListProductEntityToListProductMapper.class);
    }

}
