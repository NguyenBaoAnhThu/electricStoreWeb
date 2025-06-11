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
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;
import java.util.Locale;

@Service
public class PDFService {

    // Định nghĩa màu sắc chủ đạo
    private static final DeviceRgb PRIMARY_COLOR = new DeviceRgb(0, 102, 204);
    private static final DeviceRgb SECONDARY_COLOR = new DeviceRgb(33, 37, 41);
    private static final DeviceRgb TABLE_HEADER_COLOR = new DeviceRgb(0, 0, 0);
    private static final float BORDER_WIDTH = 0.3f; // Viền mỏng hơn cho tinh tế

    public byte[] createInvoicePDF(OrderDTO orderDTO) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(30, 30, 30, 30); // Giảm margin cho bố cục rộng rãi hơn

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

            // Tiêu đề hóa đơn
            document.add(new Paragraph("HÓA ĐƠN BÁN HÀNG")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(16)
                    .setFontColor(PRIMARY_COLOR)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginTop(15)
                    .setMarginBottom(10));

            // Thêm thông tin hóa đơn
            addInvoiceInfo(document, orderDTO, vietnameseFont, vietnameseBoldFont);

            // Vẽ đường phân cách
            addDivider(document);

            // Thông tin khách hàng
            document.add(new Paragraph("THÔNG TIN KHÁCH HÀNG")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(12)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(15)
                    .setMarginBottom(10));

            addCustomerInfo(document, orderDTO, vietnameseFont);

            // Chi tiết sản phẩm
            document.add(new Paragraph("CHI TIẾT SẢN PHẨM")
                    .setFont(vietnameseBoldFont)
                    .setFontSize(12)
                    .setFontColor(SECONDARY_COLOR)
                    .setMarginTop(15)
                    .setMarginBottom(10));

            addProductTable(document, orderDTO, vietnameseFont, vietnameseBoldFont);

            // Bảng tổng hợp
            addSummaryTable(document, orderDTO, vietnameseFont, vietnameseBoldFont);

            // Chân trang
            addFooter(document, vietnameseFont, vietnameseBoldFont);

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file PDF: " + e.getMessage(), e);
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

    private void addInvoiceInfo(Document document, OrderDTO orderDTO, PdfFont font, PdfFont boldFont) {
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        infoTable.setWidth(UnitValue.createPercentValue(100));

        // Cột bên trái
        Cell leftCell = new Cell().setBorder(Border.NO_BORDER);
        leftCell.add(new Paragraph("Số hóa đơn: " + orderDTO.getInvoiceNumber())
                .setFont(boldFont)
                .setFontSize(11));
        leftCell.add(new Paragraph("Ngày: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
                .setFont(font)
                .setFontSize(11));
        infoTable.addCell(leftCell);

        // Cột bên phải
        Cell rightCell = new Cell().setBorder(Border.NO_BORDER);
        rightCell.add(new Paragraph("Mã cửa hàng: CH001")
                .setFont(boldFont)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.RIGHT));
        rightCell.add(new Paragraph("Số chứng từ: CT" + orderDTO.getInvoiceNumber())
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

    private void addCustomerInfo(Document document, OrderDTO orderDTO, PdfFont font) {
        Table customerTable = new Table(UnitValue.createPercentArray(new float[]{25, 75}));
        customerTable.setWidth(UnitValue.createPercentValue(100));

        addInfoRow(customerTable, "Tên khách hàng:", orderDTO.getCustomerDTO().getCustomerName(), font);
        addInfoRow(customerTable, "Số điện thoại:", orderDTO.getCustomerDTO().getPhoneNumber(), font);
        addInfoRow(customerTable, "Địa chỉ:", orderDTO.getCustomerDTO().getAddress(), font);
        addInfoRow(customerTable, "Email:", orderDTO.getCustomerDTO().getEmail(), font);

        document.add(customerTable);
    }

    private void addProductTable(Document document, OrderDTO orderDTO, PdfFont font, PdfFont boldFont) {
        Table productTable = new Table(UnitValue.createPercentArray(new float[]{5, 40, 15, 20, 20}));
        productTable.setWidth(UnitValue.createPercentValue(100));

        // Header
        addTableHeader(productTable, "STT", boldFont);
        addTableHeader(productTable, "Sản phẩm", boldFont);
        addTableHeader(productTable, "Số lượng", boldFont);
        addTableHeader(productTable, "Đơn giá (VNĐ)", boldFont);
        addTableHeader(productTable, "Thành tiền (VNĐ)", boldFont);

        // Dữ liệu sản phẩm
        int index = 1;
        for (ProductOrderDTO product : orderDTO.getProductOrderDTOList()) {
            double subtotal = product.getQuantity() * product.getPriceIndex();
            addTableCell(productTable, String.valueOf(index++), font, TextAlignment.CENTER);
            addTableCell(productTable, product.getProductName(), font, TextAlignment.LEFT);
            addTableCell(productTable, String.valueOf(product.getQuantity()), font, TextAlignment.CENTER);
            addTableCell(productTable, formatCurrency(product.getPriceIndex()), font, TextAlignment.RIGHT);
            addTableCell(productTable, formatCurrency(subtotal), font, TextAlignment.RIGHT);
        }

        document.add(productTable);
    }

    private void addSummaryTable(Document document, OrderDTO orderDTO, PdfFont font, PdfFont boldFont) {
        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{80, 20}));
        summaryTable.setWidth(UnitValue.createPercentValue(50));
        summaryTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);
        summaryTable.setMarginTop(15);

        // Tính tổng tiền hàng
        double total = 0;
        for (ProductOrderDTO product : orderDTO.getProductOrderDTOList()) {
            total += product.getQuantity() * product.getPriceIndex();
        }

        // Tổng tiền hàng
        addSummaryRow(summaryTable, "Tổng tiền hàng:", formatCurrency(total), font);

        // Giảm giá theo số tiền
        double discountAmount = 0;
        if (orderDTO.getDiscountAmount() != null && orderDTO.getDiscountAmount() > 0) {
            discountAmount += orderDTO.getDiscountAmount();
            addSummaryRow(summaryTable, "Giảm giá:", formatCurrency(orderDTO.getDiscountAmount()), font);
        }

        // Giảm giá theo phần trăm
        if (orderDTO.getDiscountPercent() != null && orderDTO.getDiscountPercent() > 0) {
            double percentDiscount = total * orderDTO.getDiscountPercent() / 100;
            discountAmount += percentDiscount;
            addSummaryRow(summaryTable, "Giảm giá (" + orderDTO.getDiscountPercent().intValue() + "%):", formatCurrency(percentDiscount), font);
        }

        // Đường phân cách
        Cell separatorCell = new Cell(1, 2)
                .setBorder(Border.NO_BORDER)
                .setBorderBottom(new SolidBorder(SECONDARY_COLOR, 0.5f))
                .setHeight(1)
                .setPaddingTop(5)
                .setPaddingBottom(5);
        summaryTable.addCell(separatorCell);

        // Tổng thanh toán
        double grandTotal = total - discountAmount;
        addSummaryRow(summaryTable, "TỔNG THANH TOÁN:", formatCurrency(grandTotal), boldFont, 12, PRIMARY_COLOR);

        document.add(summaryTable);
    }

    private void addFooter(Document document, PdfFont font, PdfFont boldFont) {
        document.add(new Paragraph("\nCảm ơn quý khách đã mua hàng!")
                .setFont(boldFont)
                .setFontSize(11)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(20));
        document.add(new Paragraph("Electric Store - Địa chỉ cửa hàng điện tử uy tín")
                .setFont(font)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(5));
        document.add(new Paragraph("Điện thoại: (028) 1234 5678 | Email: contact@electricstore.com")
                .setFont(font)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER));
        document.add(new Paragraph("Website: www.electricstore.com")
                .setFont(font)
                .setFontSize(9)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10));
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

    private String formatCurrency(double amount) {
        NumberFormat currencyFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
        return currencyFormat.format(Math.round(amount));
    }
}