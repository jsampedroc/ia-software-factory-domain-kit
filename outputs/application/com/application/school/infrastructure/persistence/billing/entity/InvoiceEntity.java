package com.application.school.infrastructure.persistence.billing.entity;

import com.application.school.domain.shared.enumeration.InvoiceStatus;
import com.application.school.infrastructure.persistence.student.entity.StudentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceEntity {
    @Id
    private UUID invoiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private StudentEntity student;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private YearMonth monthYear;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InvoiceStatus status;

    @Column
    private LocalDate paymentDate;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoiceItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PaymentEntity> payments = new ArrayList<>();
}