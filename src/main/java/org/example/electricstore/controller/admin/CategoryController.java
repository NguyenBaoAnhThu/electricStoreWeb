package org.example.electricstore.controller.admin;

import org.example.electricstore.DTO.category.CategoryDTO;
import org.example.electricstore.mapper.category.CategoryMapper;
import org.example.electricstore.model.Category;
import org.example.electricstore.service.interfaces.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/Admin/category-manager")
public class CategoryController {

    private final ICategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryController(ICategoryService categoryService, CategoryMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping()
    public ModelAndView showListCategory(
            Authentication authentication,
            @RequestParam(name = "keyword", required = false, defaultValue = "") String keyword,
            @RequestParam(name = "searchType", required = false, defaultValue = "categoryName") String searchType,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size) {

        ModelAndView modelAndView = new ModelAndView("admin/product_brand_category/listCategory");
        String username = authentication.getName();
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage;

        String filterKeyword = keyword.trim();
        if (!filterKeyword.isEmpty()) {
            if ("code".equals(searchType)) {
                categoryPage = categoryService.findByCategoryCodeContainingPaged(filterKeyword, pageable);
            } else {
                categoryPage = categoryService.findByCategoryNameContainingPaged(filterKeyword, pageable);
            }
            if (categoryPage.isEmpty()) {
                modelAndView.addObject("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
            }
        } else {
            categoryPage = categoryService.getAllCategoriesPaged(pageable);
        }

        if (page >= categoryPage.getTotalPages() && categoryPage.getTotalPages() > 0) {
            int lastPage = Math.max(0, categoryPage.getTotalPages() - 1);
            ModelAndView redirectView = new ModelAndView("redirect:/Admin/category-manager");
            redirectView.addObject("page", lastPage);
            if (!filterKeyword.isEmpty()) {
                redirectView.addObject("keyword", filterKeyword);
                redirectView.addObject("searchType", searchType);
            }
            return redirectView;
        }

        modelAndView.addObject("categories", categoryPage.getContent());
        modelAndView.addObject("currentPage", page + 1);
        modelAndView.addObject("totalPages", categoryPage.getTotalPages() > 0 ? categoryPage.getTotalPages() : 1);
        modelAndView.addObject("keyword", filterKeyword);
        modelAndView.addObject("searchType", searchType);
        modelAndView.addObject("category", new CategoryDTO());
        modelAndView.addObject("username", username);
        modelAndView.addObject("size", size);

        return modelAndView;
    }

    @GetMapping("/next-code")
    @ResponseBody
    public String getNextCategoryCode() {
        return categoryService.generateNextCategoryCode();
    }

    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Kiểm tra trùng lặp tên danh mục
        if (categoryService.existsByCategoryName(categoryDTO.getCategoryName())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("categoryName", "Tên danh mục đã tồn tại");
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Category category = categoryMapper.toEntity(categoryDTO);
            categoryService.saveCategory(category);

            Map<String, String> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "Thêm danh mục thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi khi thêm danh mục: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Kiểm tra trùng lặp tên danh mục (ngoại trừ chính nó)
        if (categoryService.existsByCategoryNameAndNotId(categoryDTO.getCategoryName(), categoryDTO.getCategoryID())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("categoryName", "Tên danh mục đã tồn tại");
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            Optional<Category> existingCategoryOpt = categoryService.getCategoryById(categoryDTO.getCategoryID());

            if (existingCategoryOpt.isPresent()) {
                Category existingCategory = existingCategoryOpt.get();
                categoryMapper.updateEntityFromDto(categoryDTO, existingCategory);
                existingCategory.setUpdateAt(LocalDateTime.now());
                categoryService.saveCategory(existingCategory);

                Map<String, String> response = new HashMap<>();
                response.put("success", "true");
                response.put("message", "Cập nhật danh mục thành công.");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Không tìm thấy danh mục!");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi khi cập nhật danh mục: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteCategories(@RequestBody List<Integer> categoryIds) {
        try {
            // Check if any categories have related products
            if (categoryService.hasRelatedProducts(categoryIds)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Không thể xóa danh mục có sản phẩm liên quan.");
                return ResponseEntity.badRequest().body(response);
            }

            categoryService.deleteCategory(categoryIds);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Danh mục đã được xóa thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Lỗi khi xóa danh mục: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
    @GetMapping("/check-name")
    @ResponseBody
    public ResponseEntity<?> checkNameExists(@RequestParam String categoryName, @RequestParam(required = false) Integer id) {
        boolean exists;
        if (id != null) {
            exists = categoryService.existsByCategoryNameAndNotId(categoryName, id);
        } else {
            exists = categoryService.existsByCategoryName(categoryName);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/check-code")
    @ResponseBody
    public ResponseEntity<?> checkCodeExists(@RequestParam String categoryCode, @RequestParam(required = false) Integer id) {
        boolean exists;
        if (id != null) {
            exists = categoryService.existsByCategoryCodeAndNotId(categoryCode, id);
        } else {
            exists = categoryService.existsByCategoryCode(categoryCode);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("exists", exists);
        return ResponseEntity.ok(response);
    }
}