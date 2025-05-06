package org.example.electricstore.repository;

import org.example.electricstore.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {
    List<Category> findByCategoryNameContainingIgnoreCase(String keyword);
    Page<Category> findByCategoryNameContainingIgnoreCase(String keyword, Pageable pageable);

    List<Category> findByCategoryCodeContainingIgnoreCase(String keyword);
    Page<Category> findByCategoryCodeContainingIgnoreCase(String keyword, Pageable pageable);

    boolean existsByCategoryNameIgnoreCase(String categoryName);
    boolean existsByCategoryNameIgnoreCaseAndCategoryIDNot(String categoryName, Integer id);

    boolean existsByCategoryCodeIgnoreCase(String categoryCode);
    boolean existsByCategoryCodeIgnoreCaseAndCategoryIDNot(String categoryCode, Integer id);

    @Query("SELECT MAX(c.categoryCode) FROM Category c")
    String findMaxCategoryCode();
}