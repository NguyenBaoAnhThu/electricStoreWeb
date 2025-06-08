package org.example.electricstore.DTO.warehouse;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.electricstore.model.Invoice;

@Getter
@Setter
@NoArgsConstructor
public class InvoiceDTO {
    private Invoice invoice;
    private String paymentStatus;

    public String getSupplierName() {
        if (invoice != null && invoice.getSupplier() != null) {
            return invoice.getSupplier().getSupplierName();
        }
        return "N/A";
    }

    // Phương thức trợ giúp để lấy totalPrice từ Invoice
    public double getTotalPrice() {
        return invoice != null ? invoice.getTotalPrice() : 0.0;
    }
}