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
        // Tìm kiếm danh mục theo mã hoặc tên nếu có từ khóa
        if (!filterKeyword.isEmpty()) {
            if ("code".equals(searchType)) {
                categoryPage = categoryService.findByCategoryCodeContainingPaged(filterKeyword, pageable);
            } else {
                categoryPage = categoryService.findByCategoryNameContainingPaged(filterKeyword, pageable);
            }
            // Hiển thị thông báo nếu không tìm thấy kết quả
            if (categoryPage.isEmpty()) {
                modelAndView.addObject("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
            }
        } else {
            // Lấy toàn bộ danh mục với phân trang nếu không có từ khóa
            categoryPage = categoryService.getAllCategoriesPaged(pageable);
        }

        // Chuyển hướng đến trang cuối cùng nếu trang hiện tại vượt quá tổng số trang
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

        // Thêm dữ liệu vào model để hiển thị trên giao diện
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

    // Lấy mã danh mục tiếp theo để gợi ý khi thêm mới
    @GetMapping("/next-code")
    @ResponseBody
    public String getNextCategoryCode() {
        return categoryService.generateNextCategoryCode();
    }

    // Thêm danh mục mới
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                         BindingResult bindingResult) {
        // Kiểm tra lỗi xác thực dữ liệu đầu vào
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
            // Chuyển DTO thành entity và lưu vào cơ sở dữ liệu
            Category category = categoryMapper.toEntity(categoryDTO);
            categoryService.saveCategory(category);

            // Trả về phản hồi thành công
            Map<String, String> response = new HashMap<>();
            response.put("success", "true");
            response.put("message", "Thêm danh mục thành công.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Trả về lỗi nếu có ngoại lệ xảy ra
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi khi thêm danh mục: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Cập nhật danh mục hiện có
    @PostMapping("/edit")
    @ResponseBody
    public ResponseEntity<?> updateCategory(@Valid @RequestBody CategoryDTO categoryDTO,
                                            BindingResult bindingResult) {
        // Kiểm tra lỗi xác thực dữ liệu đầu vào
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Kiểm tra trùng lặp tên danh mục, ngoại trừ danh mục đang chỉnh sửa
        if (categoryService.existsByCategoryNameAndNotId(categoryDTO.getCategoryName(), categoryDTO.getCategoryID())) {
            Map<String, String> errors = new HashMap<>();
            errors.put("categoryName", "Tên danh mục đã tồn tại");
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            // Tìm danh mục hiện có theo ID
            Optional<Category> existingCategoryOpt = categoryService.getCategoryById(categoryDTO.getCategoryID());

            if (existingCategoryOpt.isPresent()) {
                // Cập nhật thông tin danh mục và lưu vào cơ sở dữ liệu
                Category existingCategory = existingCategoryOpt.get();
                categoryMapper.updateEntityFromDto(categoryDTO, existingCategory);
                existingCategory.setUpdateAt(LocalDateTime.now());
                categoryService.saveCategory(existingCategory);

                // Trả về phản hồi thành công
                Map<String, String> response = new HashMap<>();
                response.put("success", "true");
                response.put("message", "Chỉnh sửa danh mục thành công.");
                return ResponseEntity.ok(response);
            } else {
                // Trả về lỗi nếu không tìm thấy danh mục
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Không tìm thấy danh mục!");
                return ResponseEntity.badRequest().body(errorResponse);
            }
        } catch (Exception e) {
            // Trả về lỗi nếu có ngoại lệ xảy ra
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Lỗi khi chỉnh sửa danh mục: " + e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    // Xóa danh mục theo danh sách ID
    @PostMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> deleteCategories(@RequestBody List<Integer> categoryIds) {
        try {
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
            // Trả về lỗi nếu có ngoại lệ xảy ra
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