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
    private double total;
    private String paymentStatus;

    // Thêm phương thức trợ giúp để lấy tên nhà cung cấp
    public String getSupplierName() {
        if (invoice != null && invoice.getSupplier() != null) {
            return invoice.getSupplier().getSupplierName();
        }
        return "N/A";
    }

    // Thêm phương thức trợ giúp để lấy mã nhà cung cấp
    public String getSupplierCode() {
        if (invoice != null && invoice.getSupplier() != null) {
            return invoice.getSupplier().getSupplierCode();
        }
        return "N/A";
    }
}