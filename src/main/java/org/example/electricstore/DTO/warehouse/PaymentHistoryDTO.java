package org.example.electricstore.DTO.warehouse;

import lombok.Getter;
import lombok.Setter;
import org.example.electricstore.model.PaymentHistory;

import java.time.LocalDateTime;

@Getter
@Setter
public class PaymentHistoryDTO {
    private Integer id;
    private Integer invoiceId;
    private String method;
    private Double amount;
    private LocalDateTime paidAt;


    public static PaymentHistoryDTO fromEntity(PaymentHistory entity) {
        PaymentHistoryDTO dto = new PaymentHistoryDTO();
        dto.setId(entity.getId());
        dto.setInvoiceId(entity.getInvoiceId());
        dto.setMethod(entity.getMethod());
        dto.setAmount(entity.getAmount());
        dto.setPaidAt(entity.getPaidAt());
        return dto;
    }
}