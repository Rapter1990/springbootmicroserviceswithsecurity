package com.springbootmicroservices.productservice.controller;

import com.springbootmicroservices.productservice.base.AbstractRestControllerTest;
import com.springbootmicroservices.productservice.model.common.CustomPage;
import com.springbootmicroservices.productservice.model.common.CustomPaging;
import com.springbootmicroservices.productservice.model.product.Product;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductCreateRequest;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductPagingRequest;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductUpdateRequest;
import com.springbootmicroservices.productservice.model.product.dto.response.ProductResponse;
import com.springbootmicroservices.productservice.model.product.entity.ProductEntity;
import com.springbootmicroservices.productservice.model.product.mapper.ProductToProductResponseMapper;
import com.springbootmicroservices.productservice.service.ProductCreateService;
import com.springbootmicroservices.productservice.service.ProductDeleteService;
import com.springbootmicroservices.productservice.service.ProductReadService;
import com.springbootmicroservices.productservice.service.ProductUpdateService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProductControllerTest extends AbstractRestControllerTest {

    @MockBean
    private ProductCreateService productCreateService;

    @MockBean
    private ProductReadService productReadService;

    @MockBean
    private ProductUpdateService productUpdateService;

    @MockBean
    private ProductDeleteService productDeleteService;

    private final ProductToProductResponseMapper productToProductResponseMapper = ProductToProductResponseMapper.initialize();

    @Test
    void givenProductCreateRequest_whenProductCreatedFromAdmin_thenReturnCustomResponseWithProductId() throws Exception {

        // Given
        String productName = "Test Product";

        ProductCreateRequest productCreateRequest = ProductCreateRequest.builder()
                .name(productName)
                .unitPrice(BigDecimal.valueOf(12))
                .amount(BigDecimal.valueOf(5))
                .build();

        Product expected = Product.builder()
                .id(UUID.randomUUID().toString())
                .name(productCreateRequest.getName())
                .unitPrice(productCreateRequest.getUnitPrice())
                .amount(productCreateRequest.getAmount())
                .build();

        // When
        when(productCreateService.createProduct(any(ProductCreateRequest.class))).thenReturn(expected);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productCreateRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response").value(expected.getId()));

        // Verify
        verify(productCreateService, times(1)).createProduct(any(ProductCreateRequest.class));

    }

    @Test
    void givenProductId_whenGetProductByIdFromAdmin_thenReturnProductResponse() throws Exception {

        // Given
        String productId = UUID.randomUUID().toString();

        Product expected = Product.builder()
                .id(productId)
                .name("Test Product")
                .unitPrice(BigDecimal.valueOf(12))
                .amount(BigDecimal.valueOf(5))
                .build();

        ProductResponse productResponse = productToProductResponseMapper.map(expected);

        // When
        when(productReadService.getProductById(productId)).thenReturn(expected);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(productResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(productResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.amount").value(productResponse.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.unitPrice").value(productResponse.getUnitPrice()));

        // Verify
        verify(productReadService, times(1)).getProductById(productId);

    }

    @Test
    void givenProductId_whenGetProductByIdFromUser_thenReturnProductResponse() throws Exception {

        // Given
        String productId = UUID.randomUUID().toString();

        Product expected = Product.builder()
                .id(productId)
                .name("Test Product")
                .unitPrice(BigDecimal.valueOf(12))
                .amount(BigDecimal.valueOf(5))
                .build();

        ProductResponse productResponse = productToProductResponseMapper.map(expected);

        // When
        when(productReadService.getProductById(productId)).thenReturn(expected);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(productResponse.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(productResponse.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.amount").value(productResponse.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.unitPrice").value(productResponse.getUnitPrice()));

        // Verify
        verify(productReadService, times(1)).getProductById(productId);

    }


    @Test
    void givenProductPagingRequest_whenGetProductsFromAdmin_thenReturnCustomPageProduct() throws Exception {

        // Given
        ProductPagingRequest pagingRequest = ProductPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        String productId = UUID.randomUUID().toString();

        ProductEntity expected = ProductEntity.builder()
                .id(productId)
                .name("Test Product")
                .unitPrice(BigDecimal.valueOf(12))
                .amount(BigDecimal.valueOf(5))
                .build();

        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.addAll(Collections.singletonList(expected));

        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities, PageRequest.of(1, 1), productEntities.size());

        List<Product> productDomainModels = productEntities.stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getAmount(),entity.getUnitPrice()))
                .collect(Collectors.toList());

        CustomPage<Product> productPage = CustomPage.of(productDomainModels, productEntityPage);

        // When
        when(productReadService.getProducts(any(ProductPagingRequest.class))).thenReturn(productPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].id").value(expected.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].name").value(expected.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].amount").value(expected.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].unitPrice").value(expected.getUnitPrice()));

        // Verify
        verify(productReadService, times(1)).getProducts(any(ProductPagingRequest.class));

    }

    @Test
    void givenProductPagingRequest_whenGetProductsFromUser_thenReturnCustomPageProduct() throws Exception {

        // Given
        ProductPagingRequest pagingRequest = ProductPagingRequest.builder()
                .pagination(
                        CustomPaging.builder()
                                .pageSize(1)
                                .pageNumber(1)
                                .build()
                ).build();

        String productId = UUID.randomUUID().toString();

        ProductEntity expected = ProductEntity.builder()
                .id(productId)
                .name("Test Product")
                .unitPrice(BigDecimal.valueOf(12))
                .amount(BigDecimal.valueOf(5))
                .build();

        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.addAll(Collections.singletonList(expected));

        Page<ProductEntity> productEntityPage = new PageImpl<>(productEntities, PageRequest.of(1, 1), productEntities.size());

        List<Product> productDomainModels = productEntities.stream()
                .map(entity -> new Product(entity.getId(), entity.getName(), entity.getAmount(),entity.getUnitPrice()))
                .collect(Collectors.toList());

        CustomPage<Product> productPage = CustomPage.of(productDomainModels, productEntityPage);

        // When
        when(productReadService.getProducts(any(ProductPagingRequest.class))).thenReturn(productPage);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].id").value(expected.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].name").value(expected.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].amount").value(expected.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.content[0].unitPrice").value(expected.getUnitPrice()));

        // Verify
        verify(productReadService, times(1)).getProducts(any(ProductPagingRequest.class));

    }

    @Test
    void givenProductUpdateRequest_WhenUpdateProductByFromAdmin_thenReturnUpdatedProduct() throws Exception {

        // Given
        String productId = UUID.randomUUID().toString();
        String updatedProductName = "Updated Product Name";

        ProductUpdateRequest productUpdateRequest = ProductUpdateRequest.builder()
                .name(updatedProductName)
                .amount(BigDecimal.valueOf(5))
                .unitPrice(BigDecimal.valueOf(12))
                .build();

        Product updatedProduct = Product.builder()
                .id(productId)
                .name(productUpdateRequest.getName())
                .unitPrice(productUpdateRequest.getUnitPrice())
                .amount(productUpdateRequest.getAmount())
                .build();

        // When
        when(productUpdateService.updateProductById(anyString(), any(ProductUpdateRequest.class))).thenReturn(updatedProduct);

        // Then
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productUpdateRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.id").value(updatedProduct.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.name").value(updatedProduct.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.amount").value(updatedProduct.getAmount()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.response.unitPrice").value(updatedProduct.getUnitPrice()));

        // Verify
        verify(productUpdateService, times(1)).updateProductById(anyString(), any(ProductUpdateRequest.class));

    }

    @Test
    void givenProductId_WhenDeleteProductByIdFromAdmin_thenReturnDeletedProduct() throws Exception {

        // Given
        String productId = UUID.randomUUID().toString();

        // Then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/products/{productId}", productId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.httpStatus").value("OK"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isSuccess").value(true));

        // Verify
        verify(productDeleteService, times(1)).deleteProductById(productId);

    }

}