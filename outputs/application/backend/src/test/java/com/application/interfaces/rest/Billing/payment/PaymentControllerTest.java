package com.application;

import com.application.application.Billing.payment.application.process.ProcessPaymentCommand;
import com.application.application.Billing.payment.application.process.ProcessPaymentCommandHandler;
import com.application.interfaces.rest.Billing.payment.PaymentController;
import com.application.interfaces.rest.Billing.payment.dto.PaymentResponseDTO;
import com.application.interfaces.rest.Billing.payment.dto.ProcessPaymentRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProcessPaymentCommandHandler processPaymentCommandHandler;

    @InjectMocks
    private PaymentController paymentController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void processPayment_ValidRequest_ReturnsCreatedPayment() throws Exception {
        UUID invoiceId = UUID.randomUUID();
        UUID paymentId = UUID.randomUUID();
        ProcessPaymentRequestDTO requestDTO = new ProcessPaymentRequestDTO();
        requestDTO.setMonthlyInvoiceId(invoiceId);
        requestDTO.setAmount(new BigDecimal("150.75"));
        requestDTO.setPaymentDate(LocalDate.of(2024, 1, 15));
        requestDTO.setPaymentMethod("BANK_TRANSFER");
        requestDTO.setTransactionReference("TX-123456");

        PaymentResponseDTO expectedResponse = PaymentResponseDTO.builder()
                .id(paymentId)
                .monthlyInvoiceId(invoiceId)
                .amount(new BigDecimal("150.75"))
                .paymentDate(LocalDate.of(2024, 1, 15))
                .paymentMethod("BANK_TRANSFER")
                .transactionReference("TX-123456")
                .confirmed(true)
                .build();

        when(processPaymentCommandHandler.handle(any(ProcessPaymentCommand.class))).thenReturn(expectedResponse);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(paymentId.toString()))
                .andExpect(jsonPath("$.monthlyInvoiceId").value(invoiceId.toString()))
                .andExpect(jsonPath("$.amount").value(150.75))
                .andExpect(jsonPath("$.paymentMethod").value("BANK_TRANSFER"))
                .andExpect(jsonPath("$.confirmed").value(true));
    }

    @Test
    void processPayment_InvalidRequest_ReturnsBadRequest() throws Exception {
        ProcessPaymentRequestDTO requestDTO = new ProcessPaymentRequestDTO();
        requestDTO.setMonthlyInvoiceId(null);
        requestDTO.setAmount(BigDecimal.ZERO);
        requestDTO.setPaymentMethod("INVALID_METHOD");

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void processPayment_HandlerThrowsDomainException_ReturnsBadRequest() throws Exception {
        UUID invoiceId = UUID.randomUUID();
        ProcessPaymentRequestDTO requestDTO = new ProcessPaymentRequestDTO();
        requestDTO.setMonthlyInvoiceId(invoiceId);
        requestDTO.setAmount(new BigDecimal("100.00"));
        requestDTO.setPaymentDate(LocalDate.now());
        requestDTO.setPaymentMethod("CASH");

        when(processPaymentCommandHandler.handle(any(ProcessPaymentCommand.class)))
                .thenThrow(new IllegalArgumentException("Invoice not found"));

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}