package com.application.infrastructure.adapter;

import com.application.domain.model.ConsultingRoom;
import com.application.domain.port.ConsultingRoomRepositoryPort;
import com.application.domain.valueobject.ClinicId;
import com.application.domain.valueobject.ConsultingRoomId;
import com.application.infrastructure.entity.ConsultingRoomEntity;
import com.application.infrastructure.repository.ConsultingRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ConsultingRoomJpaAdapter implements ConsultingRoomRepositoryPort {

    private final ConsultingRoomJpaRepository consultingRoomJpaRepository;

    @Override
    public ConsultingRoom save(ConsultingRoom consultingRoom) {
        ConsultingRoomEntity entity = ConsultingRoomEntity.fromDomain(consultingRoom);
        ConsultingRoomEntity savedEntity = consultingRoomJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<ConsultingRoom> findById(ConsultingRoomId id) {
        return consultingRoomJpaRepository.findById(id.getValue())
                .map(ConsultingRoomEntity::toDomain);
    }

    @Override
    public List<ConsultingRoom> findAll() {
        return consultingRoomJpaRepository.findAll()
                .stream()
                .map(ConsultingRoomEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(ConsultingRoomId id) {
        consultingRoomJpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(ConsultingRoomId id) {
        return consultingRoomJpaRepository.existsById(id.getValue());
    }

    @Override
    public List<ConsultingRoom> findByClinicIdAndAvailable(ClinicId clinicId, boolean available) {
        return consultingRoomJpaRepository.findByClinicIdAndDisponible(clinicId.getValue(), available)
                .stream()
                .map(ConsultingRoomEntity::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ConsultingRoom> findByClinicId(ClinicId clinicId) {
        return consultingRoomJpaRepository.findByClinicId(clinicId.getValue())
                .stream()
                .map(ConsultingRoomEntity::toDomain)
                .collect(Collectors.toList());
    }
}