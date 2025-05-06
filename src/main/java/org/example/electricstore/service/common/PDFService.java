package org.example.electricstore.service.common;

import org.example.electricstore.DTO.order.OrderDTO;
import org.example.electricstore.DTO.order.ProductOrderDTO;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class PDFService {

    // Định nghĩa màu sắc chủ đạo
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(209, 0, 36); // #D10024 - Màu đỏ của shop
    private static final DeviceRgb SECONDARY_COLOR = new DeviceRgb(51, 51, 51); // #333333 - Màu đen chữ
    private static final DeviceRgb TABLE_HEADER_COLOR = new DeviceRgb(33, 37, 41); // #212529 - Màu đen nhạt
    private static final float BORDER_WIDTH = 0.5f;

    public byte[] createInvoicePDF(OrderDTO orderDTO) {
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

            // Thêm tiêu đề hóa đơn
            document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(18)
                    .setFontColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(10)
                    .setMarginBottom(5));

            // Thêm số hóa đơn và ngày tạo
            document.add(new Paragraph("Số hóa đơn: " + orderDTO.getInvoiceNumber())
                    .setFont(vietnameseFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER));

            // Lấy ngày hiện tại để hiển thị
            String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            document.add(new Paragraph("Ngày: " + currentDate)
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

            // Thông tin khách hàng
            document.add(new Paragraph("\nTHÔNG TIN KHÁCH HÀNG")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(14)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(10)
                    .setMarginBottom(10));

            // Bảng thông tin khách hàng
            Table customerInfoTable = new Table(UnitValue.createPercentArray(new float[]{30, 70}));
            customerInfoTable.setWidth(UnitValue.createPercentValue(100));

            addCustomerInfoRow(customerInfoTable, "Tên khách hàng:", orderDTO.getCustomerDTO().getCustomerName(), vietnameseFont);
            addCustomerInfoRow(customerInfoTable, "Số điện thoại:", orderDTO.getCustomerDTO().getPhoneNumber(), vietnameseFont);
            addCustomerInfoRow(customerInfoTable, "Địa chỉ:", orderDTO.getCustomerDTO().getAddress(), vietnameseFont);
            addCustomerInfoRow(customerInfoTable, "Email:", orderDTO.getCustomerDTO().getEmail(), vietnameseFont);

            document.add(customerInfoTable);
            document.add(new Paragraph("\n"));

            // Tiêu đề chi tiết sản phẩm
            document.add(new Paragraph("CHI TIẾT SẢN PHẨM")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(14)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(10)
                    .setMarginBottom(10));

            // Bảng danh sách sản phẩm
            Table productTable = new Table(UnitValue.createPercentArray(new float[]{5, 40, 15, 20, 20}));
            productTable.setWidth(UnitValue.createPercentValue(100));

            // Header của bảng sản phẩm
            addTableHeader(productTable, "STT", vietnameseBoldFont);
            addTableHeader(productTable, "Sản phẩm", vietnameseBoldFont);
            addTableHeader(productTable, "Số lượng", vietnameseBoldFont);
            addTableHeader(productTable, "Đơn giá (VNĐ)", vietnameseBoldFont);
            addTableHeader(productTable, "Thành tiền (VNĐ)", vietnameseBoldFont);

            // Dữ liệu sản phẩm
            int index = 1;
            double total = 0;
            for (ProductOrderDTO product : orderDTO.getProductOrderDTOList()) {
                double subtotal = product.getQuantity() * product.getPriceIndex();
                total += subtotal;

                // Thêm hàng sản phẩm
                addTableCell(productTable, String.valueOf(index++), vietnameseFont, TextAlignment.CENTER);
                addTableCell(productTable, product.getProductName(), vietnameseFont, TextAlignment.LEFT);
                addTableCell(productTable, String.valueOf(product.getQuantity()), vietnameseFont, TextAlignment.CENTER);
                addTableCell(productTable, formatCurrency(product.getPriceIndex()), vietnameseFont, TextAlignment.RIGHT);
                addTableCell(productTable, formatCurrency((int) subtotal), vietnameseFont, TextAlignment.RIGHT);
            }

            document.add(productTable);

            // Bảng hiển thị tổng tiền, giảm giá và tổng thanh toán
            Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{80, 20}));
            summaryTable.setWidth(UnitValue.createPercentValue(100));
            summaryTable.setMarginTop(10);

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
            subtotalValueCell.add(new Paragraph(formatCurrency((int) total)))
                    .setFont(vietnameseFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.RIGHT);
            summaryTable.addCell(subtotalValueCell);

            // Giảm giá theo số tiền
            if (orderDTO.getDiscountAmount() != null && orderDTO.getDiscountAmount() > 0) {
                Cell discountLabelCell = new Cell();
                discountLabelCell.setBorder(Border.NO_BORDER);
                discountLabelCell.add(new Paragraph("Giảm giá:"))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(discountLabelCell);

                Cell discountValueCell = new Cell();
                discountValueCell.setBorder(Border.NO_BORDER);
                discountValueCell.add(new Paragraph(formatCurrency(orderDTO.getDiscountAmount().intValue())))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(discountValueCell);
            }

            // Giảm giá theo phần trăm
            if (orderDTO.getDiscountPercent() != null && orderDTO.getDiscountPercent() > 0) {
                double discountAmount = total * orderDTO.getDiscountPercent() / 100;

                Cell discountPercentLabelCell = new Cell();
                discountPercentLabelCell.setBorder(Border.NO_BORDER);
                discountPercentLabelCell.add(new Paragraph("Giảm giá (" + orderDTO.getDiscountPercent().intValue() + "%):"))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(discountPercentLabelCell);

                Cell discountPercentValueCell = new Cell();
                discountPercentValueCell.setBorder(Border.NO_BORDER);
                discountPercentValueCell.add(new Paragraph(formatCurrency((int) discountAmount)))
                        .setFont(vietnameseFont)
                        .setFontSize(12)
                        .setTextAlignment(TextAlignment.RIGHT);
                summaryTable.addCell(discountPercentValueCell);
            }

            // Tính tổng thanh toán sau khi giảm giá
            double discountAmount = 0;
            if (orderDTO.getDiscountAmount() != null) {
                discountAmount += orderDTO.getDiscountAmount();
            }
            if (orderDTO.getDiscountPercent() != null && orderDTO.getDiscountPercent() > 0) {
                discountAmount += total * orderDTO.getDiscountPercent() / 100;
            }
            double grandTotal = total - discountAmount;

            // Đường kẻ phân cách trước tổng thanh toán
            Cell separatorCell = new Cell(1, 2);
            separatorCell.setBorder(Border.NO_BORDER);
            separatorCell.setBorderBottom(new SolidBorder(SECONDARY_COLOR, 0.5f));
            separatorCell.setHeight(1);
            separatorCell.setPaddingTop(5);
            separatorCell.setPaddingBottom(5);
            summaryTable.addCell(separatorCell);

            // Tổng thanh toán
            Cell totalLabelCell = new Cell();
            totalLabelCell.setBorder(Border.NO_BORDER);
            totalLabelCell.add(new Paragraph("TỔNG THANH TOÁN:"))
                    .setFont(vietnameseBoldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.RIGHT);
            summaryTable.addCell(totalLabelCell);

            Cell totalValueCell = new Cell();
            totalValueCell.setBorder(Border.NO_BORDER);
            totalValueCell.add(new Paragraph(formatCurrency((int) grandTotal)))
                    .setFont(vietnameseBoldFont)
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontColor(PRIMARY_COLOR);
            summaryTable.addCell(totalValueCell);

            document.add(summaryTable);

            // Vẽ đường phân cách cuối
            document.add(divider);

            // Phần chân trang
            document.add(new Paragraph("\nCảm ơn quý khách đã mua hàng!")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(20));

            document.add(new Paragraph("Electric Store - Địa chỉ cửa hàng điện tử uy tín")
                    .setFont(vietnameseFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(5));

            document.add(new Paragraph("Điện thoại: 0123.456.789 | Email: contact@electricstore.com")
                    .setFont(vietnameseFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER));

            document.add(new Paragraph("Website: www.electricstore.com")
                    .setFont(vietnameseFont)
                    .setFontSize(10)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10));

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file PDF: " + e.getMessage(), e);
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

    private void addCustomerInfoRow(Table table, String label, String value, PdfFont font) {
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

    private String formatCurrency(int amount) {
        return String.format("%,d VNĐ", amount);
    }
}