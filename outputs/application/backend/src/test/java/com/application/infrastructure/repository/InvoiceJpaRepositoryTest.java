package com.application.infrastructure.repository;

import com.application.infrastructure.entity.InvoiceEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceJpaRepositoryTest {

    @Mock
    private InvoiceJpaRepository invoiceJpaRepository;

    @Test
    void shouldSaveInvoiceEntity() {
        // Given
        InvoiceEntity invoiceEntity = createTestInvoiceEntity();
        when(invoiceJpaRepository.save(any(InvoiceEntity.class))).thenReturn(invoiceEntity);

        // When
        InvoiceEntity savedEntity = invoiceJpaRepository.save(invoiceEntity);

        // Then
        assertThat(savedEntity).isNotNull();
        assertThat(savedEntity.getId()).isEqualTo(invoiceEntity.getId());
        assertThat(savedEntity.getNumeroFactura()).isEqualTo("FAC-2024-001");
        verify(invoiceJpaRepository, times(1)).save(invoiceEntity);
    }

    @Test
    void shouldFindInvoiceEntityById() {
        // Given
        UUID invoiceId = UUID.randomUUID();
        InvoiceEntity invoiceEntity = createTestInvoiceEntity();
        invoiceEntity.setId(invoiceId);
        when(invoiceJpaRepository.findById(invoiceId)).thenReturn(Optional.of(invoiceEntity));

        // When
        Optional<InvoiceEntity> foundEntity = invoiceJpaRepository.findById(invoiceId);

        // Then
        assertThat(foundEntity).isPresent();
        assertThat(foundEntity.get().getId()).isEqualTo(invoiceId);
        verify(invoiceJpaRepository, times(1)).findById(invoiceId);
    }

    @Test
    void shouldReturnEmptyWhenInvoiceEntityNotFound() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(invoiceJpaRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When
        Optional<InvoiceEntity> foundEntity = invoiceJpaRepository.findById(nonExistentId);

        // Then
        assertThat(foundEntity).isEmpty();
        verify(invoiceJpaRepository, times(1)).findById(nonExistentId);
    }

    @Test
    void shouldFindAllInvoiceEntities() {
        // Given
        InvoiceEntity invoice1 = createTestInvoiceEntity();
        InvoiceEntity invoice2 = createTestInvoiceEntity();
        invoice2.setId(UUID.randomUUID());
        invoice2.setNumeroFactura("FAC-2024-002");
        List<InvoiceEntity> invoiceList = List.of(invoice1, invoice2);
        when(invoiceJpaRepository.findAll()).thenReturn(invoiceList);

        // When
        List<InvoiceEntity> allInvoices = invoiceJpaRepository.findAll();

        // Then
        assertThat(allInvoices).hasSize(2);
        assertThat(allInvoices).containsExactlyInAnyOrder(invoice1, invoice2);
        verify(invoiceJpaRepository, times(1)).findAll();
    }

    @Test
    void shouldFindAllInvoiceEntitiesWithPagination() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        InvoiceEntity invoiceEntity = createTestInvoiceEntity();
        Page<InvoiceEntity> invoicePage = new PageImpl<>(List.of(invoiceEntity), pageable, 1);
        when(invoiceJpaRepository.findAll(pageable)).thenReturn(invoicePage);

        // When
        Page<InvoiceEntity> resultPage = invoiceJpaRepository.findAll(pageable);

        // Then
        assertThat(resultPage).isNotNull();
        assertThat(resultPage.getContent()).hasSize(1);
        assertThat(resultPage.getContent().get(0)).isEqualTo(invoiceEntity);
        verify(invoiceJpaRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldDeleteInvoiceEntityById() {
        // Given
        UUID invoiceId = UUID.randomUUID();

        // When
        invoiceJpaRepository.deleteById(invoiceId);

        // Then
        verify(invoiceJpaRepository, times(1)).deleteById(invoiceId);
    }

    @Test
    void shouldCheckIfInvoiceEntityExistsById() {
        // Given
        UUID invoiceId = UUID.randomUUID();
        when(invoiceJpaRepository.existsById(invoiceId)).thenReturn(true);

        // When
        boolean exists = invoiceJpaRepository.existsById(invoiceId);

        // Then
        assertThat(exists).isTrue();
        verify(invoiceJpaRepository, times(1)).existsById(invoiceId);
    }

    @Test
    void shouldReturnFalseWhenInvoiceEntityDoesNotExist() {
        // Given
        UUID invoiceId = UUID.randomUUID();
        when(invoiceJpaRepository.existsById(invoiceId)).thenReturn(false);

        // When
        boolean exists = invoiceJpaRepository.existsById(invoiceId);

        // Then
        assertThat(exists).isFalse();
        verify(invoiceJpaRepository, times(1)).existsById(invoiceId);
    }

    @Test
    void shouldCountInvoiceEntities() {
        // Given
        when(invoiceJpaRepository.count()).thenReturn(5L);

        // When
        long count = invoiceJpaRepository.count();

        // Then
        assertThat(count).isEqualTo(5L);
        verify(invoiceJpaRepository, times(1)).count();
    }

    private InvoiceEntity createTestInvoiceEntity() {
        InvoiceEntity entity = new InvoiceEntity();
        entity.setId(UUID.randomUUID());
        entity.setNumeroFactura("FAC-2024-001");
        entity.setFechaEmision(LocalDate.now());
        entity.setFechaVencimiento(LocalDate.now().plusDays(30));
        entity.setSubtotal(new BigDecimal("100.00"));
        entity.setImpuestos(new BigDecimal("21.00"));
        entity.setTotal(new BigDecimal("121.00"));
        entity.setEstado("PENDIENTE");
        entity.setMetodoPago("TARJETA");
        return entity;
    }
}