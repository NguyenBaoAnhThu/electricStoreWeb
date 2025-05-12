package org.example.electricstore.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.electricstore.DTO.warehouse.InvoiceDTO;
import org.example.electricstore.mapper.product.ProductMapper;
import org.example.electricstore.model.*;
import org.example.electricstore.repository.*;
import org.example.electricstore.service.impl.BrandService;
import org.example.electricstore.service.impl.WareHouseService;
import org.example.electricstore.service.interfaces.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Admin/ware-houses")
@RequiredArgsConstructor
public class WareHouseController {

    private final WareHouseService wareHouseService;
    private final IProductService productService;
    private final ProductMapper productMapper;
    private final BrandService brandService;
    private final ISupplierRepository supplierRepository;
    private final IProductRepository productRepository;
    private final InvoiceItemRepository invoiceItemRepository;
    private final InvoiceRepository invoiceRepository;
    private final IWareHouseRepository wareHouseRepository;

    @ModelAttribute("brands")
    public List<Brand> brands() {
        return this.brandService.getAllBrands();
    }

    @GetMapping
    public ModelAndView showWareHouse(
            @RequestParam(name = "importDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate importDate,
            @RequestParam(name = "brand", required = false, defaultValue = "") String brand,
            @RequestParam(name = "statusStock", required = false, defaultValue = "0") Integer statusStock,
            @RequestParam(name = "productCode", required = false) String productCode,
            @RequestParam(name = "productName", required = false, defaultValue = "") String productName,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size) {

        // Xử lý các tham số tìm kiếm
        String nameFilter = productName != null ? productName.trim() : "";
        String codeFilter = productCode != null ? productCode.trim() : "";
        String brandFilter = brand != null ? brand.trim() : "";

        // Tạo Pageable với trang bắt đầu từ 0 (page-1)
        Pageable pageable = PageRequest.of(page - 1, size);

        // Gọi service để tìm kiếm warehouse
        Page<WareHouse> wareHousePage = wareHouseService.searchWareHouses(
                importDate, brandFilter, statusStock, codeFilter, nameFilter, pageable
        );

        // Lấy danh sách suppliers để hiển thị trong dropdown (nếu cần)
        List<Supplier> suppliers = supplierRepository.findAll();

        // Chuẩn bị ModelAndView
        ModelAndView modelAndView = new ModelAndView("admin/warehouse/warehouse-table");

        // Thêm dữ liệu vào model
        modelAndView.addObject("wareHousePage", wareHousePage);
        modelAndView.addObject("wareHouses", wareHousePage.getContent());

        // Trả lại các tham số tìm kiếm để giữ form state
        modelAndView.addObject("importDate", importDate);
        modelAndView.addObject("brand", brandFilter);
        modelAndView.addObject("statusStock", statusStock);
        modelAndView.addObject("productCode", codeFilter);
        modelAndView.addObject("productName", nameFilter);

        // Thông tin phân trang
        modelAndView.addObject("page", page);
        modelAndView.addObject("size", size);
        modelAndView.addObject("totalPages", wareHousePage.getTotalPages());
        modelAndView.addObject("currentPage", page);

        // Thêm danh sách suppliers
        modelAndView.addObject("suppliers", suppliers);

        return modelAndView;
    }

    @GetMapping("/history_warehouse")
    @Transactional(readOnly = true)
    public String showHistoryWarehouse(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filterCode,
            @RequestParam(required = false) String filterBrand,
            @RequestParam(required = false) String filterUser,
            @RequestParam(required = false) String filterFromDate,
            @RequestParam(required = false) String filterToDate,
            Model model) {

        // Tạo đối tượng Pageable với thông tin trang và kích thước
        Pageable pageable = PageRequest.of(page, size);

        // Lấy danh sách invoice (không phải invoice item)
        Page<Invoice> invoicePage;

        // Nếu có tiêu chí lọc thì thực hiện tìm kiếm
        if (StringUtils.hasText(filterCode) || StringUtils.hasText(filterBrand) ||
                StringUtils.hasText(filterUser) || StringUtils.hasText(filterFromDate) ||
                StringUtils.hasText(filterToDate)) {

            invoicePage = invoiceRepository.findWithFilters(
                    filterCode, filterBrand, filterUser,
                    filterFromDate, filterToDate, pageable);
        } else {
            // Nếu không có tiêu chí lọc, lấy tất cả
            invoicePage = invoiceRepository.findAll(pageable);
        }

        // Thêm thông tin tính toán tổng tiền cho mỗi hóa đơn
        List<InvoiceDTO> invoiceDTOs = invoicePage.getContent().stream()
                .map(invoice -> {
                    InvoiceDTO dto = new InvoiceDTO();
                    dto.setInvoice(invoice);

                    // Tính tổng tiền từ tất cả các mục trong invoice
                    double total = invoice.getProducts().stream()
                            .mapToDouble(item -> item.getQuantity() * item.getPrice())
                            .sum();

                    // Điều chỉnh thêm/bớt theo chiết khấu và phí khác
                    total = total - invoice.getDiscount() + invoice.getAdditionalFees();

                    dto.setTotal(total);

                    // Lấy trạng thái từ invoice item đầu tiên (giả sử tất cả các item trong cùng invoice có cùng trạng thái)
                    if (!invoice.getProducts().isEmpty()) {
                        dto.setPaymentStatus(invoice.getProducts().get(0).getPaymentStatus());
                    }

                    return dto;
                })
                .collect(Collectors.toList());

        model.addAttribute("invoices", invoiceDTOs);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", invoicePage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "admin/warehouse/history_warehouse";
    }
    @GetMapping("/import")
    public String showImport(@RequestParam(value = "supplierId", required = false) Integer supplierId, Model model) {
        List<Supplier> suppliers = supplierRepository.findAll();
        List<Product> products;

        if (supplierId != null) {
            products = productRepository.getProductsBySupplierId(supplierId);
        } else {
            products = productRepository.findAll();
        }

        // Gán giá từ WareHouse (mới nhất hoặc logic phù hợp)
        for (Product product : products) {
            // Tìm danh sách kho theo productId
            List<WareHouse> relatedWarehouses = wareHouseRepository.findByProductIdOrderByImportDateDesc(product.getProductID());

            // Gán giá nếu có warehouse
            if (!relatedWarehouses.isEmpty()) {
                WareHouse latest = relatedWarehouses.get(0); // Lấy phiếu nhập gần nhất
                product.setPrice(latest.getPrice());          // Gán vào Product để hiển thị ở view
            }
        }
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("products", products);
        model.addAttribute("selectedSupplier", supplierId);
        return "admin/warehouse/import";
    }
}