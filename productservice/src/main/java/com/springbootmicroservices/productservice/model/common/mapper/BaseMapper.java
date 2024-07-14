package com.springbootmicroservices.productservice.model.common.mapper;

import java.util.Collection;
import java.util.List;

/**
 * Interface for mapping entities or domain models (S) to DTOs (T).
 *
 * @param <S> the source type to map from
 * @param <T> the target type to map to
 */
public interface BaseMapper<S, T> {

    /**
     * Maps a single source object to a target object.
     *
     * @param source the source object to map
     * @return the mapped target object
     */
    T map(S source);

    /**
     * Maps a collection of source objects to a list of target objects.
     *
     * @param sources the collection of source objects to map
     * @return the list of mapped target objects
     */
    List<T> map(Collection<S> sources);

}