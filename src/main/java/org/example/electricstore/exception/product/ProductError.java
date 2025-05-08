package org.example.electricstore.exception.product;

import lombok.Getter;

@Getter
public enum ProductError {
    EMPTY_PRODUCT_NAME("Tên sản phẩm không được để trống"),
    INVALID_PRODUCT_NAME_LENGTH("Tên sản phẩm không được dài quá 100 ký tự"),
    INVALID_PRODUCT_NAME_FORMAT("Tên sản phẩm không được chứa ký tự đặc biệt"),
    EMPTY_PRODUCT_PRICE("Giá sản phẩm không được để trống!"),
    EMPTY_IMPORT_PRICE("Giá nhập không được để trống!"),
    EMPTY_COLOR("Màu sắc không được để trống. "),
    EMPTY_DESCRIPTION("Mô tả sản phẩm không được để trống"),
    EMPTY_CATEGORY("Danh mục không được để trống"),
    EMPTY_BRAND("Thương hiệu không được để trống"),
    EMPTY_SUPPLIER("Nhà cung cấp không được để trống"),
    INVALID_RAM_FORMAT("RAM phải có định dạng đúng, ví dụ: 8GB, 16GB"),
    INVALID_ROM_FORMAT("ROM phải có định dạng đúng, ví dụ: 128GB, 256GB"),
    INVALID_BATTERY_FORMAT("Pin phải có định dạng đúng, ví dụ: 4000mAh, 5000mAh"),
    EMPTY_PRODUCT_IMAGE("Vui lòng chọn ít nhất một ảnh cho sản phẩm");

    private final String message;

    ProductError(String message) {
        this.message = message;
    }
}