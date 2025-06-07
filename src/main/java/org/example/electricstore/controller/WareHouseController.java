package org.example.electricstore.controller;

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
import org.springframework.data.domain.Sort;
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

        String nameFilter = productName != null ? productName.trim() : "";
        String codeFilter = productCode != null ? productCode.trim() : "";
        String brandFilter = brand != null ? brand.trim() : "";

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("product.productCode"));
        Page<WareHouse> wareHousePage = wareHouseService.searchWareHouses(
                importDate, brandFilter, statusStock, codeFilter, nameFilter, pageable
        );

        List<Supplier> suppliers = supplierRepository.findAll();

        ModelAndView modelAndView = new ModelAndView("admin/warehouse/warehouse-table");
        modelAndView.addObject("wareHousePage", wareHousePage);
        modelAndView.addObject("wareHouses", wareHousePage.getContent());
        modelAndView.addObject("importDate", importDate);
        modelAndView.addObject("brand", brandFilter);
        modelAndView.addObject("statusStock", statusStock);
        modelAndView.addObject("productCode", codeFilter);
        modelAndView.addObject("productName", nameFilter);
        modelAndView.addObject("page", page);
        modelAndView.addObject("size", size);
        modelAndView.addObject("totalPages", wareHousePage.getTotalPages());
        modelAndView.addObject("currentPage", page);
        modelAndView.addObject("suppliers", suppliers);

        return modelAndView;
    }
    @GetMapping("/Admin/ware-houses/invoice_form_warehouses/{id}")
    public String showInvoiceDetails(@PathVariable("id") Integer invoiceId, Model model) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid invoice ID: " + invoiceId));
        invoice.calculateTotalPrice(); // Ensure totalPrice is calculated
        model.addAttribute("invoice", invoice);
        return "admin/warehouse/invoice_form_warehouses";
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

        Pageable pageable = PageRequest.of(page, size);
        Page<Invoice> invoicePage;
        boolean hasFilters = StringUtils.hasText(filterCode) || StringUtils.hasText(filterBrand) ||
                StringUtils.hasText(filterUser) || StringUtils.hasText(filterFromDate) ||
                StringUtils.hasText(filterToDate);

        if (hasFilters) {
            invoicePage = invoiceRepository.findWithFilters(
                    filterCode, filterBrand, filterUser,
                    filterFromDate, filterToDate, pageable);
        } else {
            invoicePage = invoiceRepository.findAll(pageable);
        }

        if (invoicePage.isEmpty() && hasFilters) {
            model.addAttribute("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
        }

        // Chuyển dữ liệu sang InvoiceDTO, sử dụng totalPrice từ Invoice
        List<InvoiceDTO> invoiceDTOs = invoicePage.getContent().stream()
                .map(invoice -> {
                    InvoiceDTO dto = new InvoiceDTO();
                    dto.setInvoice(invoice);
                    // Gọi calculateTotalPrice để đảm bảo totalPrice được cập nhật
                    invoice.calculateTotalPrice();
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

        for (Product product : products) {
            List<WareHouse> relatedWarehouses = wareHouseRepository.findByProductIdOrderByImportDateDesc(product.getProductID());
            if (!relatedWarehouses.isEmpty()) {
                WareHouse latest = relatedWarehouses.get(0);
                product.setPrice(latest.getPrice());
            }
        }
        model.addAttribute("suppliers", suppliers);
        model.addAttribute("products", products);
        model.addAttribute("selectedSupplier", supplierId);
        return "admin/warehouse/import";
    }
}