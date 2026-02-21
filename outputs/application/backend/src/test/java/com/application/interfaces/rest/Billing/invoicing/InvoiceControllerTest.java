package com.application;

import com.application.application.Billing.invoicing.application.generate.GenerateMonthlyInvoiceCommand;
import com.application.application.Billing.invoicing.application.generate.GenerateMonthlyInvoiceCommandHandler;
import com.application.interfaces.rest.Billing.invoicing.dto.GenerateInvoiceRequestDTO;
import com.application.interfaces.rest.Billing.invoicing.dto.MonthlyInvoiceResponseDTO;
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

import java.time.YearMonth;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InvoiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GenerateMonthlyInvoiceCommandHandler generateMonthlyInvoiceCommandHandler;

    @InjectMocks
    private InvoiceController invoiceController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void generateMonthlyInvoice_ShouldReturnCreatedInvoice() throws Exception {
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        String billingPeriod = "2024-05";
        UUID invoiceId = UUID.randomUUID();

        GenerateInvoiceRequestDTO requestDTO = new GenerateInvoiceRequestDTO();
        requestDTO.setStudentId(studentId);
        requestDTO.setSchoolId(schoolId);
        requestDTO.setBillingPeriod(billingPeriod);

        MonthlyInvoiceResponseDTO responseDTO = new MonthlyInvoiceResponseDTO();
        responseDTO.setId(invoiceId);
        responseDTO.setStudentId(studentId);
        responseDTO.setBillingPeriod(billingPeriod);
        responseDTO.setStatus("PENDING");
        responseDTO.setTotalAmount(500.00);

        when(generateMonthlyInvoiceCommandHandler.handle(any(GenerateMonthlyInvoiceCommand.class)))
                .thenReturn(responseDTO);

        mockMvc.perform(post("/api/invoices/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(invoiceId.toString()))
                .andExpect(jsonPath("$.studentId").value(studentId.toString()))
                .andExpect(jsonPath("$.billingPeriod").value(billingPeriod))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.totalAmount").value(500.00));
    }

    @Test
    void generateMonthlyInvoice_WithInvalidPeriodFormat_ShouldReturnBadRequest() throws Exception {
        GenerateInvoiceRequestDTO requestDTO = new GenerateInvoiceRequestDTO();
        requestDTO.setStudentId(UUID.randomUUID());
        requestDTO.setSchoolId(UUID.randomUUID());
        requestDTO.setBillingPeriod("2024/05");

        mockMvc.perform(post("/api/invoices/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateMonthlyInvoice_WithMissingStudentId_ShouldReturnBadRequest() throws Exception {
        GenerateInvoiceRequestDTO requestDTO = new GenerateInvoiceRequestDTO();
        requestDTO.setSchoolId(UUID.randomUUID());
        requestDTO.setBillingPeriod("2024-05");

        mockMvc.perform(post("/api/invoices/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateMonthlyInvoice_WithMissingSchoolId_ShouldReturnBadRequest() throws Exception {
        GenerateInvoiceRequestDTO requestDTO = new GenerateInvoiceRequestDTO();
        requestDTO.setStudentId(UUID.randomUUID());
        requestDTO.setBillingPeriod("2024-05");

        mockMvc.perform(post("/api/invoices/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void generateMonthlyInvoice_WhenHandlerThrowsDomainException_ShouldReturnBadRequest() throws Exception {
        UUID studentId = UUID.randomUUID();
        UUID schoolId = UUID.randomUUID();
        String billingPeriod = "2024-05";

        GenerateInvoiceRequestDTO requestDTO = new GenerateInvoiceRequestDTO();
        requestDTO.setStudentId(studentId);
        requestDTO.setSchoolId(schoolId);
        requestDTO.setBillingPeriod(billingPeriod);

        when(generateMonthlyInvoiceCommandHandler.handle(any(GenerateMonthlyInvoiceCommand.class)))
                .thenThrow(new com.application.domain.exception.DomainException("Invoice already exists for period"));

        mockMvc.perform(post("/api/invoices/generate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());
    }
}