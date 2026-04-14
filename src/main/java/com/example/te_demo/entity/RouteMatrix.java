package com.example.te_demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "route_matrix",
        uniqueConstraints = @UniqueConstraint(columnNames = {"route_code", "grade_id"}))
public class RouteMatrix {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "route_code", nullable = false, length = 20)
    private String routeCode;

    @Column(name = "from_town", nullable = false, length = 100)
    private String fromTown;

    @Column(name = "to_town", nullable = false, length = 100)
    private String toTown;

    @Column(name = "distance_km", nullable = false, precision = 8, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "grade_id", nullable = false)
    private Long gradeId;

    @Column(name = "ta_per_km", nullable = false, precision = 8, scale = 4)
    private BigDecimal taPerKm;

    @Column(name = "da_per_day", nullable = false, precision = 10, scale = 2)
    private BigDecimal daPerDay;

    @Column(name = "nh_per_night", nullable = false, precision = 10, scale = 2)
    private BigDecimal nhPerNight = BigDecimal.ZERO;
}
