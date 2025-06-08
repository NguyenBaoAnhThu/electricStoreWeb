package org.example.electricstore.service.impl;

import org.example.electricstore.model.Category;
import org.example.electricstore.repository.ICategoryRepository;
import org.example.electricstore.service.interfaces.ICategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Page<Category> getAllCategoriesPaged(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public List<Category> findByCategoryNameContaining(String keyword) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase(keyword);
    }

    @Override
    public Page<Category> findByCategoryNameContainingPaged(String keyword, Pageable pageable) {
        return categoryRepository.findByCategoryNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public List<Category> findByCategoryCodeContaining(String keyword) {
        return categoryRepository.findByCategoryCodeContainingIgnoreCase(keyword);
    }

    @Override
    public Page<Category> findByCategoryCodeContainingPaged(String keyword, Pageable pageable) {
        return categoryRepository.findByCategoryCodeContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public Optional<Category> getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId);
    }

    @Override
    public Category saveCategory(Category category) {
        if (category.getCategoryID() != null && categoryRepository.existsById(category.getCategoryID())) {
            Category existingCategory = categoryRepository.findById(category.getCategoryID()).orElse(null);
            if (existingCategory != null) {
                existingCategory.setCategoryName(category.getCategoryName());
                existingCategory.setCategoryCode(category.getCategoryCode());
                existingCategory.setDescription(category.getDescription());
                existingCategory.setUpdateAt(LocalDateTime.now());
                return categoryRepository.save(existingCategory);
            }
        } else {
            category.setCategoryCode(generateNextCategoryCode());
            category.setCreateAt(LocalDateTime.now());
            category.setUpdateAt(LocalDateTime.now());
            return categoryRepository.save(category);
        }
        return category;
    }

    @Override
    public void deleteCategory(List<Integer> categoryIds) {
        categoryRepository.deleteAllById(categoryIds);
    }

    @Override
    public boolean existsByCategoryName(String categoryName) {
        return categoryRepository.existsByCategoryNameIgnoreCase(categoryName);
    }

    @Override
    public boolean existsByCategoryNameAndNotId(String categoryName, Integer id) {
        return categoryRepository.existsByCategoryNameIgnoreCaseAndCategoryIDNot(categoryName, id);
    }

    @Override
    public boolean existsByCategoryCode(String categoryCode) {
        return categoryRepository.existsByCategoryCodeIgnoreCase(categoryCode);
    }

    @Override
    public boolean existsByCategoryCodeAndNotId(String categoryCode, Integer id) {
        return categoryRepository.existsByCategoryCodeIgnoreCaseAndCategoryIDNot(categoryCode, id);
    }

    @Override
    public boolean hasRelatedProducts(List<Integer> ids) {
        for (Integer id : ids) {
            Optional<Category> category = categoryRepository.findById(id);
            if (category.isPresent() && !category.get().getProducts().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateNextCategoryCode() {
        String maxCategoryCode = categoryRepository.findMaxCategoryCode();

        if (maxCategoryCode == null) {
            return "DM0001";
        } else {
            int currentNumber = Integer.parseInt(maxCategoryCode.substring(2));
            return String.format("DM%04d", currentNumber + 1);
        }
    }
}