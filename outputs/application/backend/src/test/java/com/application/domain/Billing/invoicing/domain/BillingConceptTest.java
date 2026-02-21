package com.application.domain.Billing.invoicing.domain;

import com.application.domain.Billing.valueobject.BillingConceptId;
import com.application.domain.shared.valueobject.Money;
import com.application.domain.exception.DomainException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BillingConceptTest {

    @Test
    void createBillingConcept_WithValidData_ShouldSucceed() {
        BillingConceptId id = new BillingConceptId();
        String name = "Monthly Tuition";
        String description = "Monthly tuition fee";
        Money defaultAmount = new Money(new BigDecimal("500.00"), "USD");
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;
        Integer applicableFromGrade = 1;
        Integer applicableToGrade = 12;

        BillingConcept billingConcept = BillingConcept.create(
                id,
                name,
                description,
                defaultAmount,
                periodicity,
                applicableFromGrade,
                applicableToGrade
        );

        assertNotNull(billingConcept);
        assertEquals(id, billingConcept.getId());
        assertEquals(name, billingConcept.getName());
        assertEquals(description, billingConcept.getDescription());
        assertEquals(defaultAmount, billingConcept.getDefaultAmount());
        assertEquals(periodicity, billingConcept.getPeriodicity());
        assertEquals(applicableFromGrade, billingConcept.getApplicableFromGrade());
        assertEquals(applicableToGrade, billingConcept.getApplicableToGrade());
        assertTrue(billingConcept.isActive());
    }

    @Test
    void createBillingConcept_WithNullName_ShouldThrowException() {
        BillingConceptId id = new BillingConceptId();
        Money defaultAmount = new Money(new BigDecimal("500.00"), "USD");
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;

        DomainException exception = assertThrows(DomainException.class, () ->
                BillingConcept.create(
                        id,
                        null,
                        "Description",
                        defaultAmount,
                        periodicity,
                        1,
                        12
                )
        );
        assertTrue(exception.getMessage().contains("name"));
    }

    @Test
    void createBillingConcept_WithEmptyName_ShouldThrowException() {
        BillingConceptId id = new BillingConceptId();
        Money defaultAmount = new Money(new BigDecimal("500.00"), "USD");
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;

        DomainException exception = assertThrows(DomainException.class, () ->
                BillingConcept.create(
                        id,
                        "",
                        "Description",
                        defaultAmount,
                        periodicity,
                        1,
                        12
                )
        );
        assertTrue(exception.getMessage().contains("name"));
    }

    @Test
    void createBillingConcept_WithNullDefaultAmount_ShouldThrowException() {
        BillingConceptId id = new BillingConceptId();
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;

        DomainException exception = assertThrows(DomainException.class, () ->
                BillingConcept.create(
                        id,
                        "Name",
                        "Description",
                        null,
                        periodicity,
                        1,
                        12
                )
        );
        assertTrue(exception.getMessage().contains("default amount"));
    }

    @Test
    void createBillingConcept_WithNegativeDefaultAmount_ShouldThrowException() {
        BillingConceptId id = new BillingConceptId();
        Money negativeAmount = new Money(new BigDecimal("-10.00"), "USD");
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;

        DomainException exception = assertThrows(DomainException.class, () ->
                BillingConcept.create(
                        id,
                        "Name",
                        "Description",
                        negativeAmount,
                        periodicity,
                        1,
                        12
                )
        );
        assertTrue(exception.getMessage().contains("default amount"));
    }

    @Test
    void createBillingConcept_WithNullPeriodicity_ShouldThrowException() {
        BillingConceptId id = new BillingConceptId();
        Money defaultAmount = new Money(new BigDecimal("500.00"), "USD");

        DomainException exception = assertThrows(DomainException.class, () ->
                BillingConcept.create(
                        id,
                        "Name",
                        "Description",
                        defaultAmount,
                        null,
                        1,
                        12
                )
        );
        assertTrue(exception.getMessage().contains("periodicity"));
    }

    @Test
    void createBillingConcept_WithInvalidGradeRange_ShouldThrowException() {
        BillingConceptId id = new BillingConceptId();
        Money defaultAmount = new Money(new BigDecimal("500.00"), "USD");
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;

        DomainException exception = assertThrows(DomainException.class, () ->
                BillingConcept.create(
                        id,
                        "Name",
                        "Description",
                        defaultAmount,
                        periodicity,
                        12,
                        1
                )
        );
        assertTrue(exception.getMessage().contains("grade"));
    }

    @Test
    void createBillingConcept_WithNullApplicableFromGrade_ShouldSucceed() {
        BillingConceptId id = new BillingConceptId();
        Money defaultAmount = new Money(new BigDecimal("500.00"), "USD");
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;

        BillingConcept billingConcept = BillingConcept.create(
                id,
                "Name",
                "Description",
                defaultAmount,
                periodicity,
                null,
                12
        );

        assertNull(billingConcept.getApplicableFromGrade());
        assertEquals(12, billingConcept.getApplicableToGrade());
    }

    @Test
    void createBillingConcept_WithNullApplicableToGrade_ShouldSucceed() {
        BillingConceptId id = new BillingConceptId();
        Money defaultAmount = new Money(new BigDecimal("500.00"), "USD");
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;

        BillingConcept billingConcept = BillingConcept.create(
                id,
                "Name",
                "Description",
                defaultAmount,
                periodicity,
                1,
                null
        );

        assertEquals(1, billingConcept.getApplicableFromGrade());
        assertNull(billingConcept.getApplicableToGrade());
    }

    @Test
    void deactivate_ShouldSetActiveToFalse() {
        BillingConcept billingConcept = createValidBillingConcept();
        assertTrue(billingConcept.isActive());

        billingConcept.deactivate();

        assertFalse(billingConcept.isActive());
    }

    @Test
    void activate_ShouldSetActiveToTrue() {
        BillingConcept billingConcept = createValidBillingConcept();
        billingConcept.deactivate();
        assertFalse(billingConcept.isActive());

        billingConcept.activate();

        assertTrue(billingConcept.isActive());
    }

    @Test
    void updateDetails_WithValidData_ShouldUpdate() {
        BillingConcept billingConcept = createValidBillingConcept();
        String newName = "Updated Tuition";
        String newDescription = "Updated description";
        Money newAmount = new Money(new BigDecimal("600.00"), "USD");
        BillingConcept.Periodicity newPeriodicity = BillingConcept.Periodicity.ANNUAL;
        Integer newFromGrade = 5;
        Integer newToGrade = 10;

        billingConcept.updateDetails(newName, newDescription, newAmount, newPeriodicity, newFromGrade, newToGrade);

        assertEquals(newName, billingConcept.getName());
        assertEquals(newDescription, billingConcept.getDescription());
        assertEquals(newAmount, billingConcept.getDefaultAmount());
        assertEquals(newPeriodicity, billingConcept.getPeriodicity());
        assertEquals(newFromGrade, billingConcept.getApplicableFromGrade());
        assertEquals(newToGrade, billingConcept.getApplicableToGrade());
    }

    @Test
    void updateDetails_WithInvalidData_ShouldThrowException() {
        BillingConcept billingConcept = createValidBillingConcept();
        Money negativeAmount = new Money(new BigDecimal("-10.00"), "USD");

        DomainException exception = assertThrows(DomainException.class, () ->
                billingConcept.updateDetails(
                        "Name",
                        "Description",
                        negativeAmount,
                        BillingConcept.Periodicity.MONTHLY,
                        1,
                        12
                )
        );
        assertTrue(exception.getMessage().contains("default amount"));
    }

    @Test
    void isApplicableForGrade_WithGradeInRange_ShouldReturnTrue() {
        BillingConcept billingConcept = BillingConcept.create(
                new BillingConceptId(),
                "Test",
                "Test",
                new Money(new BigDecimal("100.00"), "USD"),
                BillingConcept.Periodicity.MONTHLY,
                1,
                6
        );

        assertTrue(billingConcept.isApplicableForGrade(1));
        assertTrue(billingConcept.isApplicableForGrade(3));
        assertTrue(billingConcept.isApplicableForGrade(6));
    }

    @Test
    void isApplicableForGrade_WithGradeOutOfRange_ShouldReturnFalse() {
        BillingConcept billingConcept = BillingConcept.create(
                new BillingConceptId(),
                "Test",
                "Test",
                new Money(new BigDecimal("100.00"), "USD"),
                BillingConcept.Periodicity.MONTHLY,
                1,
                6
        );

        assertFalse(billingConcept.isApplicableForGrade(0));
        assertFalse(billingConcept.isApplicableForGrade(7));
    }

    @Test
    void isApplicableForGrade_WithNullFromGrade_ShouldReturnTrueForAllUpToToGrade() {
        BillingConcept billingConcept = BillingConcept.create(
                new BillingConceptId(),
                "Test",
                "Test",
                new Money(new BigDecimal("100.00"), "USD"),
                BillingConcept.Periodicity.MONTHLY,
                null,
                6
        );

        assertTrue(billingConcept.isApplicableForGrade(1));
        assertTrue(billingConcept.isApplicableForGrade(6));
        assertFalse(billingConcept.isApplicableForGrade(7));
    }

    @Test
    void isApplicableForGrade_WithNullToGrade_ShouldReturnTrueForAllFromFromGrade() {
        BillingConcept billingConcept = BillingConcept.create(
                new BillingConceptId(),
                "Test",
                "Test",
                new Money(new BigDecimal("100.00"), "USD"),
                BillingConcept.Periodicity.MONTHLY,
                5,
                null
        );

        assertFalse(billingConcept.isApplicableForGrade(4));
        assertTrue(billingConcept.isApplicableForGrade(5));
        assertTrue(billingConcept.isApplicableForGrade(10));
    }

    @Test
    void isApplicableForGrade_WithBothGradesNull_ShouldReturnTrueForAnyGrade() {
        BillingConcept billingConcept = BillingConcept.create(
                new BillingConceptId(),
                "Test",
                "Test",
                new Money(new BigDecimal("100.00"), "USD"),
                BillingConcept.Periodicity.MONTHLY,
                null,
                null
        );

        assertTrue(billingConcept.isApplicableForGrade(1));
        assertTrue(billingConcept.isApplicableForGrade(12));
        assertTrue(billingConcept.isApplicableForGrade(100));
    }

    private BillingConcept createValidBillingConcept() {
        BillingConceptId id = new BillingConceptId();
        Money defaultAmount = new Money(new BigDecimal("500.00"), "USD");
        BillingConcept.Periodicity periodicity = BillingConcept.Periodicity.MONTHLY;
        return BillingConcept.create(
                id,
                "Monthly Tuition",
                "Monthly tuition fee",
                defaultAmount,
                periodicity,
                1,
                12
        );
    }
}