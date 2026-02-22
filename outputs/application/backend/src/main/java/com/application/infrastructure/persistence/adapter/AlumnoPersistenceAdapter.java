package com.application.infrastructure.persistence.adapter;

import com.application.domain.model.alumno.Alumno;
import com.application.domain.model.alumno.AlumnoId;
import com.application.domain.model.alumno.Tutor;
import com.application.domain.model.alumno.TutorId;
import com.application.domain.shared.Repository;
import com.application.infrastructure.persistence.jpa.alumno.AlumnoJpaEntity;
import com.application.infrastructure.persistence.jpa.alumno.AlumnoJpaRepository;
import com.application.infrastructure.persistence.jpa.alumno.TutorJpaEntity;
import com.application.infrastructure.persistence.jpa.alumno.TutorJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AlumnoPersistenceAdapter implements Repository<Alumno, AlumnoId> {

    private final AlumnoJpaRepository alumnoJpaRepository;
    private final TutorJpaRepository tutorJpaRepository;
    private final AlumnoMapper alumnoMapper;
    private final TutorMapper tutorMapper;

    @Override
    public Alumno save(Alumno alumno) {
        AlumnoJpaEntity alumnoJpaEntity = alumnoMapper.toJpaEntity(alumno);
        AlumnoJpaEntity savedAlumnoEntity = alumnoJpaRepository.save(alumnoJpaEntity);

        List<TutorJpaEntity> tutorEntities = alumno.getTutores().stream()
                .map(tutor -> {
                    TutorJpaEntity tutorEntity = tutorMapper.toJpaEntity(tutor);
                    tutorEntity.setAlumno(savedAlumnoEntity);
                    return tutorEntity;
                })
                .collect(Collectors.toList());
        tutorJpaRepository.saveAll(tutorEntities);

        return alumnoMapper.toDomainEntity(savedAlumnoEntity);
    }

    @Override
    public Optional<Alumno> findById(AlumnoId id) {
        return alumnoJpaRepository.findById(id.value())
                .map(alumnoMapper::toDomainEntity);
    }

    @Override
    public void deleteById(AlumnoId id) {
        alumnoJpaRepository.findById(id.value()).ifPresent(alumnoEntity -> {
            alumnoEntity.setActivo(false);
            alumnoJpaRepository.save(alumnoEntity);
        });
    }

    @Override
    public boolean existsById(AlumnoId id) {
        return alumnoJpaRepository.existsById(id.value());
    }

    public Optional<Alumno> findByNumeroMatricula(String numeroMatricula) {
        return alumnoJpaRepository.findByNumeroMatricula(numeroMatricula)
                .map(alumnoMapper::toDomainEntity);
    }

    public List<Alumno> findAllActivos() {
        return alumnoJpaRepository.findByActivoTrue().stream()
                .map(alumnoMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    public Tutor saveTutor(Tutor tutor, AlumnoId alumnoId) {
        AlumnoJpaEntity alumnoEntity = alumnoJpaRepository.findById(alumnoId.value())
                .orElseThrow(() -> new RuntimeException("Alumno no encontrado con ID: " + alumnoId.value()));
        TutorJpaEntity tutorEntity = tutorMapper.toJpaEntity(tutor);
        tutorEntity.setAlumno(alumnoEntity);
        TutorJpaEntity savedTutorEntity = tutorJpaRepository.save(tutorEntity);
        return tutorMapper.toDomainEntity(savedTutorEntity);
    }

    public Optional<Tutor> findTutorById(TutorId tutorId) {
        return tutorJpaRepository.findById(tutorId.value())
                .map(tutorMapper::toDomainEntity);
    }

    public void deleteTutorById(TutorId tutorId) {
        tutorJpaRepository.deleteById(tutorId.value());
    }
}