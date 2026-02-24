package com.application.domain.shared;
import java.util.*;
public interface EntityRepository<T, ID> { T save(T e); Optional<T> findById(ID id); List<T> findAll(); void delete(T e); boolean existsById(ID id); }