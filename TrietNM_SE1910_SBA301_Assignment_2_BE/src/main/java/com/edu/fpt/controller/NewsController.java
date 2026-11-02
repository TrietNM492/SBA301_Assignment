package com.edu.fpt.controller;

import com.edu.fpt.dto.request.NewsRequest;
import com.edu.fpt.dto.response.NewsResponse;
import com.edu.fpt.service.NewsArticleService;
import com.edu.fpt.util.ApiResponse;
import com.edu.fpt.util.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsArticleService newsArticleService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<NewsResponse>>> getNews(
            @RequestParam(defaultValue = "") String key,
            Pageable pageable) {
        PageResponse<NewsResponse> news = newsArticleService.getAll(key, pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "News retrieved successfully", news));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NewsResponse>> createNews(
            @Valid @RequestBody NewsRequest request,
            Authentication authentication) {
        NewsResponse news = newsArticleService.createNews(request, currentUserId(authentication));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "News created successfully", news));
    }

    @PutMapping("/{newsId}")
    public ResponseEntity<ApiResponse<NewsResponse>> updateNews(
            @PathVariable Long newsId,
            @Valid @RequestBody NewsRequest request,
            Authentication authentication) {
        NewsResponse news = newsArticleService.updateNews(request, currentUserId(authentication), newsId);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "News updated successfully", news));
    }

    @DeleteMapping("/{newsId}")
    public ResponseEntity<ApiResponse<String>> deleteNews(
            @PathVariable Long newsId,
            Authentication authentication) {
        String message = newsArticleService.deleteNewsByOwn(newsId, currentUserId(authentication));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), message, message));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<PageResponse<NewsResponse>>> getNewsHistory(
            Authentication authentication,
            Pageable pageable) {
        PageResponse<NewsResponse> history = newsArticleService.viewHistory(currentUserId(authentication), pageable);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "News history retrieved successfully", history));
    }

    private Long currentUserId(Authentication authentication) {
        if (authentication == null || !(authentication.getPrincipal() instanceof Long userId)) {
            throw new AccessDeniedException("The current session does not identify a user.");
        }
        return userId;
    }
}
