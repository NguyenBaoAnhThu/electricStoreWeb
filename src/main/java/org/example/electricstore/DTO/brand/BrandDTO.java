package org.example.electricstore.DTO.brand;

import org.example.electricstore.model.Brand;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrandDTO {

    private Integer brandID;

    private String brandCode;

    @NotBlank(message = "Tên thương hiệu không được để trống")
    @Size(min = 2, max = 100, message = "Tên thương hiệu phải từ 2 đến 100 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9\\s]+$", message = "Tên thương hiệu không được chứa ký tự đặc biệt")
    private String brandName;

    @Size(max = 100, message = "Tên quốc gia không được dài quá 100 ký tự")
    @Pattern(regexp = "^[\\p{L}0-9\\s]+$", message = "Tên quốc gia không được chứa ký tự đặc biệt")
    private String country;

    private Boolean status = true;

    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public BrandDTO(Brand brand) {
        this.brandID = brand.getBrandID();
        this.brandCode = brand.getBrandCode();
        this.brandName = brand.getBrandName();
        this.country = brand.getCountry();
        this.createAt = brand.getCreateAt();
        this.updateAt = brand.getUpdateAt();
    }

    public Brand toEntity() {
        Brand brand = new Brand();
        brand.setBrandID(this.brandID);
        brand.setBrandCode(this.brandCode);
        brand.setBrandName(this.brandName);
        brand.setCountry(this.country);
        if (this.createAt != null) {
            brand.setCreateAt(this.createAt);
        }
        if (this.updateAt != null) {
            brand.setUpdateAt(this.updateAt);
        }
        return brand;
    }
}