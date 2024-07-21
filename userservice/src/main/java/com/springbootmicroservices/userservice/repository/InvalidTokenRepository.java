package com.springbootmicroservices.userservice.repository;

import com.springbootmicroservices.userservice.model.user.entity.InvalidTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository interface for managing {@link InvalidTokenEntity} instances.
 * Provides CRUD operations and additional query methods for {@link InvalidTokenEntity}.
 */
public interface InvalidTokenRepository extends JpaRepository<InvalidTokenEntity, String> {

    /**
     * Finds an {@link InvalidTokenEntity} by its token ID.
     *
     * @param tokenId the token ID to search for.
     * @return an {@link Optional} containing the {@link InvalidTokenEntity} if found, or {@link Optional#empty()} if not.
     */
    Optional<InvalidTokenEntity> findByTokenId(final String tokenId);

}
