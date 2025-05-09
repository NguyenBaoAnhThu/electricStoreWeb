package org.example.electricstore.controller.admin;

import jakarta.servlet.http.HttpSession;
import org.example.electricstore.DTO.product.ProductDTO;
import org.example.electricstore.mapper.product.ProductMapper;
import org.example.electricstore.model.Product;
import org.example.electricstore.service.common.CloudinaryService;
import org.example.electricstore.service.impl.BrandService;
import org.example.electricstore.service.impl.CategoryService;
import org.example.electricstore.service.impl.ProductService;
import org.example.electricstore.service.impl.SupplierService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.beans.PropertyEditorSupport;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/Admin/product-manager")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping
    public String showListProduct(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "minPrice", required = false) Double minPrice,
            @RequestParam(name = "maxPrice", required = false) Double maxPrice,
            @RequestParam(name = "category", required = false) Integer categoryId,
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
            Model model) {

        Page<Product> productPage = productService.searchProducts(keyword, minPrice, maxPrice, categoryId, page - 1, size);
        List<Product> products = productPage.getContent();
        DecimalFormat decimalFormat = new DecimalFormat("#,### VND");

        for (Product product : products) {
            try {
                Double priceValue = (product.getPrice() != null) ? product.getPrice() : 0.0;
                product.setFormattedPrice(decimalFormat.format(priceValue));
            } catch (IllegalArgumentException e) {
                product.setFormattedPrice("0 VND");
            }
        }
        model.addAttribute("products", products);
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("category", categoryId);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());

        if (page > productPage.getTotalPages() && productPage.getTotalPages() > 0) {
            int newPage = Math.max(1, productPage.getTotalPages());
            StringBuilder redirectUrl = new StringBuilder("redirect:/Admin/product-manager?page=" + newPage);

            if (keyword != null) {
                redirectUrl.append("&keyword=").append(encodeParam(keyword));
            }
            if (minPrice != null) {
                redirectUrl.append("&minPrice=").append(minPrice);
            }
            if (maxPrice != null) {
                redirectUrl.append("&maxPrice=").append(maxPrice);
            }
            if (categoryId != null) {
                redirectUrl.append("&category=").append(categoryId);
            }

            return redirectUrl.toString();
        }

        if (productPage.getTotalElements() == 0 && page > 1) {
            StringBuilder redirectUrl = new StringBuilder("redirect:/Admin/product-manager?page=1");

            if (keyword != null) {
                redirectUrl.append("&keyword=").append(encodeParam(keyword));
            }
            if (minPrice != null) {
                redirectUrl.append("&minPrice=").append(minPrice);
            }
            if (maxPrice != null) {
                redirectUrl.append("&maxPrice=").append(maxPrice);
            }
            if (categoryId != null) {
                redirectUrl.append("&category=").append(categoryId);
            }

            return redirectUrl.toString();
        }

        if (products.isEmpty() && (keyword != null || minPrice != null || maxPrice != null || categoryId != null)) {
            model.addAttribute("noResultMessage", "Không tìm thấy kết quả phù hợp với dữ liệu tìm kiếm.");
        }

        return "admin/product_brand_category/listProduct";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            ProductDTO productDTO = productMapper.toDTO(product.get());
            model.addAttribute("product", productDTO);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            model.addAttribute("mainImageUrl", productDTO.getMainImageUrl());

            // Thêm các thuộc tính cho validation
            model.addAttribute("imageError", null); // Để hiển thị lỗi ảnh nếu có
            model.addAttribute("validationErrors", new HashMap<String, String>()); // Lưu trữ lỗi validation

            return "admin/product_brand_category/editProduct";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm!");
            return "redirect:/Admin/product-manager";
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Double.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text != null && !text.isEmpty()) {
                    try {
                        // Xóa tất cả các ký tự không phải số
                        String cleanedText = text.replaceAll("[^0-9]", "");
                        if (!cleanedText.isEmpty()) {
                            setValue(Double.parseDouble(cleanedText));
                        } else {
                            setValue(null);
                        }
                    } catch (NumberFormatException e) {
                        logger.error("Lỗi chuyển đổi giá: {}", text, e);
                        setValue(null);
                    }
                } else {
                    setValue(null);
                }
            }
        });
    }

    @PostMapping("/edit")
    public String editProduct(
            @Valid @ModelAttribute("product") ProductDTO productDTO,
            BindingResult bindingResult,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "importPrice", required = false) Double importPrice,
            Model model,
            RedirectAttributes redirectAttributes) {

        logger.info("Bắt đầu cập nhật sản phẩm ID: {}", productDTO.getProductID());

        // Xử lý vấn đề mã sản phẩm bị nhân lên
        Optional<Product> existingProductOpt = productService.getProductById(productDTO.getProductID());
        if (existingProductOpt.isPresent()) {
            // Lấy mã sản phẩm từ database
            String originalProductCode = existingProductOpt.get().getProductCode();

            // Nếu mã sản phẩm trong request chứa dấu phẩy, reset về giá trị gốc
            if (productDTO.getProductCode() != null && productDTO.getProductCode().contains(",")) {
                productDTO.setProductCode(originalProductCode);
            }
        }

        // Kiểm tra lỗi validation từ @Valid (ngoại trừ mainImageUrl nếu có ảnh)
        if (bindingResult.hasErrors() && (bindingResult.getFieldError("mainImageUrl") == null ||
                files == null || files.isEmpty() || files.stream().allMatch(f -> f.isEmpty()))) {

            logger.warn("Dữ liệu không hợp lệ khi cập nhật sản phẩm: {}", bindingResult.getAllErrors());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            model.addAttribute("mainImageUrl", productDTO.getMainImageUrl());
            return "admin/product_brand_category/editProduct";
        }

        // Kiểm tra tên sản phẩm có ký tự đặc biệt không
        if (productDTO.getName() != null && !productDTO.getName().matches("^[\\p{L}0-9\\s]+$")) {
            bindingResult.rejectValue("name", "error.product", "Tên sản phẩm không được chứa ký tự đặc biệt");
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            return "admin/product_brand_category/editProduct";
        }

        // Kiểm tra sản phẩm có tồn tại không
        if (!existingProductOpt.isPresent()) {
            logger.error("Không tìm thấy sản phẩm ID: {}", productDTO.getProductID());
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy sản phẩm!");
            return "redirect:/Admin/product-manager";
        }

        Product existingProduct = existingProductOpt.get();

        // Chuyển đổi từ DTO sang Entity
        Product product = productMapper.toEntity(productDTO);

        // Giữ giá trị create_at từ sản phẩm hiện có
        product.setCreateAt(existingProduct.getCreateAt());

        // Thiết lập mối quan hệ
        product.getProductDetail().setProduct(product);

        // Kiểm tra thông tin file ảnh
        if (files != null && !files.isEmpty()) {
            logger.info("Nhận được {} files khi cập nhật sản phẩm", files.size());
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                if (!file.isEmpty()) {
                    logger.info("File {}: {}, kích thước: {}", i+1, file.getOriginalFilename(), file.getSize());
                }
            }
        } else {
            logger.info("Không có files mới khi cập nhật sản phẩm");
        }

        // Kiểm tra xem có file ảnh mới được tải lên không
        boolean hasNewImage = files != null && files.stream().anyMatch(file -> !file.isEmpty());

        // Nếu không có ảnh mới, truyền null vào để giữ nguyên ảnh cũ
        List<MultipartFile> filesToUpdate = hasNewImage ? files : null;

        // Lưu sản phẩm vào database với giá nhập
        try {
            if (importPrice != null && importPrice > 0) {
                logger.info("Cập nhật sản phẩm với giá nhập: {}", importPrice);
                productService.updateProductWithImportPrice(product, importPrice, filesToUpdate);
            } else {
                logger.info("Cập nhật sản phẩm không có giá nhập");
                productService.updateProduct(product, filesToUpdate);
            }
            logger.info("Cập nhật sản phẩm thành công");
            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật sản phẩm thành công.");
            return "redirect:/Admin/product-manager";
        } catch (Exception e) {
            logger.error("Lỗi khi cập nhật sản phẩm: {}", e.getMessage(), e);
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            model.addAttribute("errorMessage", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
            return "admin/product_brand_category/editProduct";
        }
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        ProductDTO productDTO = new ProductDTO();
        // Tạo mã sản phẩm mới
        String nextCode = productService.generateProductCode();
        productDTO.setProductCode(nextCode);

        // Thiết lập các giá trị mặc định
        productDTO.setCreateAt(LocalDateTime.now());
        productDTO.setUpdateAt(LocalDateTime.now());

        // Thiết lập giá trị stock mặc định là 0
        productDTO.setStock(0);

        // Thêm các thuộc tính vào model
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("brands", brandService.getAllBrands());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());

        // Thêm các thuộc tính cho validation
        model.addAttribute("imageError", null);
        model.addAttribute("validationErrors", new HashMap<String, String>());

        return "admin/product_brand_category/addProduct";
    }

    @PostMapping("/add")
    public String addProduct(
            @Valid @ModelAttribute("product") ProductDTO productDTO,
            BindingResult bindingResult,
            @RequestParam(value = "files", required = false) List<MultipartFile> files,
            @RequestParam(value = "importPrice", required = false) Double importPrice,
            Model model,
            RedirectAttributes redirectAttributes) {

        logger.info("Bắt đầu thêm sản phẩm mới");

        // Tạo mã sản phẩm tự động
        String generatedCode = productService.generateProductCode();
        productDTO.setProductCode(generatedCode);
        logger.info("Mã sản phẩm tự động: {}", generatedCode);

        // Thiết lập giá trị stock mặc định là 0 nếu chưa được thiết lập
        if (productDTO.getStock() == null) {
            productDTO.setStock(0);
            logger.info("Thiết lập giá trị stock mặc định là 0");
        }

        // Log thông tin sản phẩm
        logger.info("Thông tin sản phẩm: Tên={}, Giá={}, Danh mục={}, Thương hiệu={}, Nhà cung cấp={}, Stock={}",
                productDTO.getName(),
                productDTO.getPrice(),
                productDTO.getCategoryId(),
                productDTO.getBrandId(),
                productDTO.getId(),
                productDTO.getStock());

        // Kiểm tra lỗi validation (trừ lỗi mainImageUrl nếu có ảnh)
        if (bindingResult.hasErrors() && (bindingResult.getFieldError("mainImageUrl") == null ||
                files == null || files.isEmpty())) {
            logger.warn("Dữ liệu không hợp lệ khi thêm sản phẩm: {}", bindingResult.getAllErrors());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            return "admin/product_brand_category/addProduct";
        }

        // Kiểm tra file ảnh
        if (files == null || files.isEmpty()) {
            logger.error("Không nhận được file ảnh");
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            return "admin/product_brand_category/addProduct";
        } else {
            boolean hasValidImage = false;
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    hasValidImage = true;
                    logger.info("File ảnh: tên={}, kích thước={}, loại={}",
                            file.getOriginalFilename(), file.getSize(), file.getContentType());
                }
            }

            if (!hasValidImage) {
                logger.error("Tất cả file ảnh đều trống");
                model.addAttribute("imageError", "Vui lòng chọn ít nhất một ảnh hợp lệ");
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("brands", brandService.getAllBrands());
                model.addAttribute("suppliers", supplierService.getAllSuppliers());
                return "admin/product_brand_category/addProduct";
            }
        }

        try {
            // Chuyển đổi từ DTO sang Entity
            Product product = productMapper.toEntity(productDTO);
            logger.info("Đã chuyển đổi từ DTO sang Entity");

            // Đảm bảo stock được thiết lập là 0 trong entity
            if (product.getStock() == null) {
                product.setStock(0);
            }

            // Thiết lập mối quan hệ giữa product và productDetail
            if (product.getProductDetail() != null) {
                product.getProductDetail().setProduct(product);
            }

            // Format lại giá nhập nếu cần
            if (importPrice != null) {
                logger.info("Giá nhập ban đầu: {}", importPrice);
            }

            // Lưu sản phẩm vào database với giá nhập
            logger.info("Bắt đầu lưu sản phẩm với ảnh và giá nhập");
            Product savedProduct = productService.saveProductWithImportPrice(product, product.getProductDetail(), importPrice, files);
            logger.info("Đã lưu sản phẩm thành công với ID: {}", savedProduct.getProductID());

            redirectAttributes.addFlashAttribute("successMessage", "Thêm sản phẩm thành công.");
            return "redirect:/Admin/product-manager";
        } catch (Exception e) {
            // Xử lý các lỗi không mong đợi
            logger.error("Lỗi khi thêm sản phẩm: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "Lỗi khi thêm sản phẩm: " + e.getMessage());
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("brands", brandService.getAllBrands());
            model.addAttribute("suppliers", supplierService.getAllSuppliers());
            return "admin/product_brand_category/addProduct";
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deleteProducts(@RequestBody List<Integer> productIds, HttpSession session) {
        try {
            productService.deleteProduct(productIds);
            session.setAttribute("successMessage", "Sản phẩm đã được xoá thành công.");
            return ResponseEntity.ok().body("{\"success\": true}");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("{\"success\": false, \"message\": \"Lỗi khi xóa sản phẩm!\"}");
        }
    }

    // Phương thức tiện ích để encode parameter URL an toàn
    private String encodeParam(String param) {
        try {
            return URLEncoder.encode(param, StandardCharsets.UTF_8);
        } catch (Exception e) {
            logger.error("Lỗi khi encode tham số: {}", param, e);
            return "";
        }
    }
}