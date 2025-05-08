package org.example.electricstore.DTO.product;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

    private Integer productID;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    @Size(max = 100, message = "Tên sản phẩm không được dài quá 100 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9\\s-]+$", message = "Tên sản phẩm không được chứa ký tự đặc biệt")
    private String name;

    private String productCode;

    @NotBlank(message = "Ảnh sản phẩm không được để trống")
    private String mainImageUrl;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @DecimalMin(value = "1000.0", message = "Giá sản phẩm phải lớn hơn 1000 VND")
    @Digits(integer = 10, fraction = 2, message = "Giá sản phẩm không hợp lệ")
    private Double price;

    @NotNull(message = "Giá nhập không được để trống")
    @DecimalMin(value = "1000.0", message = "Giá nhập phải lớn hơn 1000 VND")
    @Digits(integer = 10, fraction = 2, message = "Giá nhập không hợp lệ")
    private Double importPrice;

    @NotBlank(message = "Mô tả sản phẩm không được để trống")
    private String description;

    @PastOrPresent(message = "Ngày tạo không hợp lệ")
    private LocalDateTime createAt;

    @PastOrPresent(message = "Ngày cập nhật không hợp lệ")
    private LocalDateTime updateAt;

    @NotNull(message = "Danh mục không được để trống")
    private Integer categoryId;

    @NotNull(message = "Thương hiệu không được để trống")
    private Integer brandId;

    @NotNull(message = "Nhà cung cấp không được để trống")
    private Integer id; // ID của Supplier

    private Double screenSize;
    private Integer camera;
    private Integer frontCamera;

    @NotBlank(message = "Màu không được để trống")
    private String color;

    private String cpu;
    private String gpu;

    @Pattern(regexp = "^([1-9][0-9]*GB)?$", message = "RAM phải có định dạng đúng, ví dụ: 8GB, 16GB")
    private String ram;

    @Pattern(regexp = "^([1-9][0-9]*GB)?$", message = "ROM phải có định dạng đúng, ví dụ: 128GB, 256GB")
    private String rom;

    private String os;
    private String osVersion;

    @Pattern(regexp = "^([1-9][0-9]*mAh)?$", message = "Pin phải có định dạng đúng, ví dụ: 4000mAh, 5000mAh")
    private String battery;

    private String screenType;
    private String screenResolution;
    private String ports;
    private Integer weight;

    public void setSupplierId(Integer supplierId) {
        this.id = supplierId;
    }
}