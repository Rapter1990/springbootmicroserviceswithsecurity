package com.springbootmicroservices.productservice.service;

/**
 * Service interface named {@link ProductDeleteService} for deleting products.
 */
public interface ProductDeleteService {

    /**
     * Deletes a product identified by its unique ID.
     *
     * @param productId The ID of the product to delete.
     */
    void deleteProductById(final String productId);

}
