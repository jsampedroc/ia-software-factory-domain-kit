package com.application.domain.model;

import com.application.domain.valueobject.InvoiceItemId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("InvoiceItem Value Object Unit Tests")
class InvoiceItemTest {

    private InvoiceItemId createTestId() {
        return new InvoiceItemId(UUID.randomUUID());
    }

    @Nested
    @DisplayName("Successful Creation")
    class SuccessfulCreationTests {

        @Test
        @DisplayName("Should create InvoiceItem with valid parameters")
        void shouldCreateInvoiceItemWithValidParameters() {
            // Given
            InvoiceItemId id = createTestId();
            String treatmentCode = "FILL01";
            String description = "Composite Filling";
            int quantity = 1;
            BigDecimal unitPrice = new BigDecimal("150.00");
            BigDecimal amount = new BigDecimal("150.00");

            // When
            InvoiceItem invoiceItem = new InvoiceItem(id, treatmentCode, description, quantity, unitPrice, amount);

            // Then
            assertThat(invoiceItem).isNotNull();
            assertThat(invoiceItem.itemId()).isEqualTo(id);
            assertThat(invoiceItem.treatmentCode()).isEqualTo(treatmentCode);
            assertThat(invoiceItem.description()).isEqualTo(description);
            assertThat(invoiceItem.quantity()).isEqualTo(quantity);
            assertThat(invoiceItem.unitPrice()).isEqualTo(unitPrice);
            assertThat(invoiceItem.amount()).isEqualTo(amount);
        }

        @ParameterizedTest
        @CsvSource({
                "2, 75.50, 151.00",
                "3, 100.00, 300.00",
                "5, 0.01, 0.05",
                "1, 9999.99, 9999.99"
        })
        @DisplayName("Should create InvoiceItem with various valid quantities and prices")
        void shouldCreateInvoiceItemWithVariousValidQuantitiesAndPrices(int quantity, String unitPriceStr, String amountStr) {
            // Given
            InvoiceItemId id = createTestId();
            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            BigDecimal amount = new BigDecimal(amountStr);

            // When & Then
            assertThat(new InvoiceItem(id, "CLEAN01", "Dental Cleaning", quantity, unitPrice, amount))
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("Null Parameter Validation")
    class NullParameterValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when itemId is null")
        void shouldThrowExceptionWhenItemIdIsNull() {
            assertThatThrownBy(() -> new InvoiceItem(null, "CODE01", "Description", 1, BigDecimal.TEN, BigDecimal.TEN))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Item ID cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when treatmentCode is null")
        void shouldThrowExceptionWhenTreatmentCodeIsNull() {
            InvoiceItemId id = createTestId();
            assertThatThrownBy(() -> new InvoiceItem(id, null, "Description", 1, BigDecimal.TEN, BigDecimal.TEN))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Treatment code cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when description is null")
        void shouldThrowExceptionWhenDescriptionIsNull() {
            InvoiceItemId id = createTestId();
            assertThatThrownBy(() -> new InvoiceItem(id, "CODE01", null, 1, BigDecimal.TEN, BigDecimal.TEN))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Description cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when unitPrice is null")
        void shouldThrowExceptionWhenUnitPriceIsNull() {
            InvoiceItemId id = createTestId();
            assertThatThrownBy(() -> new InvoiceItem(id, "CODE01", "Description", 1, null, BigDecimal.TEN))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Unit price cannot be null");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is null")
        void shouldThrowExceptionWhenAmountIsNull() {
            InvoiceItemId id = createTestId();
            assertThatThrownBy(() -> new InvoiceItem(id, "CODE01", "Description", 1, BigDecimal.TEN, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Amount cannot be null");
        }
    }

    @Nested
    @DisplayName("Blank String Validation")
    class BlankStringValidationTests {

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Should throw IllegalArgumentException when treatmentCode is blank")
        void shouldThrowExceptionWhenTreatmentCodeIsBlank(String blankCode) {
            InvoiceItemId id = createTestId();
            assertThatThrownBy(() -> new InvoiceItem(id, blankCode, "Description", 1, BigDecimal.TEN, BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Treatment code cannot be blank");
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "\t", "\n"})
        @DisplayName("Should throw IllegalArgumentException when description is blank")
        void shouldThrowExceptionWhenDescriptionIsBlank(String blankDescription) {
            InvoiceItemId id = createTestId();
            assertThatThrownBy(() -> new InvoiceItem(id, "CODE01", blankDescription, 1, BigDecimal.TEN, BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Description cannot be blank");
        }
    }

    @Nested
    @DisplayName("Numeric Validation")
    class NumericValidationTests {

        @ParameterizedTest
        @ValueSource(ints = {0, -1, -10, Integer.MIN_VALUE})
        @DisplayName("Should throw IllegalArgumentException when quantity is not positive")
        void shouldThrowExceptionWhenQuantityIsNotPositive(int invalidQuantity) {
            InvoiceItemId id = createTestId();
            assertThatThrownBy(() -> new InvoiceItem(id, "CODE01", "Description", invalidQuantity, BigDecimal.TEN, BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Quantity must be positive");
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when unitPrice is negative")
        void shouldThrowExceptionWhenUnitPriceIsNegative() {
            InvoiceItemId id = createTestId();
            BigDecimal negativePrice = new BigDecimal("-50.00");
            assertThatThrownBy(() -> new InvoiceItem(id, "CODE01", "Description", 1, negativePrice, BigDecimal.TEN))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Unit price cannot be negative");
        }

        @Test
        @DisplayName("Should accept zero unitPrice (free item)")
        void shouldAcceptZeroUnitPrice() {
            InvoiceItemId id = createTestId();
            BigDecimal zeroPrice = BigDecimal.ZERO;
            BigDecimal zeroAmount = BigDecimal.ZERO;

            InvoiceItem invoiceItem = new InvoiceItem(id, "FREE01", "Complimentary Service", 1, zeroPrice, zeroAmount);

            assertThat(invoiceItem.unitPrice()).isEqualTo(zeroPrice);
            assertThat(invoiceItem.amount()).isEqualTo(zeroAmount);
        }

        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is negative")
        void shouldThrowExceptionWhenAmountIsNegative() {
            InvoiceItemId id = createTestId();
            BigDecimal negativeAmount = new BigDecimal("-100.00");
            assertThatThrownBy(() -> new InvoiceItem(id, "CODE01", "Description", 1, BigDecimal.TEN, negativeAmount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount cannot be negative");
        }

        @Test
        @DisplayName("Should accept zero amount (free item)")
        void shouldAcceptZeroAmount() {
            InvoiceItemId id = createTestId();
            BigDecimal zeroAmount = BigDecimal.ZERO;

            InvoiceItem invoiceItem = new InvoiceItem(id, "FREE01", "Complimentary Service", 1, BigDecimal.ZERO, zeroAmount);

            assertThat(invoiceItem.amount()).isEqualTo(zeroAmount);
        }
    }

    @Nested
    @DisplayName("Business Invariant Validation")
    class BusinessInvariantValidationTests {

        @Test
        @DisplayName("Should throw IllegalArgumentException when amount does not equal quantity * unitPrice")
        void shouldThrowExceptionWhenAmountDoesNotMatchCalculation() {
            InvoiceItemId id = createTestId();
            BigDecimal unitPrice = new BigDecimal("150.00");
            BigDecimal incorrectAmount = new BigDecimal("200.00"); // Should be 150.00

            assertThatThrownBy(() -> new InvoiceItem(id, "FILL01", "Filling", 1, unitPrice, incorrectAmount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount must equal quantity * unitPrice");
        }

        @ParameterizedTest
        @CsvSource({
                "2, 75.00, 160.00",
                "3, 100.00, 299.99",
                "5, 10.00, 49.99"
        })
        @DisplayName("Should throw IllegalArgumentException for various incorrect amount calculations")
        void shouldThrowExceptionForVariousIncorrectAmountCalculations(int quantity, String unitPriceStr, String amountStr) {
            InvoiceItemId id = createTestId();
            BigDecimal unitPrice = new BigDecimal(unitPriceStr);
            BigDecimal amount = new BigDecimal(amountStr);

            assertThatThrownBy(() -> new InvoiceItem(id, "CODE01", "Description", quantity, unitPrice, amount))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Amount must equal quantity * unitPrice");
        }

        @Test
        @DisplayName("Should accept amount with different scale but same value")
        void shouldAcceptAmountWithDifferentScaleButSameValue() {
            InvoiceItemId id = createTestId();
            BigDecimal unitPrice = new BigDecimal("150.0");
            BigDecimal amount = new BigDecimal("150.00");

            InvoiceItem invoiceItem = new InvoiceItem(id, "FILL01", "Filling", 1, unitPrice, amount);

            assertThat(invoiceItem.amount().compareTo(amount)).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Value Object Semantics")
    class ValueObjectSemanticsTests {

        @Test
        @DisplayName("Should implement ValueObject interface")
        void shouldImplementValueObjectInterface() {
            InvoiceItemId id = createTestId();
            InvoiceItem invoiceItem = new InvoiceItem(id, "CODE01", "Description", 1, BigDecimal.TEN, BigDecimal.TEN);

            assertThat(invoiceItem).isInstanceOf(com.application.domain.shared.ValueObject.class);
        }

        @Test
        @DisplayName("Equals and hashCode should be based on all fields")
        void equalsAndHashCodeShouldBeBasedOnAllFields() {
            InvoiceItemId id1 = new InvoiceItemId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
            InvoiceItemId id2 = new InvoiceItemId(UUID.fromString("123e4567-e89b-12d3-a456-426614174001"));

            InvoiceItem item1 = new InvoiceItem(id1, "CODE01", "Description", 1, new BigDecimal("100.00"), new BigDecimal("100.00"));
            InvoiceItem item2 = new InvoiceItem(id1, "CODE01", "Description", 1, new BigDecimal("100.00"), new BigDecimal("100.00"));
            InvoiceItem item3 = new InvoiceItem(id2, "CODE01", "Description", 1, new BigDecimal("100.00"), new BigDecimal("100.00"));
            InvoiceItem item4 = new InvoiceItem(id1, "CODE02", "Description", 1, new BigDecimal("100.00"), new BigDecimal("100.00"));

            // Test equality
            assertThat(item1).isEqualTo(item2);
            assertThat(item1).isNotEqualTo(item3);
            assertThat(item1).isNotEqualTo(item4);

            // Test hashCode consistency
            assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
            assertThat(item1.hashCode()).isNotEqualTo(item3.hashCode());
        }

        @Test
        @DisplayName("Should have informative toString representation")
        void shouldHaveInformativeToStringRepresentation() {
            InvoiceItemId id = new InvoiceItemId(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
            InvoiceItem invoiceItem = new InvoiceItem(id, "FILL01", "Composite Filling", 1, new BigDecimal("150.00"), new BigDecimal("150.00"));

            String toString = invoiceItem.toString();

            assertThat(toString).contains("InvoiceItem[");
            assertThat(toString).contains("itemId=" + id);
            assertThat(toString).contains("treatmentCode=FILL01");
            assertThat(toString).contains("description=Composite Filling");
            assertThat(toString).contains("quantity=1");
            assertThat(toString).contains("unitPrice=150.00");
            assertThat(toString).contains("amount=150.00");
        }
    }
}