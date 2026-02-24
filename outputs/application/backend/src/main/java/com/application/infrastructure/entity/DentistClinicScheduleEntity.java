package com.application.infrastructure.entity;

import com.application.domain.valueobject.DentistId;
import com.application.domain.valueobject.ClinicId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Table(name = "dentist_clinic_schedule")
@Getter
@NoArgsConstructor(access = PROTECTED)
public class DentistClinicScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "dentist_id", nullable = false))
    private DentistId dentistId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "clinic_id", nullable = false))
    private ClinicId clinicId;

    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "is_active", nullable = false)
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "dentist_id", referencedColumnName = "dentist_id", insertable = false, updatable = false)
    })
    private DentistEntity dentist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "clinic_id", referencedColumnName = "clinic_id", insertable = false, updatable = false)
    })
    private ClinicEntity clinic;
}