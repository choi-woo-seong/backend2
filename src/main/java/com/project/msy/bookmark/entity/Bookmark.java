package com.project.msy.bookmark.entity;

import com.project.msy.user.entity.User;
import com.project.msy.facility.entity.Facility;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    private LocalDateTime createdAt;
}