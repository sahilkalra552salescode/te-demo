package com.example.te_demo.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "city_tiers")
public class CityTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "city_name", nullable = false, unique = true)
    private String cityName;

    @Column(nullable = false)
    private Short tier;
}
