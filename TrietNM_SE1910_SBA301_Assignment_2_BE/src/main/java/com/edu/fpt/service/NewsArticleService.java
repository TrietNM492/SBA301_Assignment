package com.edu.fpt.service;

import com.edu.fpt.domain.entity.NewsArticle;
import com.edu.fpt.domain.entity.SystemAccount;
import com.edu.fpt.dto.request.NewsRequest;
import com.edu.fpt.dto.response.NewsResponse;
import com.edu.fpt.exception.ResourceNotFoundException;
import com.edu.fpt.repository.NewsRepository;
import com.edu.fpt.repository.SystemAccountRepository;
import com.edu.fpt.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class NewsArticleService {
    private final NewsRepository newsRepository;
    private final SystemAccountRepository systemAccountRepository;

    public PageResponse<NewsResponse> getAll(String key, Pageable pageable) {
        Page<NewsArticle> newsArticles = newsRepository
                .getNewsArticlesByNewsTitleContainingIgnoreCase(key, pageable);
        Page<NewsResponse> newsResponses = newsArticles.map(this::toResponse);
        return new PageResponse<>(newsResponses);
    }

    @Transactional
    public NewsResponse createNews(NewsRequest request, Long userId) {
        NewsArticle newsArticle = generateNews(request, userId);
        return toResponse(newsArticle);
    }

    @Transactional
    public NewsResponse updateNews(NewsRequest request, Long userUpdateId, Long newsId) {
        NewsArticle articleFounded = newsRepository.findById(newsId)
                .orElseThrow(() -> new ResourceNotFoundException("News Not Found!"));
        articleFounded.setNewsTitle(request.newsTitle());
        articleFounded.setHeadline(request.headline());
        articleFounded.setNewsContent(request.newsContent());
        articleFounded.setNewsSource(request.newsSource());
        articleFounded.setCategoryID(request.categoryID());
        articleFounded.setNewsStatus(true);
        articleFounded.setUpdatedByID(userUpdateId);
        articleFounded.setModifiedDate(Instant.now());
        return toResponse(articleFounded);
    }

    @Transactional
    public String deleteNewsByOwn(Long newsId, Long userId) {
        NewsArticle articleFounded = newsRepository.findNewsArticleByNewsArticleIDAndUserId(newsId, userId);
        if (articleFounded != null) {
            newsRepository.delete(articleFounded);
        } else {
            throw new RuntimeException("Can't delete");
        }
        return String.format("Deleted News with id: %d and userId: %d", newsId, userId);
    }


    public PageResponse<NewsResponse> viewHistory(Long userId, Pageable pageable) {
        Page<NewsArticle> newsArticles = newsRepository.getNewsArticlesByUserId(userId, pageable);
        Page<NewsResponse> newsResponses = newsArticles.map(this::toResponse);
        return new PageResponse<>(newsResponses);
    }


    private NewsArticle generateNews(NewsRequest request, Long userId) {
        NewsArticle newsArticle = new NewsArticle();
        newsArticle.setNewsTitle(request.newsTitle());
        newsArticle.setHeadline(request.headline());
        newsArticle.setCreatedDate(Instant.now());
        newsArticle.setNewsContent(request.newsContent());
        newsArticle.setNewsSource(request.newsSource());
        newsArticle.setCategoryID(request.categoryID());
        newsArticle.setNewsStatus(true);
        SystemAccount user = systemAccountRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found!"));
        newsArticle.setCreateBy(user);
        newsArticle.setUpdatedByID(null);
        newsArticle.setModifiedDate(Instant.now());
        return newsArticle;
    }


    private NewsResponse toResponse(NewsArticle newsArticle) {
        return new NewsResponse(
                newsArticle.getNewsArticleID(),
                newsArticle.getNewsTitle(),
                newsArticle.getHeadline(),
                newsArticle.getCreatedDate(),
                newsArticle.getNewsContent(),
                newsArticle.getNewsSource(),
                newsArticle.getCategoryID(),
                newsArticle.getNewsStatus(),
                newsArticle.getUpdatedByID(),
                newsArticle.getModifiedDate()
        );
    }
}
