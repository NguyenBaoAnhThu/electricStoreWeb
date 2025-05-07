package org.example.electricstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "product_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column
    private Double screenSize;

    @Column
    private Integer camera;
    private String description;

    @Column
    private Integer frontCamera;

    @Column(length = 255)
    private String color;

    @Column(length = 255)
    private String cpu;

    @Column(length = 255)
    private String gpu;

    @Column(length = 255)
    private String ram;

    @Column(length = 255)
    private String rom;

    @Column(length = 255)
    private String os;

    @Column(length = 255)
    private String osVersion;

    @Column(length = 255)
    private String battery;

    @Column(name = "screen_type", length = 255)
    private String screenType;

    @Column(name = "screen_resolution", length = 255)
    private String screenResolution;

    @Column(length = 255)
    private String ports;

    @Column
    private Integer weight;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}