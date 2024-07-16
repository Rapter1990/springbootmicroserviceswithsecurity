package com.springbootmicroservices.productservice.service.impl;

import com.springbootmicroservices.productservice.base.AbstractBaseServiceTest;
import com.springbootmicroservices.productservice.exception.ProductAlreadyExistException;
import com.springbootmicroservices.productservice.exception.ProductNotFoundException;
import com.springbootmicroservices.productservice.model.product.Product;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductUpdateRequest;
import com.springbootmicroservices.productservice.model.product.entity.ProductEntity;
import com.springbootmicroservices.productservice.model.product.mapper.ProductEntityToProductMapper;
import com.springbootmicroservices.productservice.model.product.mapper.ProductUpdateRequestToProductEntityMapper;
import com.springbootmicroservices.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductUpdateServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private ProductUpdateServiceImpl productUpdateService;

    @Mock
    private ProductRepository productRepository;

    private ProductUpdateRequestToProductEntityMapper productUpdateRequestToProductEntityMapper =
            ProductUpdateRequestToProductEntityMapper.initialize();

    private ProductEntityToProductMapper productEntityToProductMapper =
            ProductEntityToProductMapper.initialize();

    @Test
    void givenProductUpdateRequest_whenProductUpdated_thenReturnProduct() {

        // Given
        String productId = "1";
        String newProductName = "New Product Name";

        ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder()
                .name(newProductName)
                .amount(BigDecimal.valueOf(5))
                .unitPrice(BigDecimal.valueOf(12))
                .build();

        ProductEntity existingProductEntity = ProductEntity.builder()
                .id(productId)
                .name(productUpdateRequest.getName())
                .unitPrice(productUpdateRequest.getUnitPrice())
                .amount(productUpdateRequest.getAmount())
                .build();

        productUpdateRequestToProductEntityMapper.mapForUpdating(existingProductEntity,productUpdateRequest);

        Product expected = productEntityToProductMapper.map(existingProductEntity);

        // When
        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProductEntity));
        when(productRepository.existsProductEntityByName(newProductName)).thenReturn(false);
        when(productRepository.save(any(ProductEntity.class))).thenReturn(existingProductEntity);

        // Then
        Product updatedProduct = productUpdateService.updateProductById(productId, productUpdateRequest);

        // Then
        assertNotNull(updatedProduct);
        assertEquals(expected.getId(), updatedProduct.getId());
        assertEquals(expected.getName(), updatedProduct.getName());
        assertEquals(expected.getAmount(), updatedProduct.getAmount());
        assertEquals(expected.getUnitPrice(), updatedProduct.getUnitPrice());

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, times(1)).existsProductEntityByName(newProductName);
        verify(productRepository, times(1)).save(any(ProductEntity.class));

    }

    @Test
    void givenProductUpdateRequest_whenProductNotFound_thenThrowProductNotFoundException() {

        // Given
        String productId = "1";
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ProductNotFoundException.class, () -> productUpdateService.updateProductById(productId, productUpdateRequest));

        // Verify
        verify(productRepository, times(1)).findById(productId);
        verify(productRepository, never()).existsProductEntityByName(anyString());
        verify(productRepository, never()).save(any(ProductEntity.class));

    }

    @Test
    void givenProductUpdateRequest_whenProductAlreadyExist_thenThrowProductAlreadyExistException() {

        // Given
        String productId = "1";
        String existingProductName = "Existing Product";
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest();
        productUpdateRequest.setName(existingProductName);

        ProductEntity existingProductEntity = new ProductEntity();
        existingProductEntity.setId(productId);
        existingProductEntity.setName(existingProductName);

        when(productRepository.existsProductEntityByName(existingProductName)).thenReturn(true);

        // When/Then
        assertThrows(ProductAlreadyExistException.class, () -> productUpdateService.updateProductById(productId, productUpdateRequest));

        // Verify
        verify(productRepository, times(1)).existsProductEntityByName(existingProductName);
        verify(productRepository, never()).findById(productId);
        verify(productRepository, never()).save(any(ProductEntity.class));

    }

}