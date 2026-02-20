package com.application.infrastructure.config;

import com.application.application.mapper.AttendanceMapper;
import com.application.application.mapper.BillingMapper;
import com.application.application.mapper.StudentMapper;
import com.application.application.service.AttendanceServiceImpl;
import com.application.application.service.BillingServiceImpl;
import com.application.application.service.StudentServiceImpl;
import com.application.domain.ports.in.AttendanceService;
import com.application.domain.ports.in.BillingService;
import com.application.domain.ports.in.StudentService;
import com.application.domain.ports.out.AttendanceRepository;
import com.application.domain.ports.out.FeeStructureRepository;
import com.application.domain.ports.out.InvoiceRepository;
import com.application.domain.ports.out.SchoolRepository;
import com.application.domain.ports.out.StudentRepository;
import com.application.infrastructure.persistence.adapter.AttendanceRepositoryAdapter;
import com.application.infrastructure.persistence.adapter.InvoiceRepositoryAdapter;
import com.application.infrastructure.persistence.adapter.SchoolRepositoryAdapter;
import com.application.infrastructure.persistence.adapter.StudentRepositoryAdapter;
import com.application.infrastructure.persistence.jpa.AttendanceJpaRepository;
import com.application.infrastructure.persistence.jpa.InvoiceJpaRepository;
import com.application.infrastructure.persistence.jpa.SchoolJpaRepository;
import com.application.infrastructure.persistence.jpa.StudentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    private final StudentJpaRepository studentJpaRepository;
    private final AttendanceJpaRepository attendanceJpaRepository;
    private final InvoiceJpaRepository invoiceJpaRepository;
    private final SchoolJpaRepository schoolJpaRepository;

    @Bean
    public StudentRepository studentRepository() {
        return new StudentRepositoryAdapter(studentJpaRepository);
    }

    @Bean
    public AttendanceRepository attendanceRepository() {
        return new AttendanceRepositoryAdapter(attendanceJpaRepository);
    }

    @Bean
    public InvoiceRepository invoiceRepository() {
        return new InvoiceRepositoryAdapter(invoiceJpaRepository);
    }

    @Bean
    public SchoolRepository schoolRepository() {
        return new SchoolRepositoryAdapter(schoolJpaRepository);
    }

    @Bean
    public StudentService studentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        return new StudentServiceImpl(studentRepository, studentMapper);
    }

    @Bean
    public AttendanceService attendanceService(AttendanceRepository attendanceRepository, AttendanceMapper attendanceMapper) {
        return new AttendanceServiceImpl(attendanceRepository, attendanceMapper);
    }

    @Bean
    public BillingService billingService(InvoiceRepository invoiceRepository, FeeStructureRepository feeStructureRepository, BillingMapper billingMapper) {
        return new BillingServiceImpl(invoiceRepository, feeStructureRepository, billingMapper);
    }

    @Bean
    public StudentMapper studentMapper() {
        return new StudentMapper();
    }

    @Bean
    public AttendanceMapper attendanceMapper() {
        return new AttendanceMapper();
    }

    @Bean
    public BillingMapper billingMapper() {
        return new BillingMapper();
    }
}