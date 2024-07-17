package com.springbootmicroservices.userservice.builder;

import lombok.SneakyThrows;

public abstract class BaseBuilder<T> {

    @SneakyThrows
    public BaseBuilder(Class<T> clazz) {
        this.data = clazz.getDeclaredConstructor().newInstance();
    }

    T data;

    public T build() {
        return data;
    }

}
