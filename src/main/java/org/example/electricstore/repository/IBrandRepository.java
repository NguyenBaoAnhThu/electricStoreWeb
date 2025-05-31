package org.example.electricstore.repository;

import org.example.electricstore.model.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, Integer> {
    List<Brand> findByBrandNameContainingIgnoreCase(String brandName);

    boolean existsByBrandName(String brandName);

    Page<Brand> findByBrandNameContainingIgnoreCase(String brandName, Pageable pageable);

    Page<Brand> findByBrandCodeContainingIgnoreCase(String brandCode, Pageable pageable);

    @Query("SELECT MAX(b.brandCode) FROM Brand b WHERE b.brandCode LIKE 'TH%'")
    String findMaxBrandCode();
}