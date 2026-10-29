package com.edu.fpt.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@Embeddable
public class NewsTagId implements Serializable {
    private static final long serialVersionUID = 3666435848835300504L;
    @Size(max = 20)
    @NotNull
    @Column(name = "NewsArticleID", nullable = false, length = 20)
    private Long newsArticleID;

    @NotNull
    @Column(name = "TagID", nullable = false)
    private Integer tagID;


}