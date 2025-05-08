package org.example.electricstore.exception.product;

import lombok.Getter;

@Getter
public enum ProductError {
    INVALID_PRODUCT_NAME_LENGTH("Tên sản phẩm không được dài quá 100 ký tự"),
    INVALID_RAM_FORMAT("RAM phải có định dạng đúng, ví dụ: 8GB, 16GB"),
    INVALID_ROM_FORMAT("ROM phải có định dạng đúng, ví dụ: 128GB, 256GB"),
    PRODUCT_IMAGE_REQUIRED("Ảnh sản phẩm không được để trống"),
    INVALID_BATTERY_FORMAT("Pin phải có định dạng đúng, ví dụ: 4000mAh, 5000mAh");
    private final String message;

    ProductError(String message) {
        this.message = message;
    }
}