package com.edu.fpt.service;

import com.edu.fpt.domain.entity.Category;
import com.edu.fpt.dto.request.CategoryRequest;
import com.edu.fpt.dto.response.CategoryResponse;
import com.edu.fpt.exception.ResourceNotFoundException;
import com.edu.fpt.repository.CategoryRepository;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public PageResponse<CategoryResponse> findAll(String key, Pageable pageable) {
        Page<Category> categories = categoryRepository.findByCategoryNameContainingIgnoreCase(key, pageable);
        Page<CategoryResponse> categoryResponses = categories.map(this::toResponse);
        return new PageResponse<>(categoryResponses);
    }

    @Transactional
    public Category createCategory(CategoryRequest request) {
        Category categorySaving = new Category();
        categorySaving.setCategoryName(request.categoryName());
        categorySaving.setCategoryDesciption(request.categoryDesciption());
        categorySaving.setParentCategoryID(request.parentCategoryID());
        categorySaving.setIsActive(true);
        return categoryRepository.save(categorySaving);
    }

    @Transactional
    public Category updateCategoty(Long id, CategoryRequest request) {
        Category categoryFinded = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find Category with id " + id));

        categoryFinded.setCategoryName(request.categoryName());
        categoryFinded.setCategoryDesciption(request.categoryDesciption());
        categoryFinded.setParentCategoryID(request.parentCategoryID());
        return categoryFinded;
    }

    @Transactional
    public String deleteCategory(Long id) {
        Category categoryFinded = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find Category with id " + id));

        categoryRepository.delete(categoryFinded);
        return String.format("Deleted category with id: " + id);
    }

    @Transactional
    public Category changeCategoryStatus(Long id) {
        Category categoryFinded = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find Category with id " + id));

        categoryFinded.setIsActive(!categoryFinded.getIsActive());
        return categoryFinded;
    }

    private CategoryResponse toResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getCategoryName(), category.getCategoryDesciption(), category.getParentCategoryID().getId(), category.getIsActive());
    }
}
