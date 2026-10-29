package com.edu.fpt.domain.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "NewsTag")
@NoArgsConstructor
@AllArgsConstructor
public class NewsTag {
    @EmbeddedId
    private NewsTagId id;

    //TODO [Reverse Engineering] generate columns from DB
}