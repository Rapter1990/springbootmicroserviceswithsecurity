package com.springbootmicroservices.productservice.controller;

import com.springbootmicroservices.productservice.model.common.CustomPage;
import com.springbootmicroservices.productservice.model.common.dto.response.CustomPagingResponse;
import com.springbootmicroservices.productservice.model.common.dto.response.CustomResponse;
import com.springbootmicroservices.productservice.model.product.Product;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductCreateRequest;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductPagingRequest;
import com.springbootmicroservices.productservice.model.product.dto.request.ProductUpdateRequest;
import com.springbootmicroservices.productservice.model.product.dto.response.ProductResponse;
import com.springbootmicroservices.productservice.model.product.mapper.CustomPageToCustomPagingResponseMapper;
import com.springbootmicroservices.productservice.model.product.mapper.ProductToProductResponseMapper;
import com.springbootmicroservices.productservice.service.ProductCreateService;
import com.springbootmicroservices.productservice.service.ProductDeleteService;
import com.springbootmicroservices.productservice.service.ProductReadService;
import com.springbootmicroservices.productservice.service.ProductUpdateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductCreateService productCreateService;
    private final ProductReadService productReadService;
    private final ProductUpdateService productUpdateService;
    private final ProductDeleteService productDeleteService;

    private final ProductToProductResponseMapper productToProductResponseMapper = ProductToProductResponseMapper.initialize();

    private final CustomPageToCustomPagingResponseMapper customPageToCustomPagingResponseMapper =
            CustomPageToCustomPagingResponseMapper.initialize();

    @PostMapping
    public CustomResponse<String> createProduct(@RequestBody @Valid final ProductCreateRequest productCreateRequest) {

        final Product createdProduct = productCreateService
                .createProduct(productCreateRequest);

        return CustomResponse.successOf(createdProduct.getId());
    }

    @GetMapping("/{productId}")
    public CustomResponse<ProductResponse> getProductById(@PathVariable @UUID final String productId) {

        final Product product = productReadService.getProductById(productId);

        final ProductResponse productResponse = productToProductResponseMapper.map(product);

        return CustomResponse.successOf(productResponse);

    }

    @GetMapping
    public CustomResponse<CustomPagingResponse<ProductResponse>> getProducts(
            @RequestBody @Valid final ProductPagingRequest productPagingRequest) {

        final CustomPage<Product> productPage = productReadService.getProducts(productPagingRequest);

        final CustomPagingResponse<ProductResponse> productPagingResponse =
                customPageToCustomPagingResponseMapper.toPagingResponse(productPage);

        return CustomResponse.successOf(productPagingResponse);

    }

    @PutMapping("/{productId}")
    public CustomResponse<ProductResponse> updatedProductById(
            @RequestBody @Valid final ProductUpdateRequest productUpdateRequest,
            @PathVariable @UUID final String productId) {

        final Product updatedProduct = productUpdateService.updateProductById(productId, productUpdateRequest);

        final ProductResponse productResponse = productToProductResponseMapper.map(updatedProduct);

        return CustomResponse.successOf(productResponse);
    }

    @DeleteMapping("/{productId}")
    public CustomResponse<Void> deleteProductById(@PathVariable @UUID final String productId) {

        productDeleteService.deleteProductById(productId);
        return CustomResponse.SUCCESS;
    }

}
