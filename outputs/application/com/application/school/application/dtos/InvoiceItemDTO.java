package com.application.school.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDTO {
    private String itemId;
    private String concept;
    private String description;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
}