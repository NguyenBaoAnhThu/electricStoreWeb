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
import org.example.electricstore.repository.ISupplierRepository;
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
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(0, 123, 255); // Màu xanh chủ đạo
    private static final DeviceRgb SECONDARY_COLOR = new DeviceRgb(51, 51, 51); // #333333 - Màu đen chữ
    private static final DeviceRgb TABLE_HEADER_COLOR = new DeviceRgb(33, 37, 41); // #212529 - Màu đen nhạt
    private static final float BORDER_WIDTH = 0.5f;

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private ISupplierRepository supplierRepository;

    public byte[] createImportPDF(Integer invoiceId) throws IOException {
        // Lấy thông tin hóa đơn từ database
        Invoice invoice = invoiceRepository.findByIdWithProducts(invoiceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu nhập kho"));

        // Convert sang DTO để xử lý
        WarehouseImportDTO importDTO = convertToDTO(invoice);

        // Tạo PDF
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            // Tạo PDF writer và document
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(36, 36, 36, 36); // 36pt = 0.5 inch

            // Tải font tiếng Việt
            InputStream fontStream = new ClassPathResource("fonts/vuArial.ttf").getInputStream();
            PdfFont vietnameseFont = PdfFontFactory.createFont(fontStream.readAllBytes(), PdfEncodings.IDENTITY_H);

            // Tải font in đậm (nếu có)
            InputStream boldFontStream = null;
            PdfFont vietnameseBoldFont = null;
            try {
                boldFontStream = new ClassPathResource("fonts/vuArialBold.ttf").getInputStream();
                vietnameseBoldFont = PdfFontFactory.createFont(boldFontStream.readAllBytes(), PdfEncodings.IDENTITY_H);
            } catch (Exception e) {
                // Nếu không có font in đậm, sử dụng font thường
                vietnameseBoldFont = vietnameseFont;
            }

            // Thêm logo (nếu có)
            try {
                InputStream logoStream = new ClassPathResource("static/img/logo.png").getInputStream();
                Image logo = new Image(ImageDataFactory.create(logoStream.readAllBytes()));
                logo.setWidth(150);
                logo.setHorizontalAlignment(HorizontalAlignment.CENTER);
                document.add(logo);
            } catch (Exception e) {
                // Nếu không có logo, thêm tên cửa hàng
                document.add(new Paragraph("ELECTRIC STORE")
                        .setFont(vietnameseBoldFont)
                        .setFontSize(20)
                        .setFontColor(PRIMARY_COLOR)
                        .setTextAlignment(TextAlignment.CENTER));
            }

            // Thêm tiêu đề phiếu nhập kho
            document.add(new Paragraph("PHIẾU NHẬP KHO")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(18)
                    .setFontColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10)
                    .setMarginBottom(5));

            // Thêm mã phiếu và ngày tạo
            document.add(new Paragraph("Mã phiếu: " + importDTO.getReceiptCode())
                    .setFont(vietnameseFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            // Format ngày tháng
            String formattedDate = importDTO.getImportDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            document.add(new Paragraph("Ngày nhập: " + formattedDate)
                    .setFont(vietnameseFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20));

            // Vẽ đường phân cách
            Table divider = new Table(1);
            divider.setWidth(UnitValue.createPercentValue(100));
            Cell dividerCell = new Cell();
            dividerCell.setBorder(Border.NO_BORDER);
            dividerCell.setBorderBottom(new SolidBorder(PRIMARY_COLOR, 1));
            dividerCell.setHeight(1);
            divider.addCell(dividerCell);
            document.add(divider);

            // Thông tin nhà cung cấp
            document.add(new Paragraph("\nTHÔNG TIN NHÀ CUNG CẤP")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(14)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(10)
                    .setMarginBottom(10));

            // Bảng thông tin nhà cung cấp
            Table supplierInfoTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            supplierInfoTable.setWidth(UnitValue.createPercentValue(100));

            addInfoRow(supplierInfoTable, "Nhà cung cấp:", importDTO.getSupplierName(), vietnameseFont);

            // Thêm thông tin địa chỉ và số điện thoại của nhà cung cấp nếu có
            if (invoice.getSupplier() != null) {
                if (invoice.getSupplier().getAddress() != null && !invoice.getSupplier().getAddress().isEmpty()) {
                    addInfoRow(supplierInfoTable, "Địa chỉ:", invoice.getSupplier().getAddress(), vietnameseFont);
                }
                if (invoice.getSupplier().getPhone() != null && !invoice.getSupplier().getPhone().isEmpty()) {
                    addInfoRow(supplierInfoTable, "Số điện thoại:", invoice.getSupplier().getPhone(), vietnameseFont);
                }
            }

            // Thêm ghi chú nếu có
            if (importDTO.getNotes() != null && !importDTO.getNotes().isEmpty()) {
                addInfoRow(supplierInfoTable, "Ghi chú:", importDTO.getNotes(), vietnameseFont);
            }

            document.add(supplierInfoTable);
            document.add(new Paragraph("\n"));

            // Tiêu đề chi tiết sản phẩm
            document.add(new Paragraph("CHI TIẾT SẢN PHẨM NHẬP")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(14)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(10)
                    .setMarginBottom(10));

            // Bảng danh sách sản phẩm
            Table productTable = new Table(UnitValue.createPercentArray(new float[]{5, 10, 30, 15, 10, 15, 15}));
            productTable.setWidth(UnitValue.createPercentValue(100));

            // Header của bảng sản phẩm
            addTableHeader(productTable, "STT", vietnameseBoldFont);
            addTableHeader(productTable, "Mã SP", vietnameseBoldFont);
            addTableHeader(productTable, "Tên sản phẩm", vietnameseBoldFont);
            addTableHeader(productTable, "Thương hiệu", vietnameseBoldFont);
            addTableHeader(productTable, "Số lượng", vietnameseBoldFont);
            addTableHeader(productTable, "Đơn giá (VNĐ)", vietnameseBoldFont);
            addTableHeader(productTable, "Thành tiền (VNĐ)", vietnameseBoldFont);

            // Dữ liệu sản phẩm
            int index = 1;
            for (InvoiceItemDTO product : importDTO.getProducts()) {
                addTableCell(productTable, String.valueOf(index++), vietnameseFont, TextAlignment.CENTER);
                addTableCell(productTable, product.getProductCode(), vietnameseFont, TextAlignment.LEFT);
                addTableCell(productTable, product.getProductName(), vietnameseFont, TextAlignment.LEFT);
                addTableCell(productTable, product.getBrand(), vietnameseFont, TextAlignment.LEFT);
                addTableCell(productTable, String.valueOf(product.getQuantity()), vietnameseFont, TextAlignment.CENTER);
                addTableCell(productTable, formatCurrency(product.getPrice()), vietnameseFont, TextAlignment.RIGHT);
                addTableCell(productTable, formatCurrency(product.getTotal()), vietnameseFont, TextAlignment.RIGHT);
            }

            document.add(productTable);

            // Bảng tổng hợp
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{80, 20}));
            summaryTable.setWidth(UnitValue.createPercentValue(100));
            summaryTable.setMarginTop(10);

            // Tổng số lượng
            Cell quantityLabelCell = new Cell();
            quantityLabelCell.setBorder(Border.NO_BORDER);
            quantityLabelCell.add(new Paragraph("Tổng số lượng nhập:"))
                    .setFont(vietnameseFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            summaryTable.addCell(quantityLabelCell);

            Cell quantityValueCell = new Cell();
            quantityValueCell.setBorder(Border.NO_BORDER);
            quantityValueCell.add(new Paragraph(String.valueOf(importDTO.getTotalQuantity())))
                    .setFont(vietnameseFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            summaryTable.addCell(quantityValueCell);

            // Tổng tiền hàng
            Cell subtotalLabelCell = new Cell();
            subtotalLabelCell.setBorder(Border.NO_BORDER);
            subtotalLabelCell.add(new Paragraph("Tổng tiền hàng:"))
                    .setFont(vietnameseFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            summaryTable.addCell(subtotalLabelCell);

            Cell subtotalValueCell = new Cell();
            subtotalValueCell.setBorder(Border.NO_BORDER);
            subtotalValueCell.add(new Paragraph(formatCurrency(importDTO.getTotalAmount())))
                    .setFont(vietnameseFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            summaryTable.addCell(subtotalValueCell);

            // Giảm giá
            if (importDTO.getDiscount() > 0) {
                Cell discountLabelCell = new Cell();
                discountLabelCell.setBorder(Border.NO_BORDER);
                discountLabelCell.add(new Paragraph("Chiết khấu:"))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(discountLabelCell);

                Cell discountValueCell = new Cell();
                discountValueCell.setBorder(Border.NO_BORDER);
                discountValueCell.add(new Paragraph(formatCurrency(importDTO.getDiscount())))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(discountValueCell);
            }

            // VAT
            if (importDTO.getVat() > 0) {
                Cell vatLabelCell = new Cell();
                vatLabelCell.setBorder(Border.NO_BORDER);
                vatLabelCell.add(new Paragraph("VAT (" + (int)importDTO.getVat() + "%):"))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(vatLabelCell);

                // Tính VAT trên số tiền sau khi giảm giá
                double amountAfterDiscount = importDTO.getTotalAmount() - importDTO.getDiscount();
                // Làm tròn để chỉ lấy số nguyên
                long vatAmount = Math.round(amountAfterDiscount * importDTO.getVat() / 100);

                Cell vatValueCell = new Cell();
                vatValueCell.setBorder(Border.NO_BORDER);
                vatValueCell.add(new Paragraph(formatCurrency(vatAmount)))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(vatValueCell);
            }

            // Chi phí khác
            if (importDTO.getAdditionalFees() > 0) {
                Cell feesLabelCell = new Cell();
                feesLabelCell.setBorder(Border.NO_BORDER);
                feesLabelCell.add(new Paragraph("Chi phí khác:"))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(feesLabelCell);

                Cell feesValueCell = new Cell();
                feesValueCell.setBorder(Border.NO_BORDER);
                feesValueCell.add(new Paragraph(formatCurrency(importDTO.getAdditionalFees())))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(feesValueCell);
            }

            // Đường kẻ phân cách trước tổng thanh toán
            Cell separatorCell = new Cell(1, 2);
            separatorCell.setBorder(Border.NO_BORDER);
            separatorCell.setBorderBottom(new SolidBorder(SECONDARY_COLOR, 0.5f));
            separatorCell.setHeight(1);
            separatorCell.setPaddingTop(5);
            separatorCell.setPaddingBottom(5);
            summaryTable.addCell(separatorCell);

            // Tổng giá trị nhập
            Cell totalLabelCell = new Cell();
            totalLabelCell.setBorder(Border.NO_BORDER);
            totalLabelCell.add(new Paragraph("TỔNG GIÁ TRỊ NHẬP:"))
                    .setFont(vietnameseBoldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.RIGHT);
            summaryTable.addCell(totalLabelCell);

            Cell totalValueCell = new Cell();
            totalValueCell.setBorder(Border.NO_BORDER);
            totalValueCell.add(new Paragraph(formatCurrency(importDTO.getGrandTotal())))
                    .setFont(vietnameseBoldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontColor(PRIMARY_COLOR);
            summaryTable.addCell(totalValueCell);

            document.add(summaryTable);

            // Vẽ đường phân cách cuối
            document.add(divider);

            // Phần chữ ký
            document.add(new Paragraph("\n"));

            Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
            signatureTable.setWidth(UnitValue.createPercentValue(100));

            signatureTable.addCell(new Cell().add(new Paragraph("Người nhập kho")
                            .setFont(vietnameseBoldFont)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(Border.NO_BORDER));

            signatureTable.addCell(new Cell().add(new Paragraph("Người giao hàng")
                            .setFont(vietnameseBoldFont)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(Border.NO_BORDER));

            signatureTable.addCell(new Cell().add(new Paragraph("(Ký, ghi rõ họ tên)")
                            .setFont(vietnameseFont)
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(Border.NO_BORDER));

            signatureTable.addCell(new Cell().add(new Paragraph("(Ký, ghi rõ họ tên)")
                            .setFont(vietnameseFont)
                            .setFontSize(10)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setBorder(Border.NO_BORDER));

            document.add(signatureTable);

            // Chân trang
            document.add(new Paragraph("\nElectric Store - Hệ thống quản lý kho hàng")
                    .setFont(vietnameseFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            document.close();
            return outputStream.toByteArray();
        }
    }

    // Helper methods
    private void addTableHeader(Table table, String text, PdfFont font) {
        Cell cell = new Cell();
        cell.setBackgroundColor(TABLE_HEADER_COLOR);
        cell.add(new Paragraph(text).setFont(font).setFontColor(ColorConstants.WHITE));
        cell.setTextAlignment(TextAlignment.CENTER);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setPadding(8);
        table.addHeaderCell(cell);
    }

    private void addTableCell(Table table, String text, PdfFont font, TextAlignment alignment) {
        Cell cell = new Cell();
        cell.add(new Paragraph(text).setFont(font).setFontColor(SECONDARY_COLOR));
        cell.setTextAlignment(alignment);
        cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        cell.setPadding(8);
        // Thêm border mỏng
        cell.setBorder(new SolidBorder(ColorConstants.LIGHT_GRAY, BORDER_WIDTH));
        table.addCell(cell);
    }

    private void addInfoRow(Table table, String label, String value, PdfFont font) {
        Cell labelCell = new Cell();
        labelCell.setBorder(Border.NO_BORDER);
        labelCell.add(new Paragraph(label).setFont(font).setFontColor(SECONDARY_COLOR));
        labelCell.setPadding(5);
        table.addCell(labelCell);

        Cell valueCell = new Cell();
        valueCell.setBorder(Border.NO_BORDER);
        valueCell.add(new Paragraph(value).setFont(font).setFontColor(SECONDARY_COLOR));
        valueCell.setPadding(5);
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

        // Convert các sản phẩm
        List<InvoiceItemDTO> itemDTOs = new ArrayList<>();

        for (InvoiceItem item : invoice.getProducts()) {
            itemDTOs.add(convertToItemDTO(item));
        }

        dto.setProducts(itemDTOs);

        // Tính các giá trị tổng
        double totalAmount = 0;
        for (InvoiceItemDTO item : itemDTOs) {
            totalAmount += item.getTotal();
        }
        dto.setTotalAmount(totalAmount);

        long totalQuantity = 0;
        for (InvoiceItemDTO item : itemDTOs) {
            totalQuantity += item.getQuantity();
        }
        dto.setTotalQuantity(totalQuantity);

        // Tính tổng giá trị (sau thuế, chiết khấu, phí)
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
        return currencyFormat.format(amount);
    }
}