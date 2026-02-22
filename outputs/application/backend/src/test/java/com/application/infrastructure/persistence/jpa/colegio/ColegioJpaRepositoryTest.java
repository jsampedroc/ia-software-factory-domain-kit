package com.application.infrastructure.persistence.jpa.colegio;

import com.application.domain.model.colegio.Colegio;
import com.application.domain.valueobject.colegio.CodigoCentroEducativo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ColegioJpaRepositoryTest {

    @Mock
    private ColegioJpaRepository colegioJpaRepository;

    private ColegioJpaEntity colegioJpaEntity;
    private Colegio colegioDomain;
    private final Long TEST_ID = 1L;
    private final String TEST_CODIGO = "COD123";

    @BeforeEach
    void setUp() {
        colegioJpaEntity = new ColegioJpaEntity();
        colegioJpaEntity.setId(TEST_ID);
        colegioJpaEntity.setCodigoCentro(TEST_CODIGO);
        colegioJpaEntity.setNombre("Colegio Test");
        colegioJpaEntity.setActivo(true);

        colegioDomain = Colegio.builder()
                .id(new Colegio.ColegioId(TEST_ID))
                .codigoCentro(new CodigoCentroEducativo(TEST_CODIGO))
                .nombre("Colegio Test")
                .activo(true)
                .build();
    }

    @Test
    void findById_shouldReturnEntityWhenExists() {
        when(colegioJpaRepository.findById(TEST_ID)).thenReturn(Optional.of(colegioJpaEntity));

        Optional<ColegioJpaEntity> result = colegioJpaRepository.findById(TEST_ID);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(TEST_ID);
        verify(colegioJpaRepository).findById(TEST_ID);
    }

    @Test
    void findById_shouldReturnEmptyWhenNotExists() {
        when(colegioJpaRepository.findById(TEST_ID)).thenReturn(Optional.empty());

        Optional<ColegioJpaEntity> result = colegioJpaRepository.findById(TEST_ID);

        assertThat(result).isEmpty();
        verify(colegioJpaRepository).findById(TEST_ID);
    }

    @Test
    void findByCodigoCentro_shouldReturnEntityWhenExists() {
        when(colegioJpaRepository.findByCodigoCentro(TEST_CODIGO)).thenReturn(Optional.of(colegioJpaEntity));

        Optional<ColegioJpaEntity> result = colegioJpaRepository.findByCodigoCentro(TEST_CODIGO);

        assertThat(result).isPresent();
        assertThat(result.get().getCodigoCentro()).isEqualTo(TEST_CODIGO);
        verify(colegioJpaRepository).findByCodigoCentro(TEST_CODIGO);
    }

    @Test
    void findByCodigoCentro_shouldReturnEmptyWhenNotExists() {
        when(colegioJpaRepository.findByCodigoCentro(TEST_CODIGO)).thenReturn(Optional.empty());

        Optional<ColegioJpaEntity> result = colegioJpaRepository.findByCodigoCentro(TEST_CODIGO);

        assertThat(result).isEmpty();
        verify(colegioJpaRepository).findByCodigoCentro(TEST_CODIGO);
    }

    @Test
    void findByActivoTrue_shouldReturnPageOfEntities() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<ColegioJpaEntity> expectedPage = new PageImpl<>(List.of(colegioJpaEntity));
        when(colegioJpaRepository.findByActivoTrue(eq(pageable))).thenReturn(expectedPage);

        Page<ColegioJpaEntity> result = colegioJpaRepository.findByActivoTrue(pageable);

        assertThat(result).isNotEmpty();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).isActivo()).isTrue();
        verify(colegioJpaRepository).findByActivoTrue(pageable);
    }

    @Test
    void existsByCodigoCentro_shouldReturnTrueWhenExists() {
        when(colegioJpaRepository.existsByCodigoCentro(TEST_CODIGO)).thenReturn(true);

        boolean result = colegioJpaRepository.existsByCodigoCentro(TEST_CODIGO);

        assertThat(result).isTrue();
        verify(colegioJpaRepository).existsByCodigoCentro(TEST_CODIGO);
    }

    @Test
    void existsByCodigoCentro_shouldReturnFalseWhenNotExists() {
        when(colegioJpaRepository.existsByCodigoCentro(TEST_CODIGO)).thenReturn(false);

        boolean result = colegioJpaRepository.existsByCodigoCentro(TEST_CODIGO);

        assertThat(result).isFalse();
        verify(colegioJpaRepository).existsByCodigoCentro(TEST_CODIGO);
    }

    @Test
    void save_shouldPersistEntity() {
        when(colegioJpaRepository.save(any(ColegioJpaEntity.class))).thenReturn(colegioJpaEntity);

        ColegioJpaEntity savedEntity = colegioJpaRepository.save(colegioJpaEntity);

        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(TEST_ID);
        verify(colegioJpaRepository).save(colegioJpaEntity);
    }

    @Test
    void deleteById_shouldInvokeDelete() {
        colegioJpaRepository.deleteById(TEST_ID);
        verify(colegioJpaRepository).deleteById(TEST_ID);
    }

    @Test
    void countByActivoTrue_shouldReturnCount() {
        when(colegioJpaRepository.countByActivoTrue()).thenReturn(5L);

        long count = colegioJpaRepository.countByActivoTrue();

        assertThat(count).isEqualTo(5L);
        verify(colegioJpaRepository).countByActivoTrue();
    }
}