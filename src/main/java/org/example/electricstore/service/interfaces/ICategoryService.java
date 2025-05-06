package org.example.electricstore.service.interfaces;

import org.example.electricstore.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<Category> getAllCategories();
    Page<Category> getAllCategoriesPaged(Pageable pageable);

    List<Category> findByCategoryNameContaining(String keyword);
    Page<Category> findByCategoryNameContainingPaged(String keyword, Pageable pageable);

    List<Category> findByCategoryCodeContaining(String keyword);
    Page<Category> findByCategoryCodeContainingPaged(String keyword, Pageable pageable);

    Optional<Category> getCategoryById(Integer categoryId);
    Category saveCategory(Category category);
    void deleteCategory(List<Integer> categoryIds);

    boolean existsByCategoryName(String categoryName);
    boolean existsByCategoryNameAndNotId(String categoryName, Integer id);

    boolean existsByCategoryCode(String categoryCode);
    boolean existsByCategoryCodeAndNotId(String categoryCode, Integer id);

    boolean hasRelatedProducts(List<Integer> ids);
    String generateNextCategoryCode();
}