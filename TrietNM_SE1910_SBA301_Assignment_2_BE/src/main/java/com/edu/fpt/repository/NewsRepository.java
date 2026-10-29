package com.edu.fpt.repository;

import com.edu.fpt.domain.entity.NewsArticle;
import com.edu.fpt.domain.entity.SystemAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<NewsArticle, Long> {

    Page<NewsArticle> getNewsArticlesByNewsTitleContainingIgnoreCase(String newsTitle, Pageable pageable);

    @Query("select na from NewsArticle na left join fetch na.createBy sa where na.newsArticleID = :newId and sa.id = :userId")
    NewsArticle findNewsArticleByNewsArticleIDAndUserId(@Param("newsId") Long newsId, @Param("userId") Long userId);


    @Query("select na from NewsArticle na where na.createBy.id = :userId order by na.createdDate desc")
    Page<NewsArticle> getNewsArticlesByUserId(@Param("userId") Long userId, Pageable pageable);
}
