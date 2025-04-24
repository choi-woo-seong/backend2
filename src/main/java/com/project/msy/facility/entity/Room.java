package com.project.msy.facility.entity;

import com.project.msy.facility.entity.Facility;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomType;
    private String roomOptions;
    private int squareMeters;
    private int monthlyFee;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "facility_id")
    private Facility facility;
}
