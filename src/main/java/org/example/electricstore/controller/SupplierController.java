package org.example.electricstore.controller;

import org.example.electricstore.DTO.supplier.SupplierDTO;
import org.example.electricstore.exception.supplier.SupplierError;
import org.example.electricstore.exception.supplier.SupplierException;
import org.example.electricstore.model.Product;
import org.example.electricstore.model.Supplier;
import org.example.electricstore.service.impl.SupplierService;
import org.example.electricstore.service.interfaces.IProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/Admin/suppliers-manager")
public class SupplierController {

    @Autowired
    private SupplierService supplierService;
    @Autowired
    private IProductService productService;

    @GetMapping
    public String listSuppliers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String successMessage,
            @RequestParam(required = false) String errorMessage,
            Model model) {

        SupplierDTO supplierDTO = new SupplierDTO();
        supplierDTO.setSupplierCode(supplierService.generateNewSupplierCode());
        model.addAttribute("supplierDTO", supplierDTO);
        model.addAttribute("editSupplier", new SupplierDTO());

        if (successMessage != null && !successMessage.isEmpty()) {
            model.addAttribute("successMessage", successMessage);
        }
        if (errorMessage != null && !errorMessage.isEmpty()) {
            model.addAttribute("errorMessage", errorMessage);
        }

        Page<Supplier> supplierPage;

        if (filter != null && keyword != null && !keyword.trim().isEmpty()) {
            supplierPage = searchByFilter(filter, keyword, page, size);
            model.addAttribute("isSearch", true);
            if (supplierPage.isEmpty()) {
                model.addAttribute("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
            }
        } else {
            supplierPage = supplierService.getSuppliers(page, size);
            model.addAttribute("isSearch", false);
        }

        if (page >= supplierPage.getTotalPages() && supplierPage.getTotalPages() > 0) {
            int newPage = Math.max(0, supplierPage.getTotalPages() - 1);
            return "redirect:/Admin/suppliers-manager?page=" + newPage + "&size=" + size;
        }
        if (supplierPage.getTotalElements() == 0 && page > 0) {
            return "redirect:/Admin/suppliers-manager?page=0&size=" + size;
        }

        model.addAttribute("suppliers", supplierPage.getContent());
        model.addAttribute("currentPage", supplierPage.getNumber());
        model.addAttribute("totalPages", supplierPage.getTotalPages());
        model.addAttribute("totalItems", supplierPage.getTotalElements());
        model.addAttribute("pageSize", size);
        model.addAttribute("filter", filter);
        model.addAttribute("keyword", keyword);

        return "admin/suppliers/listSupplier";
    }

    @GetMapping("/generate-code")
    @ResponseBody
    public String generateSupplierCode() {
        return supplierService.generateNewSupplierCode();
    }

    // Phương thức tìm kiếm với phân trang
    private Page<Supplier> searchByFilter(String filter, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        switch (filter) {
            case "supplierCode":
                return supplierService.searchSuppliersByAllFields(keyword, null, null, null, null, pageable);
            case "supplierName":
                return supplierService.searchSuppliersByAllFields(null, keyword, null, null, null, pageable);
            case "phone":
                return supplierService.searchSuppliersByAllFields(null, null, null, keyword, null, pageable);
            default:
                return supplierService.getSuppliers(page, size);
        }
    }

    @GetMapping("/{id}")
    public String getSupplier(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "0") int productPage,
            @RequestParam(defaultValue = "10") int productSize,
            Model model) {

        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        if (supplier.isPresent()) {
            model.addAttribute("supplier", supplier.get());

            Page<Product> productsPage = productService.getProductsBySupplierPaginated(id, productPage, productSize);

            model.addAttribute("products", productsPage.getContent());
            model.addAttribute("productCurrentPage", productsPage.getNumber());
            model.addAttribute("productTotalPages", productsPage.getTotalPages());
            model.addAttribute("productTotalItems", productsPage.getTotalElements());
            model.addAttribute("productPageSize", productSize);

            return "admin/suppliers/supplierDetail";
        }
        return "redirect:/Admin/suppliers-manager";
    }

    @GetMapping("/get/{id}")
    @ResponseBody
    public SupplierDTO getSupplierForEdit(@PathVariable Integer id) {
        Optional<Supplier> supplier = supplierService.getSupplierById(id);
        if (supplier.isPresent()) {
            Supplier s = supplier.get();
            SupplierDTO dto = new SupplierDTO();
            dto.setId(s.getSupplierID().intValue());
            dto.setSupplierCode(s.getSupplierCode());
            dto.setSupplierName(s.getSupplierName());
            dto.setAddress(s.getAddress());
            dto.setPhone(s.getPhone());
            dto.setEmail(s.getEmail());
            dto.setCreatedAt(s.getCreateAt());
            dto.setUpdatedAt(s.getUpdateAt());
            return dto;
        }
        return new SupplierDTO();
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addSupplier(@Valid @RequestBody SupplierDTO supplierDTO,
                                         BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            List<FieldError> emptyFieldErrors = bindingResult.getFieldErrors().stream()
                    .filter(error -> {
                        String code = error.getCode();
                        return code != null && (
                                code.contains("NotBlank") ||
                                        code.contains("NotNull") ||
                                        error.getDefaultMessage().contains("không được để trống")
                        );
                    })
                    .collect(Collectors.toList());

            for (FieldError error : emptyFieldErrors) {
                errors.put(error.getField(), error.getDefaultMessage());
            }

            // Thêm các lỗi validate khác cho các trường chưa có lỗi "không được để trống"
            for (FieldError error : bindingResult.getFieldErrors()) {
                if (!errors.containsKey(error.getField())) {
                    errors.put(error.getField(), error.getDefaultMessage());
                }
            }

            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Supplier supplier = new Supplier();
            supplier.setSupplierCode(supplierDTO.getSupplierCode());
            supplier.setSupplierName(supplierDTO.getSupplierName());
            supplier.setAddress(supplierDTO.getAddress());
            supplier.setPhone(supplierDTO.getPhone());
            supplier.setEmail(supplierDTO.getEmail());
            supplierService.addSupplier(supplier);

            return ResponseEntity.ok("Thêm nhà cung cấp thành công.");
        } catch (SupplierException ex) {
            errors.put(mapErrorCodeToField(ex.getErrorCode()), ex.getMessage());
            return ResponseEntity.badRequest().body(errors);
        } catch (Exception e) {
            errors.put("globalError", "Lỗi khi thêm nhà cung cấp: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> updateSupplier(@Valid @RequestBody SupplierDTO supplierDTO,
                                            BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        if (bindingResult.hasErrors()) {
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Supplier supplier = new Supplier();
            supplier.setSupplierID(supplierDTO.getId().intValue());
            supplier.setSupplierCode(supplierDTO.getSupplierCode());
            supplier.setSupplierName(supplierDTO.getSupplierName());
            supplier.setAddress(supplierDTO.getAddress());
            supplier.setPhone(supplierDTO.getPhone());
            supplier.setEmail(supplierDTO.getEmail());

            supplierService.updateSupplier(supplier.getSupplierID(), supplier);
            return ResponseEntity.ok("Chỉnh sửa nhà cung cấp thành công.");
        } catch (SupplierException ex) {
            errors.put(mapErrorCodeToField(ex.getErrorCode()), ex.getMessage());
            return ResponseEntity.badRequest().body(errors);
        } catch (Exception e) {
            errors.put("globalError", "Lỗi khi chỉnh sửa nhà cung cấp: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteSuppliers(@RequestBody List<Integer> supplierIds) {
        Map<String, Object> response = new HashMap<>();
        try {
            supplierService.deleteSuppliers(supplierIds);

            int pageSize = 5;
            Page<Supplier> supplierPage = supplierService.getSuppliers(0, pageSize);
            int totalPages = supplierPage.getTotalPages();

            totalPages = Math.max(totalPages, 1);

            response.put("success", true);
            response.put("message", "Nhà cung cấp đã được xóa thành công.");
            response.put("totalPages", totalPages);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Lỗi khi xóa nhà cung cấp!");
            return ResponseEntity.badRequest().body(response);
        }
    }
    private String mapErrorCodeToField(SupplierError errorCode) {
        switch (errorCode) {
            case INVALID_NAME_FORMAT:
                return "supplierName";
            case INVALID_PHONE_FORMAT:
                return "phone";
            case INVALID_EMAIL_FORMAT:
                return "email";
            case INVALID_ADDRESS_FORMAT:
                return "address";
            case DUPLICATE_PHONE:
                return "phone";
            case DUPLICATE_EMAIL:
                return "email";
            case DUPLICATE_SUPPLIER_CODE:
                return "supplierCode";
            default:
                return "globalError";
        }
    }
}