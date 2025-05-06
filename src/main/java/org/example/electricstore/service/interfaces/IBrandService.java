package org.example.electricstore.service.interfaces;

import org.example.electricstore.model.Brand;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IBrandService {
    List<Brand> getAllBrands();

    Optional<Brand> getBrandById(Integer brandID);

    Brand saveBrand(Brand brand);

    void deleteBrand(List<Integer> brandIds);

    List<Brand> findByBrandNameContaining(String keyword);

    boolean existsByBrandName(String brandName);

    Page<Brand> getAllBrandsPaginated(int page, int size);

    Page<Brand> findByBrandNameContainingPaginated(String brandName, int page, int size);

    String generateNewBrandCode();
}