package org.example.electricstore.controller.api;

import lombok.RequiredArgsConstructor;
import org.example.electricstore.service.common.PDFImportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/Admin/invoice")
@RequiredArgsConstructor
@CrossOrigin("*")
public class InvoicePDFController {

    private final PDFImportService pdfImportService;

    @GetMapping("/pdf/{id}")
    public ResponseEntity<byte[]> generateInvoicePDF(@PathVariable Long id) {
        try {
            byte[] pdfBytes = pdfImportService.createImportPDF(id);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "phieu-nhap-kho-" + id + ".pdf");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}