package com.application.domain.repository;

import com.application.domain.shared.EntityRepository;
import com.application.domain.model.User;
import com.application.domain.valueobject.UserId;
import java.util.Optional;

public interface UserRepository extends EntityRepository<User, UserId> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}