package org.example.electricstore.mapper.category;

import org.example.electricstore.DTO.category.CategoryDTO;
import org.example.electricstore.model.Category;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryDTO dto) {
        if (dto == null) {
            return null;
        }

        Category category = new Category();
        category.setCategoryID(dto.getCategoryID());
        category.setCategoryCode(dto.getCategoryCode());
        category.setCategoryName(dto.getCategoryName());
        category.setDescription(dto.getDescription());

        // Đặt thời gian tạo và cập nhật nếu là danh mục mới
        if (dto.getCreateAt() != null) {
            category.setCreateAt(dto.getCreateAt());
        } else if (category.getCategoryID() == null) {
            LocalDateTime now = LocalDateTime.now();
            category.setCreateAt(now);
            category.setUpdateAt(now);
        }

        // Luôn cập nhật thời gian chỉnh sửa
        if (dto.getUpdateAt() != null) {
            category.setUpdateAt(dto.getUpdateAt());
        } else {
            category.setUpdateAt(LocalDateTime.now());
        }

        return category;
    }

    public CategoryDTO toDto(Category entity) {
        if (entity == null) {
            return null;
        }

        CategoryDTO dto = new CategoryDTO();
        dto.setCategoryID(entity.getCategoryID());
        dto.setCategoryCode(entity.getCategoryCode());
        dto.setCategoryName(entity.getCategoryName());
        dto.setDescription(entity.getDescription());
        dto.setCreateAt(entity.getCreateAt());
        dto.setUpdateAt(entity.getUpdateAt());
        return dto;
    }

    public void updateEntityFromDto(CategoryDTO dto, Category entity) {
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getCategoryCode() != null) {
            entity.setCategoryCode(dto.getCategoryCode());
        }
        if (dto.getCategoryName() != null) {
            entity.setCategoryName(dto.getCategoryName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }

        // Luôn cập nhật thời gian chỉnh sửa, nhưng giữ nguyên thời gian tạo
        entity.setUpdateAt(LocalDateTime.now());
    }
}