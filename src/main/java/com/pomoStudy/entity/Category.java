package com.pomoStudy.entity;

import jakarta.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String color;

    private String icon;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user_category;
}
