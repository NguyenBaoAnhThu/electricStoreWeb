package org.example.electricstore.service.impl;

import org.example.electricstore.model.Brand;
import org.example.electricstore.repository.IBrandRepository;
import org.example.electricstore.service.interfaces.IBrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BrandService implements IBrandService {

    private final IBrandRepository brandRepository;

    public BrandService(IBrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    @Override
    public Optional<Brand> getBrandById(Integer brandID) {
        return brandRepository.findById(brandID);
    }

    @Override
    public Brand saveBrand(Brand brand) {
        // Nếu là brand có id (đang cập nhật)
        if (brand.getBrandID() != null && brandRepository.existsById(brand.getBrandID())) {
            Brand existingBrand = brandRepository.findById(brand.getBrandID()).orElse(null);
            if (existingBrand != null) {
                existingBrand.setBrandName(brand.getBrandName());
                if (brand.getCountry() != null) {
                    existingBrand.setCountry(brand.getCountry());
                }
                existingBrand.setUpdateAt(LocalDateTime.now());
                return brandRepository.save(existingBrand);
            }
        } else {
            // Nếu là brand mới (chưa có id), tạo brandCode
            brand.setBrandCode(generateNewBrandCode());
            brand.setCreateAt(LocalDateTime.now());
            brand.setUpdateAt(LocalDateTime.now());
            return brandRepository.save(brand);
        }
        return brand;
    }

    // Thêm phương thức để lấy mã thương hiệu mới
    public String generateNewBrandCode() {
        String maxBrandCode = brandRepository.findMaxBrandCode();

        if (maxBrandCode == null) {
            return "TH0001";
        } else {
            // Trích xuất số từ mã hiện tại
            int currentNumber = Integer.parseInt(maxBrandCode.substring(2));
            // Tạo mã mới với số tăng lên 1
            return String.format("TH%04d", currentNumber + 1);
        }
    }

    @Override
    public void deleteBrand(List<Integer> brandIds) {
        brandRepository.deleteAllById(brandIds);
    }

    @Override
    public List<Brand> findByBrandNameContaining(String keyword) {
        return brandRepository.findByBrandNameContainingIgnoreCase(keyword);
    }

    @Override
    public boolean existsByBrandName(String brandName) {
        return brandRepository.existsByBrandName(brandName);
    }

    @Override
    public Page<Brand> getAllBrandsPaginated(int page, int size) {
        return brandRepository.findAll(PageRequest.of(page, size));
    }

    @Override
    public Page<Brand> findByBrandNameContainingPaginated(String brandName, int page, int size) {
        return brandRepository.findByBrandNameContainingIgnoreCase(brandName, PageRequest.of(page, size));
    }

    @Override
    public Page<Brand> findByBrandCodeContainingPaginated(String brandCode, int page, int size) {
        return brandRepository.findByBrandCodeContainingIgnoreCase(brandCode, PageRequest.of(page, size));
    }
}