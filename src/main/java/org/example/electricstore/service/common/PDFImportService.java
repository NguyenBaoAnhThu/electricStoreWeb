package org.example.electricstore.service.common;

import com.itextpdf.io.font.PdfEncodings;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.example.electricstore.DTO.warehouse.InvoiceItemDTO;
import org.example.electricstore.DTO.warehouse.WarehouseImportDTO;
import org.example.electricstore.model.Invoice;
import org.example.electricstore.model.InvoiceItem;
import org.example.electricstore.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PDFImportService {

    // Định nghĩa màu sắc chủ đạo
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(0, 102, 204);
    private static final DeviceRgb SECONDARY_COLOR = new DeviceRgb(33, 37, 41);
    private static final DeviceRgb TABLE_HEADER_COLOR = new DeviceRgb(0, 0, 0);
    private static final float BORDER_WIDTH = 0.3f;

    @Autowired
    private InvoiceRepository invoiceRepository;

    public byte[] createImportPDF(Integer invoiceId) throws IOException {
        // Lấy thông tin hóa đơn từ database
        Invoice invoice = invoiceRepository.findByIdWithProducts(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập kho"));

        WarehouseImportDTO importDTO = convertToDTO(invoice);

        // Tạo PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(30, 30, 30, 30);

            // Tải font chữ hỗ trợ tiếng Việt
            InputStream fontStream = new ClassPathResource("fonts/vuArial.ttf").getInputStream();
            PdfFont vietnameseFont = PdfFontFactory.createFont(fontStream.readAllBytes(), PdfEncodings.IDENTITY_H);

            // Thử tải font in đậm, nếu không tồn tại thì dùng font thường
            PdfFont vietnameseBoldFont = vietnameseFont;
            try {
                InputStream boldFontStream = new ClassPathResource("fonts/vuArialBold.ttf").getInputStream();
                vietnameseBoldFont = PdfFontFactory.createFont(boldFontStream.readAllBytes(), PdfEncodings.IDENTITY_H);
            } catch (IOException e) {
                System.err.println("Font vuArialBold.ttf not found, falling back to vuArial.ttf");
            }

            // Thêm header công ty
            addCompanyHeader(document, vietnameseFont, vietnameseBoldFont);

            // Tiêu đề phiếu nhập kho
            document.add(new Paragraph("PHIẾU NHẬP KHO")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(16)
                    .setFontColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(15)
                    .setMarginBottom(10));

            // Thêm thông tin phiếu
            addInvoiceInfo(document, importDTO, vietnameseFont, vietnameseBoldFont);

            // Vẽ đường phân cách
            addDivider(document);

            // Thông tin nhà cung cấp
            document.add(new Paragraph("THÔNG TIN NHÀ CUNG CẤP")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(12)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(15)
                    .setMarginBottom(10));

            addSupplierInfo(document, invoice, importDTO, vietnameseFont);

            // Chi tiết sản phẩm
            document.add(new Paragraph("CHI TIẾT SẢN PHẨM NHẬP")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(12)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(15)
                    .setMarginBottom(10));

            addProductTable(document, importDTO, vietnameseFont, vietnameseBoldFont);

            // Bảng tổng hợp
            addSummaryTable(document, importDTO, vietnameseFont, vietnameseBoldFont);

            // Phần chữ ký
            addSignatureSection(document, vietnameseFont, vietnameseBoldFont);

            // Chân trang
            document.add(new Paragraph("Electric Store - Hệ thống quản lý kho hàng")
                    .setFont(vietnameseFont)
                    .setFontSize(9)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20)
                    .setFontColor(new DeviceRgb(100, 100, 100)));

            document.close();
            return outputStream.toByteArray();
        }
    }

    private void addCompanyHeader(Document document, PdfFont font, PdfFont boldFont) throws IOException {
        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        // Logo
        try {
            InputStream logoStream = new ClassPathResource("static/images/logo/logodark.png").getInputStream();
            Image logo = new Image(ImageDataFactory.create(logoStream.readAllBytes()));
            logo.setWidth(120);
            Cell logoCell = new Cell().add(logo)
                    .setBorder(Border.NO_BORDER)
                    .setVerticalAlignment(VerticalAlignment.MIDDLE);
            headerTable.addCell(logoCell);
        } catch (Exception e) {
            Cell logoCell = new Cell().add(new Paragraph("ELECTRIC STORE")
                            .setFont(boldFont)
                            .setFontSize(18)
                            .setFontColor(PRIMARY_COLOR))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.LEFT);
            headerTable.addCell(logoCell);
        }

        // Thông tin công ty
        Cell companyInfoCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE);
        companyInfoCell.add(new Paragraph("CÔNG TY TNHH ELECTRIC STORE")
                .setFont(boldFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT));
        companyInfoCell.add(new Paragraph("Địa chỉ: 79 Ngũ Hành Sơn, Ngũ Hành Sơn, Đà Nẵng")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT));
        companyInfoCell.add(new Paragraph("Mã số thuế: 0123456789")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT));
        companyInfoCell.add(new Paragraph("Điện thoại: (028) 1234 5678")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.RIGHT));
        headerTable.addCell(companyInfoCell);

        document.add(headerTable);
    }

    private void addInvoiceInfo(Document document, WarehouseImportDTO importDTO, PdfFont font, PdfFont boldFont) {
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        infoTable.setWidth(UnitValue.createPercentValue(100));

        // Cột bên trái
        Cell leftCell = new Cell().setBorder(Border.NO_BORDER);
        leftCell.add(new Paragraph("Mã phiếu: " + importDTO.getReceiptCode())
                .setFont(boldFont)
                .setFontSize(11));
        leftCell.add(new Paragraph("Ngày nhập: " + importDTO.getImportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .setFont(font)
                .setFontSize(11));
        infoTable.addCell(leftCell);

        // Cột bên phải
        Cell rightCell = new Cell().setBorder(Border.NO_BORDER);
        rightCell.add(new Paragraph("Mã kho: KHO001") // Giả định mã kho
                .setFont(boldFont)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.RIGHT));
        rightCell.add(new Paragraph("Số chứng từ: CT" + importDTO.getReceiptCode())
                .setFont(font)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.RIGHT));
        infoTable.addCell(rightCell);

        document.add(infoTable);
    }

    private void addDivider(Document document) {
        Table divider = new Table(1);
        divider.setWidth(UnitValue.createPercentValue(100));
        Cell dividerCell = new Cell()
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(PRIMARY_COLOR, 1))
                .setHeight(2)
                .setMarginTop(10)
                .setMarginBottom(10);
        divider.addCell(dividerCell);
        document.add(divider);
    }

    private void addSupplierInfo(Document document, Invoice invoice, WarehouseImportDTO importDTO, PdfFont font) {
        Table supplierTable = new Table(UnitValue.createPercentArray(new float[]{25, 75}));
        supplierTable.setWidth(UnitValue.createPercentValue(100));

        addInfoRow(supplierTable, "Nhà cung cấp:", importDTO.getSupplierName(), font);
        if (invoice.getSupplier() != null) {
            if (invoice.getSupplier().getAddress() != null && !invoice.getSupplier().getAddress().isEmpty()) {
                addInfoRow(supplierTable, "Địa chỉ:", invoice.getSupplier().getAddress(), font);
            }
            if (invoice.getSupplier().getPhone() != null && !invoice.getSupplier().getPhone().isEmpty()) {
                addInfoRow(supplierTable, "Số điện thoại:", invoice.getSupplier().getPhone(), font);
            }
        }
        if (importDTO.getNotes() != null && !importDTO.getNotes().isEmpty()) {
            addInfoRow(supplierTable, "Ghi chú:", importDTO.getNotes(), font);
        }

        document.add(supplierTable);
    }

    private void addProductTable(Document document, WarehouseImportDTO importDTO, PdfFont font, PdfFont boldFont) {
        Table productTable = new Table(UnitValue.createPercentArray(new float[]{5, 15, 30, 15, 10, 15, 15}));
        productTable.setWidth(UnitValue.createPercentValue(100));

        // Header
        addTableHeader(productTable, "STT", boldFont);
        addTableHeader(productTable, "Mã SP", boldFont);
        addTableHeader(productTable, "Tên sản phẩm", boldFont);
        addTableHeader(productTable, "Thương hiệu", boldFont);
        addTableHeader(productTable, "Số lượng", boldFont);
        addTableHeader(productTable, "Đơn giá (VNĐ)", boldFont);
        addTableHeader(productTable, "Thành tiền (VNĐ)", boldFont);

        // Dữ liệu sản phẩm
        int index = 1;
        for (InvoiceItemDTO product : importDTO.getProducts()) {
            addTableCell(productTable, String.valueOf(index++), font, TextAlignment.CENTER);
            addTableCell(productTable, product.getProductCode(), font, TextAlignment.LEFT);
            addTableCell(productTable, product.getProductName(), font, TextAlignment.LEFT);
            addTableCell(productTable, product.getBrand(), font, TextAlignment.LEFT);
            addTableCell(productTable, String.valueOf((int) product.getQuantity()), font, TextAlignment.CENTER);
            addTableCell(productTable, formatCurrency(product.getPrice()), font, TextAlignment.RIGHT);
            addTableCell(productTable, formatCurrency(product.getTotal()), font, TextAlignment.RIGHT);
        }

        document.add(productTable);
    }

    private void addSummaryTable(Document document, WarehouseImportDTO importDTO, PdfFont font, PdfFont boldFont) {
        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{80, 20}));
        summaryTable.setWidth(UnitValue.createPercentValue(50));
        summaryTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        summaryTable.setMarginTop(15);

        // Tổng số lượng
        addSummaryRow(summaryTable, "Tổng số lượng nhập:", String.valueOf((int) importDTO.getTotalQuantity()), font);
        // Tổng tiền hàng
        addSummaryRow(summaryTable, "Tổng tiền hàng:", formatCurrency(importDTO.getTotalAmount()), font);
        // Chiết khấu
        if (importDTO.getDiscount() > 0) {
            addSummaryRow(summaryTable, "Chiết khấu:", formatCurrency(importDTO.getDiscount()), font);
        }
        // VAT
        if (importDTO.getVat() > 0) {
            long vatAmount = Math.round(importDTO.getTotalAmount() * importDTO.getVat() / 100);
            addSummaryRow(summaryTable, "VAT (" + (int) importDTO.getVat() + "%):", formatCurrency(vatAmount), font);
        }
        // Chi phí khác
        if (importDTO.getAdditionalFees() > 0) {
            addSummaryRow(summaryTable, "Chi phí khác:", formatCurrency(importDTO.getAdditionalFees()), font);
        }

        // Đường phân cách
        Cell separatorCell = new Cell(1, 2)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(SECONDARY_COLOR, 0.5f))
                .setHeight(1)
                .setPaddingTop(5)
                .setPaddingBottom(5);
        summaryTable.addCell(separatorCell);

        // Tổng giá trị nhập
        addSummaryRow(summaryTable, "TỔNG GIÁ TRỊ NHẬP:", formatCurrency(importDTO.getGrandTotal()), boldFont, 12, PRIMARY_COLOR);

        document.add(summaryTable);
    }

    private void addSignatureSection(Document document, PdfFont font, PdfFont boldFont) {
        Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{33, 33, 33}));
        signatureTable.setWidth(UnitValue.createPercentValue(100));
        signatureTable.setMarginTop(30);

        // Người lập phiếu
        signatureTable.addCell(new Cell()
                .add(new Paragraph("Người lập phiếu")
                        .setFont(boldFont)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));
        // Người nhập kho
        signatureTable.addCell(new Cell()
                .add(new Paragraph("Người nhập kho")
                        .setFont(boldFont)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));
        // Người giao hàng
        signatureTable.addCell(new Cell()
                .add(new Paragraph("Người giao hàng")
                        .setFont(boldFont)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));

        // Ký, ghi rõ họ tên
        signatureTable.addCell(new Cell()
                .add(new Paragraph("(Ký, ghi rõ họ tên)")
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));
        signatureTable.addCell(new Cell()
                .add(new Paragraph("(Ký, ghi rõ họ tên)")
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));
        signatureTable.addCell(new Cell()
                .add(new Paragraph("(Ký, ghi rõ họ tên)")
                        .setFont(font)
                        .setFontSize(9)
                        .setTextAlignment(TextAlignment.CENTER))
                .setBorder(Border.NO_BORDER));

        document.add(signatureTable);
    }

    private void addTableHeader(Table table, String text, PdfFont font) {
        Cell cell = new Cell();
        cell.setBackgroundColor(TABLE_HEADER_COLOR);
        cell.add(new Paragraph(text)
                .setFont(font)
                .setFontSize(10)
                .setFontColor(ColorConstants.WHITE));
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setPadding(6);
        cell.setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, BORDER_WIDTH));
        table.addHeaderCell(cell);
    }

    private void addTableCell(Table table, String text, PdfFont font, TextAlignment alignment) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text)
                .setFont(font)
                .setFontSize(10)
                .setFontColor(SECONDARY_COLOR));
        cell.setTextAlignment(alignment);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setPadding(6);
        cell.setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, BORDER_WIDTH));
        table.addCell(cell);
    }

    private void addInfoRow(Table table, String label, String value, PdfFont font) {
        Cell labelCell = new Cell();
        labelCell.setBorder(Border.NO_BORDER);
        labelCell.add(new Paragraph(label)
                .setFont(font)
                .setFontSize(10)
                .setFontColor(SECONDARY_COLOR));
        labelCell.setPadding(4);
        table.addCell(labelCell);

        Cell valueCell = new Cell();
        valueCell.setBorder(Border.NO_BORDER);
        valueCell.add(new Paragraph(value)
                .setFont(font)
                .setFontSize(10)
                .setFontColor(SECONDARY_COLOR));
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }

    private void addSummaryRow(Table table, String label, String value, PdfFont font) {
        addSummaryRow(table, label, value, font, 10, SECONDARY_COLOR);
    }

    private void addSummaryRow(Table table, String label, String value, PdfFont font, float fontSize, DeviceRgb color) {
        Cell labelCell = new Cell();
        labelCell.setBorder(Border.NO_BORDER);
        labelCell.add(new Paragraph(label)
                .setFont(font)
                .setFontSize(fontSize)
                .setTextAlignment(TextAlignment.RIGHT));
        labelCell.setPadding(4);
        table.addCell(labelCell);

        Cell valueCell = new Cell();
        valueCell.setBorder(Border.NO_BORDER);
        valueCell.add(new Paragraph(value)
                .setFont(font)
                .setFontSize(fontSize)
                .setFontColor(color)
                .setTextAlignment(TextAlignment.RIGHT));
        valueCell.setPadding(4);
        table.addCell(valueCell);
    }

    private WarehouseImportDTO convertToDTO(Invoice invoice) {
        WarehouseImportDTO dto = new WarehouseImportDTO();
        dto.setId(invoice.getId());
        dto.setReceiptCode(invoice.getReceiptCode());
        dto.setImportDate(invoice.getImportDate());
        if (invoice.getSupplier() != null) {
            dto.setSupplierId(Integer.valueOf(invoice.getSupplier().getSupplierID()));
            dto.setSupplierName(invoice.getSupplier().getSupplierName());
        } else {
            dto.setSupplierId(null);
            dto.setSupplierName("Không xác định");
        }

        dto.setNotes(invoice.getNotes());
        dto.setDiscount(invoice.getDiscount());
        dto.setVat(invoice.getVat());
        dto.setAdditionalFees(invoice.getAdditionalFees());

        List<InvoiceItemDTO> itemDTOs = new ArrayList<>();
        for (InvoiceItem item : invoice.getProducts()) {
            itemDTOs.add(convertToItemDTO(item));
        }
        dto.setProducts(itemDTOs);

        double totalAmount = 0;
        long totalQuantity = 0;
        for (InvoiceItemDTO item : itemDTOs) {
            totalAmount += item.getTotal();
            totalQuantity += item.getQuantity();
        }
        dto.setTotalAmount(totalAmount);
        dto.setTotalQuantity(totalQuantity);

        double amountAfterDiscount = totalAmount - invoice.getDiscount();
        double vatAmount = amountAfterDiscount * invoice.getVat() / 100;
        double grandTotal = amountAfterDiscount + vatAmount + invoice.getAdditionalFees();
        dto.setGrandTotal(grandTotal);

        return dto;
    }

    private InvoiceItemDTO convertToItemDTO(InvoiceItem item) {
        InvoiceItemDTO dto = new InvoiceItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProductId());
        dto.setProductCode(item.getProductCode());
        dto.setProductName(item.getProductName());
        dto.setBrand(item.getBrand());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setPaymentStatus(item.getPaymentStatus());
        dto.setTotal(item.getQuantity() * item.getPrice());
        return dto;
    }

    private String formatCurrency(long amount) {
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return currencyFormat.format(amount);
    }

    private String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return currencyFormat.format(Math.round(amount));
    }
}