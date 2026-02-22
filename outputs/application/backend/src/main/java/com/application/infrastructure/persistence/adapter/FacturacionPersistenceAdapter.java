package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.facturacion.Factura;
import com.application.domain.model.facturacion.FacturaId;
import com.application.domain.model.facturacion.LineaFactura;
import com.application.domain.model.facturacion.LineaFacturaId;
import com.application.domain.model.facturacion.Tarifa;
import com.application.domain.model.facturacion.TarifaId;
import com.application.infrastructure.persistence.jpa.facturacion.FacturaJpaEntity;
import com.application.infrastructure.persistence.jpa.facturacion.FacturaJpaRepository;
import com.application.infrastructure.persistence.jpa.facturacion.LineaFacturaJpaEntity;
import com.application.infrastructure.persistence.jpa.facturacion.LineaFacturaJpaRepository;
import com.application.infrastructure.persistence.jpa.facturacion.TarifaJpaEntity;
import com.application.infrastructure.persistence.jpa.facturacion.TarifaJpaRepository;
import com.application.domain.shared.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FacturacionPersistenceAdapter implements Repository<Factura, FacturaId>, Repository<Tarifa, TarifaId> {

    private final FacturaJpaRepository facturaJpaRepository;
    private final LineaFacturaJpaRepository lineaFacturaJpaRepository;
    private final TarifaJpaRepository tarifaJpaRepository;

    @Override
    public Factura save(Factura factura) {
        FacturaJpaEntity jpaEntity = FacturaJpaEntity.fromDomain(factura);
        FacturaJpaEntity savedEntity = facturaJpaRepository.save(jpaEntity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Factura> findById(FacturaId id) {
        return facturaJpaRepository.findById(id.value())
                .map(FacturaJpaEntity::toDomain);
    }

    @Override
    public void delete(Factura factura) {
        facturaJpaRepository.deleteById(factura.getId().value());
    }

    @Override
    public List<Factura> findAll() {
        return facturaJpaRepository.findAll().stream()
                .map(FacturaJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    public List<LineaFactura> findLineasByFacturaId(FacturaId facturaId) {
        return lineaFacturaJpaRepository.findByFacturaId(facturaId.value()).stream()
                .map(LineaFacturaJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    public LineaFactura saveLinea(LineaFactura lineaFactura) {
        LineaFacturaJpaEntity jpaEntity = LineaFacturaJpaEntity.fromDomain(lineaFactura);
        LineaFacturaJpaEntity savedEntity = lineaFacturaJpaRepository.save(jpaEntity);
        return savedEntity.toDomain();
    }

    public void deleteLinea(LineaFacturaId lineaFacturaId) {
        lineaFacturaJpaRepository.deleteById(lineaFacturaId.value());
    }

    @Override
    public Tarifa save(Tarifa tarifa) {
        TarifaJpaEntity jpaEntity = TarifaJpaEntity.fromDomain(tarifa);
        TarifaJpaEntity savedEntity = tarifaJpaRepository.save(jpaEntity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Tarifa> findById(TarifaId id) {
        return tarifaJpaRepository.findById(id.value())
                .map(TarifaJpaEntity::toDomain);
    }

    @Override
    public void delete(Tarifa tarifa) {
        tarifaJpaRepository.deleteById(tarifa.getId().value());
    }

    @Override
    public List<Tarifa> findAll() {
        return tarifaJpaRepository.findAll().stream()
                .map(TarifaJpaEntity::toDomain)
                .collect(Collectors.toList());
    }

    public List<Tarifa> findTarifasActivas() {
        return tarifaJpaRepository.findByActivoTrue().stream()
                .map(TarifaJpaEntity::toDomain)
                .collect(Collectors.toList());
    }
}