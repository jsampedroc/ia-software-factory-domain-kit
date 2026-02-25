package com.application.domain.shared;
import java.util.*;
public interface EntityRepository<T extends Entity<ID>, ID extends ValueObject> { T save(T entity); Optional<T> findById(ID id); List<T> findAll(); void delete(T entity); boolean existsById(ID id); }