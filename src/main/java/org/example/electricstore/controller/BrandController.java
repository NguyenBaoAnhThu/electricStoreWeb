package org.example.electricstore.controller;

import org.example.electricstore.DTO.brand.BrandDTO;
import org.example.electricstore.model.Brand;
import org.example.electricstore.service.impl.BrandService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/Admin/brand-manager")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ModelAndView showListBrand(
            Authentication authentication,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            @RequestParam(name = "searchType", required = false, defaultValue = "all") String searchType) {

        ModelAndView modelAndView = new ModelAndView("admin/product_brand_category/listBrand");
        String username = authentication.getName();
        Page<Brand> brandPage;

        String filterKeyword = keyword.trim();
        if (!filterKeyword.isEmpty()) {
            switch (searchType) {
                case "brandName":
                    brandPage = brandService.findByBrandNameContainingPaginated(filterKeyword, page, size);
                    break;
                case "brandCode":
                    brandPage = brandService.findByBrandCodeContainingPaginated(filterKeyword, page, size);
                    break;
                default:
                    brandPage = brandService.getAllBrandsPaginated(page, size);
            }
            if (brandPage.isEmpty()) {
                modelAndView.addObject("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
            }
        } else {
            brandPage = brandService.getAllBrandsPaginated(page, size);
        }

        if (page >= brandPage.getTotalPages() && brandPage.getTotalPages() > 0) {
            int lastPage = Math.max(0, brandPage.getTotalPages() - 1);
            ModelAndView redirectView = new ModelAndView("redirect:/Admin/brand-manager");
            redirectView.addObject("page", lastPage);
            if (!filterKeyword.isEmpty()) {
                redirectView.addObject("keyword", filterKeyword);
            }
            return redirectView;
        }

        modelAndView.addObject("brands", brandPage.getContent());
        modelAndView.addObject("currentPage", page + 1);
        modelAndView.addObject("totalPages", brandPage.getTotalPages() > 0 ? brandPage.getTotalPages() : 1);
        modelAndView.addObject("keyword", filterKeyword);
        modelAndView.addObject("brand", new BrandDTO());
        modelAndView.addObject("username", username);
        modelAndView.addObject("size", size);
        return modelAndView;
    }

    @GetMapping("/generate-code")
    @ResponseBody
    public String generateBrandCode() {
        return brandService.generateNewBrandCode();
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addBrand(@Valid @RequestBody BrandDTO brandDTO,
                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Brand brand = new Brand();
            brand.setBrandName(brandDTO.getBrandName());
            if (brandDTO.getCountry() != null) {
                brand.setCountry(brandDTO.getCountry());
            }

            brandService.saveBrand(brand);
            return ResponseEntity.ok("Thêm thương hiệu thành công");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi khi thêm thương hiệu: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> updateBrand(@Valid @RequestBody BrandDTO brandDTO,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Brand brand = new Brand();
            brand.setBrandID(brandDTO.getBrandID());
            brand.setBrandName(brandDTO.getBrandName());
            brand.setCountry(brandDTO.getCountry());
            brand.setUpdateAt(LocalDateTime.now());

            brandService.saveBrand(brand);
            return ResponseEntity.ok("Cập nhật thương hiệu thành công.");
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi khi cập nhật thương hiệu: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteBrands(@RequestBody List<Integer> brandIds) {
        try {
            brandService.deleteBrand(brandIds);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Thương hiệu đã được xóa thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi xóa thương hiệu.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check-name")
    @ResponseBody
    public ResponseEntity<?> checkBrandNameExists(@RequestParam("brandName") String brandName) {
        boolean exists = brandService.existsByBrandName(brandName);
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}