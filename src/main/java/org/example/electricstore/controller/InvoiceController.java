package org.example.electricstore.controller;

import lombok.RequiredArgsConstructor;
import org.example.electricstore.model.Invoice;
import org.example.electricstore.repository.InvoiceRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Admin/ware-houses")
@RequiredArgsConstructor
public class InvoiceController {
    private final InvoiceRepository invoiceRepository;

    @GetMapping("/invoice_form_warehouses/{id}")
    public String showInvoiceForm(@PathVariable("id") Integer id, Model model) {
        Invoice invoice = invoiceRepository.findByIdWithProducts(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập"));

        double total = invoice.getProducts().stream()
                .mapToDouble(p -> p.getPrice() * p.getQuantity())
                .sum() - invoice.getDiscount() + invoice.getAdditionalFees();

        long totalQuantity = invoice.getProducts().stream()
                .mapToLong(p -> Math.round(p.getQuantity()))
                .sum();
        model.addAttribute("invoice", invoice);
        model.addAttribute("total", total);
        model.addAttribute("totalQuantity", totalQuantity);

        return "admin/warehouse/invoice_form_warehouses";
    }
}