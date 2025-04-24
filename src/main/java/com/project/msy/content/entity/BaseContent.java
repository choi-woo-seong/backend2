package com.project.msy.content.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@MappedSuperclass
public abstract class BaseContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    protected String title;

    @Column(columnDefinition = "TEXT")
    protected String content;

    protected LocalDateTime createdAt;
}