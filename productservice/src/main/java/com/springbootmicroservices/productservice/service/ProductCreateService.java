package com.springbootmicroservices.productservice.service;

import com.springbootmicroservices.productservice.model.product.Product;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductCreateRequest;

/**
 * Service interface named {@link ProductCreateService} for creating products.
 */
public interface ProductCreateService {

    /**
     * Creates a new product based on the provided product creation request.
     *
     * @param productCreateRequest The request containing data to create the product.
     * @return The created Product object.
     */
    Product createProduct(final ProductCreateRequest productCreateRequest);

}