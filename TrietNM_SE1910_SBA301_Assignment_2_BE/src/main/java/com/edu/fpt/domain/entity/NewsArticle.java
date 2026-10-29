package com.edu.fpt.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "NewsArticle")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewsArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Size(max = 20)
    @Column(name = "NewsArticleID", nullable = false, length = 20)
    private Long newsArticleID;

    @Size(max = 400)
    @Nationalized
    @Column(name = "NewsTitle", length = 400)
    private String newsTitle;

    @Size(max = 150)
    @NotNull
    @Nationalized
    @Column(name = "Headline", nullable = false, length = 150)
    private String headline;

    @Column(name = "CreatedDate")
    private Instant createdDate;

    @Size(max = 4000)
    @Nationalized
    @Column(name = "NewsContent", length = 4000)
    private String newsContent;

    @Size(max = 400)
    @Nationalized
    @Column(name = "NewsSource", length = 400)
    private String newsSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "CategoryID")
    private Category categoryID;

    @Column(name = "NewsStatus")
    private Boolean newsStatus;

    @ManyToOne
    @JoinColumn(name = "CreatedByID")
    private SystemAccount createBy;

    @Column(name = "UpdatedByID")
    private Long updatedByID;

    @Column(name = "ModifiedDate")
    private Instant modifiedDate;


}