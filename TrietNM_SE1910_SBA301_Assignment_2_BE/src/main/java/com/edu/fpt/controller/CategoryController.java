package com.edu.fpt.controller;

import com.edu.fpt.domain.entity.Category;
import com.edu.fpt.dto.request.CategoryRequest;
import com.edu.fpt.dto.response.CategoryResponse;
import com.edu.fpt.service.CategoryService;
import com.edu.fpt.util.ApiResponse;
import com.edu.fpt.util.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<CategoryResponse>>> getCategories(
            @RequestParam(defaultValue = "") String key,
            Pageable pageable) {
        PageResponse<CategoryResponse> categories = categoryService.findAll(key, pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Categories retrieved successfully", categories));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Category>> createCategory(@Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Category created successfully", category));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Category>> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {
        Category category = categoryService.updateCategoty(id, request);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Category updated successfully", category));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        String message = categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), message, message));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Category>> changeCategoryStatus(@PathVariable Long id) {
        Category category = categoryService.changeCategoryStatus(id);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Category status changed successfully", category));
    }
}
