package com.springbootmicroservices.productservice.model.product.dto.response;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductResponseTest {

    @Test
    void givenNoArgsConstructor_whenCreateInstance_thenNotNull() {

        // Given
        ProductResponse productResponse = new ProductResponse();

        // Then
        assertThat(productResponse).isNotNull();

    }

    @Test
    void givenAllArgsConstructor_whenCreateInstance_thenFieldsAreSet() {

        // Given
        String id = "123";
        String name = "Product Name";
        BigDecimal amount = new BigDecimal("10.50");
        BigDecimal unitPrice = new BigDecimal("2.25");

        // When
        ProductResponse productResponse = new ProductResponse(id, name, amount, unitPrice);

        // Then
        assertThat(productResponse.getId()).isEqualTo(id);
        assertThat(productResponse.getName()).isEqualTo(name);
        assertThat(productResponse.getAmount()).isEqualTo(amount);
        assertThat(productResponse.getUnitPrice()).isEqualTo(unitPrice);
    }

    @Test
    void givenSetterMethods_whenSetValues_thenGettersReturnCorrectValues() {
        // Given
        ProductResponse productResponse = new ProductResponse();
        String id = "123";
        String name = "Product Name";
        BigDecimal amount = new BigDecimal("10.50");
        BigDecimal unitPrice = new BigDecimal("2.25");

        // When
        productResponse.setId(id);
        productResponse.setName(name);
        productResponse.setAmount(amount);
        productResponse.setUnitPrice(unitPrice);

        // Then
        assertThat(productResponse.getId()).isEqualTo(id);
        assertThat(productResponse.getName()).isEqualTo(name);
        assertThat(productResponse.getAmount()).isEqualTo(amount);
        assertThat(productResponse.getUnitPrice()).isEqualTo(unitPrice);
    }

    @Test
    void givenBuilder_whenBuildObject_thenFieldsAreSet() {
        // Given
        String id = "123";
        String name = "Product Name";
        BigDecimal amount = new BigDecimal("10.50");
        BigDecimal unitPrice = new BigDecimal("2.25");

        // When
        ProductResponse productResponse = ProductResponse.builder()
                .id(id)
                .name(name)
                .amount(amount)
                .unitPrice(unitPrice)
                .build();

        // Then
        assertThat(productResponse.getId()).isEqualTo(id);
        assertThat(productResponse.getName()).isEqualTo(name);
        assertThat(productResponse.getAmount()).isEqualTo(amount);
        assertThat(productResponse.getUnitPrice()).isEqualTo(unitPrice);
    }

}