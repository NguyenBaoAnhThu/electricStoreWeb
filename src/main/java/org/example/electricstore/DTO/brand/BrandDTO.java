package org.example.electricstore.DTO.brand;

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

}