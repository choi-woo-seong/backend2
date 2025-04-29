package com.project.msy.facility.entity;

import lombok.*;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * 시설 요금 정보 Entity
 */
@Entity
@Table(name = "facilities_price", indexes = {
        @Index(name = "idx_facilities_price_fid", columnList = "facility_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FacilityPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Column(name = "base_fee", nullable = false, precision = 10, scale = 2)
    private Double baseFee;

    @Column(name = "care_fee", nullable = false, precision = 10, scale = 2)
    private Double careFee;

    @Column(name = "meal_fee", nullable = false, precision = 10, scale = 2)
    private Double mealFee;

    @Column(name = "program_fee", nullable = false, precision = 10, scale = 2)
    private Double programFee;

    @Column(name = "insurance_rate", nullable = false, precision = 5, scale = 2)
    private Double insuranceRate;

    @Column(name = "total_fee", nullable = false, precision = 10, scale = 2)
    private Double totalFee;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
