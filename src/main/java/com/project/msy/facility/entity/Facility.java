package com.project.msy.facility.entity;

import com.project.msy.bookmark.entity.Bookmark;
import com.project.msy.review.entity.Review;
import jakarta.persistence.*;

import java.util.List;
import java.time.LocalDateTime;

@Entity
public class Facility {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private FacilityType type;

    private String address;
    private String region;
    private String phone;
    private String time;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "facility")
    private List<Service> services;

    @OneToMany(mappedBy = "facility")
    private List<Room> rooms;

    @OneToMany(mappedBy = "facility")
    private List<Review> reviews;

    @OneToMany(mappedBy = "facility")
    private List<Image> images;

    @OneToMany(mappedBy = "facility")
    private List<Bookmark> bookmarks;
}