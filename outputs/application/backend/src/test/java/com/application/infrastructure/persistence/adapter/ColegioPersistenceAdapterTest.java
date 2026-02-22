package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.colegio.Colegio;
import com.application.domain.model.colegio.Clase;
import com.application.domain.valueobject.colegio.CodigoCentroEducativo;
import com.application.infrastructure.persistence.jpa.colegio.ColegioJpaEntity;
import com.application.infrastructure.persistence.jpa.colegio.ClaseJpaEntity;
import com.application.infrastructure.persistence.jpa.colegio.ColegioJpaRepository;
import com.application.infrastructure.persistence.jpa.colegio.ClaseJpaRepository;
import com.application.infrastructure.persistence.mapper.colegio.ColegioMapper;
import com.application.infrastructure.persistence.mapper.colegio.ClaseMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ColegioPersistenceAdapterTest {

    @Mock
    private ColegioJpaRepository colegioJpaRepository;
    @Mock
    private ClaseJpaRepository claseJpaRepository;
    @Mock
    private ColegioMapper colegioMapper;
    @Mock
    private ClaseMapper claseMapper;

    @InjectMocks
    private ColegioPersistenceAdapter underTest;

    private Colegio colegioDomain;
    private ColegioJpaEntity colegioJpaEntity;
    private Clase claseDomain;
    private ClaseJpaEntity claseJpaEntity;
    private UUID colegioId;
    private UUID claseId;

    @BeforeEach
    void setUp() {
        colegioId = UUID.randomUUID();
        claseId = UUID.randomUUID();

        colegioDomain = mock(Colegio.class);
        colegioJpaEntity = mock(ColegioJpaEntity.class);
        claseDomain = mock(Clase.class);
        claseJpaEntity = mock(ClaseJpaEntity.class);
    }

    @Test
    void saveColegio_ShouldSaveAndReturnDomainEntity() {
        when(colegioMapper.toJpaEntity(colegioDomain)).thenReturn(colegioJpaEntity);
        when(colegioJpaRepository.save(colegioJpaEntity)).thenReturn(colegioJpaEntity);
        when(colegioMapper.toDomain(colegioJpaEntity)).thenReturn(colegioDomain);

        Colegio result = underTest.saveColegio(colegioDomain);

        assertThat(result).isEqualTo(colegioDomain);
        verify(colegioJpaRepository).save(colegioJpaEntity);
    }

    @Test
    void findColegioById_WhenExists_ShouldReturnDomainEntity() {
        when(colegioJpaRepository.findById(colegioId)).thenReturn(Optional.of(colegioJpaEntity));
        when(colegioMapper.toDomain(colegioJpaEntity)).thenReturn(colegioDomain);

        Optional<Colegio> result = underTest.findColegioById(colegioId);

        assertThat(result).isPresent().contains(colegioDomain);
    }

    @Test
    void findColegioById_WhenNotExists_ShouldReturnEmpty() {
        when(colegioJpaRepository.findById(colegioId)).thenReturn(Optional.empty());

        Optional<Colegio> result = underTest.findColegioById(colegioId);

        assertThat(result).isEmpty();
    }

    @Test
    void findColegioByCodigoCentro_WhenExists_ShouldReturnDomainEntity() {
        String codigo = "COD123";
        CodigoCentroEducativo codigoVo = new CodigoCentroEducativo(codigo);
        when(colegioJpaRepository.findByCodigoCentro(codigo)).thenReturn(Optional.of(colegioJpaEntity));
        when(colegioMapper.toDomain(colegioJpaEntity)).thenReturn(colegioDomain);

        Optional<Colegio> result = underTest.findColegioByCodigoCentro(codigoVo);

        assertThat(result).isPresent().contains(colegioDomain);
    }

    @Test
    void findColegioByCodigoCentro_WhenNotExists_ShouldReturnEmpty() {
        String codigo = "COD123";
        CodigoCentroEducativo codigoVo = new CodigoCentroEducativo(codigo);
        when(colegioJpaRepository.findByCodigoCentro(codigo)).thenReturn(Optional.empty());

        Optional<Colegio> result = underTest.findColegioByCodigoCentro(codigoVo);

        assertThat(result).isEmpty();
    }

    @Test
    void findAllColegios_ShouldReturnList() {
        List<ColegioJpaEntity> jpaList = List.of(colegioJpaEntity);
        List<Colegio> domainList = List.of(colegioDomain);
        when(colegioJpaRepository.findAll()).thenReturn(jpaList);
        when(colegioMapper.toDomain(colegioJpaEntity)).thenReturn(colegioDomain);

        List<Colegio> result = underTest.findAllColegios();

        assertThat(result).isEqualTo(domainList);
    }

    @Test
    void deleteColegioById_ShouldCallRepository() {
        underTest.deleteColegioById(colegioId);

        verify(colegioJpaRepository).deleteById(colegioId);
    }

    @Test
    void existsColegioById_ShouldReturnRepositoryResult() {
        when(colegioJpaRepository.existsById(colegioId)).thenReturn(true);

        boolean result = underTest.existsColegioById(colegioId);

        assertThat(result).isTrue();
    }

    @Test
    void saveClase_ShouldSaveAndReturnDomainEntity() {
        when(claseMapper.toJpaEntity(claseDomain)).thenReturn(claseJpaEntity);
        when(claseJpaRepository.save(claseJpaEntity)).thenReturn(claseJpaEntity);
        when(claseMapper.toDomain(claseJpaEntity)).thenReturn(claseDomain);

        Clase result = underTest.saveClase(claseDomain);

        assertThat(result).isEqualTo(claseDomain);
        verify(claseJpaRepository).save(claseJpaEntity);
    }

    @Test
    void findClaseById_WhenExists_ShouldReturnDomainEntity() {
        when(claseJpaRepository.findById(claseId)).thenReturn(Optional.of(claseJpaEntity));
        when(claseMapper.toDomain(claseJpaEntity)).thenReturn(claseDomain);

        Optional<Clase> result = underTest.findClaseById(claseId);

        assertThat(result).isPresent().contains(claseDomain);
    }

    @Test
    void findClaseById_WhenNotExists_ShouldReturnEmpty() {
        when(claseJpaRepository.findById(claseId)).thenReturn(Optional.empty());

        Optional<Clase> result = underTest.findClaseById(claseId);

        assertThat(result).isEmpty();
    }

    @Test
    void findAllClasesByColegioId_ShouldReturnList() {
        List<ClaseJpaEntity> jpaList = List.of(claseJpaEntity);
        List<Clase> domainList = List.of(claseDomain);
        when(claseJpaRepository.findByColegioId(colegioId)).thenReturn(jpaList);
        when(claseMapper.toDomain(claseJpaEntity)).thenReturn(claseDomain);

        List<Clase> result = underTest.findAllClasesByColegioId(colegioId);

        assertThat(result).isEqualTo(domainList);
    }

    @Test
    void deleteClaseById_ShouldCallRepository() {
        underTest.deleteClaseById(claseId);

        verify(claseJpaRepository).deleteById(claseId);
    }

    @Test
    void existsClaseById_ShouldReturnRepositoryResult() {
        when(claseJpaRepository.existsById(claseId)).thenReturn(true);

        boolean result = underTest.existsClaseById(claseId);

        assertThat(result).isTrue();
    }
}