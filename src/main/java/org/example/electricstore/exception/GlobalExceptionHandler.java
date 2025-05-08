package org.example.electricstore.exception;

import org.example.electricstore.DTO.customer.CustomerDTO;
import org.example.electricstore.DTO.product.ProductDTO;
import org.example.electricstore.exception.customer.CustomerException;
import org.example.electricstore.exception.product.ProductError;
import org.example.electricstore.exception.product.ProductException;
import org.example.electricstore.service.impl.BrandService;
import org.example.electricstore.service.impl.CategoryService;
import org.example.electricstore.service.impl.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import org.example.electricstore.service.impl.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final CustomerService customerService;
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final SupplierService supplierService;

    public GlobalExceptionHandler(CustomerService customerService,
                                  CategoryService categoryService,
                                  BrandService brandService,
                                  SupplierService supplierService) {
        this.customerService = customerService;
        this.categoryService = categoryService;
        this.brandService = brandService;
        this.supplierService = supplierService;
    }

    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(HttpServletRequest req) {
        ModelAndView modelAndView = new ModelAndView("admin/customer/listCustomer");
        String field = req.getParameter("searchField");
        String key = req.getParameter("searchInput");
        modelAndView.addObject("formatError",
                "Dữ liệu \"" + key + "\" không đúng định dạng \"" + field.toUpperCase() + "\" !");
        modelAndView.addObject("field" , field);
        modelAndView.addObject("filterKeyWord" , key);
        modelAndView.addObject("customers" ,this.customerService.getAllCustomers(1 , 5));
        return modelAndView;
    }

    @ExceptionHandler(CustomerException.class)
    public ModelAndView handleCustomerException(HttpServletRequest request, CustomerException ex) {
        ModelAndView modelAndView;
        Integer id = request.getParameter("customerId") != null
                ? Integer.parseInt(request.getParameter("customerId"))
                : null;

        CustomerDTO customerDTO = CustomerDTO.builder()
                .customerId(id)
                .customerName(request.getParameter("customerName"))
                .phoneNumber(request.getParameter("phoneNumber"))
                .address(request.getParameter("address"))
                .birthDate(LocalDate.parse(request.getParameter("birthDate")))
                .build();
        if (request.getRequestURI().startsWith("/Admin/customers/update")) {
            modelAndView = new ModelAndView("admin/customer/editCustomer");
        } else {
            modelAndView = new ModelAndView("admin/customer/listCustomer");
        }
        modelAndView.addObject("customerDTO" , customerDTO) ;
        modelAndView.addObject("error", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception ex, Model model, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        String errorType = ex.getClass().getSimpleName();

        model.addAttribute("error", errorType);
        model.addAttribute("message", ex.getMessage());
        model.addAttribute("trace", ex.getStackTrace());
        model.addAttribute("status", status.value());
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("timestamp", LocalDateTime.now());

        return "error";
    }

    @ExceptionHandler(ProductException.class)
    public ModelAndView handleProductException(HttpServletRequest request, ProductException ex) {
        ModelAndView modelAndView;
        Integer id = request.getParameter("productID") != null
                ? Integer.parseInt(request.getParameter("productID"))
                : null;

        // Lấy giá trị từ request
        String price = request.getParameter("price");
        String importPrice = request.getParameter("importPrice");
        Integer categoryId = null;
        Integer brandId = null;
        Integer supplierId = null;

        try {
            if (request.getParameter("categoryId") != null && !request.getParameter("categoryId").isEmpty()) {
                categoryId = Integer.parseInt(request.getParameter("categoryId"));
            }
            if (request.getParameter("brandId") != null && !request.getParameter("brandId").isEmpty()) {
                brandId = Integer.parseInt(request.getParameter("brandId"));
            }
            if (request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
                supplierId = Integer.parseInt(request.getParameter("id"));
            }
        } catch (NumberFormatException e) {
            // Bỏ qua nếu có lỗi chuyển đổi
        }

        // Sử dụng builder pattern giống như CustomerDTO
        ProductDTO productDTO = ProductDTO.builder()
                .productID(id)
                .name(request.getParameter("name"))
                .productCode(request.getParameter("productCode"))
                .description(request.getParameter("description"))
                .mainImageUrl(request.getParameter("mainImageUrl"))
                .color(request.getParameter("color"))
                .cpu(request.getParameter("cpu"))
                .gpu(request.getParameter("gpu"))
                .ram(request.getParameter("ram"))
                .rom(request.getParameter("rom"))
                .os(request.getParameter("os"))
                .osVersion(request.getParameter("osVersion"))
                .battery(request.getParameter("battery"))
                .categoryId(categoryId)
                .brandId(brandId)
                .id(supplierId)
                .build();

        // Đặt view dựa trên URL
        if (request.getRequestURI().contains("/edit")) {
            modelAndView = new ModelAndView("admin/product_brand_category/editProduct");
        } else if (request.getRequestURI().contains("/add")) {
            modelAndView = new ModelAndView("admin/product_brand_category/addProduct");
        } else {
            modelAndView = new ModelAndView("admin/product_brand_category/listProduct");
        }

        // Thêm đối tượng DTO vào model
        modelAndView.addObject("product", productDTO);

        // Xác định trường lỗi cụ thể dựa trên loại lỗi
        switch (ex.getErrorCode()) {
            case EMPTY_PRODUCT_NAME:
            case INVALID_PRODUCT_NAME_LENGTH:
            case INVALID_PRODUCT_NAME_FORMAT:
                modelAndView.addObject("nameError", ex.getMessage());
                break;
            case EMPTY_PRODUCT_PRICE:
                modelAndView.addObject("priceError", ex.getMessage());
                break;
            case EMPTY_IMPORT_PRICE:
                modelAndView.addObject("importPriceError", ex.getMessage());
                break;
            case EMPTY_DESCRIPTION:
                modelAndView.addObject("descriptionError", ex.getMessage());
                break;
            case EMPTY_CATEGORY:
                modelAndView.addObject("categoryError", ex.getMessage());
                break;
            case EMPTY_BRAND:
                modelAndView.addObject("brandError", ex.getMessage());
                break;
            case EMPTY_SUPPLIER:
                modelAndView.addObject("supplierError", ex.getMessage());
                break;
            case INVALID_RAM_FORMAT:
                modelAndView.addObject("ramError", ex.getMessage());
                break;
            case INVALID_ROM_FORMAT:
                modelAndView.addObject("romError", ex.getMessage());
                break;
            case INVALID_BATTERY_FORMAT:
                modelAndView.addObject("batteryError", ex.getMessage());
                break;
            case EMPTY_PRODUCT_IMAGE:
                modelAndView.addObject("imageError", ex.getMessage());
                break;
            default:
                modelAndView.addObject("error", ex.getMessage());
                break;
        }

        modelAndView.addObject("categories", categoryService.getAllCategories());
        modelAndView.addObject("brands", brandService.getAllBrands());
        modelAndView.addObject("suppliers", supplierService.getAllSuppliers());

        return modelAndView;
    }
}