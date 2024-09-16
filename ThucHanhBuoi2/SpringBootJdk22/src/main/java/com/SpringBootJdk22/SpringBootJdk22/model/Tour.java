package com.SpringBootJdk22.SpringBootJdk22.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.format.annotation.NumberFormat;

import java.util.List;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Name is required")
    private String name;
    @NumberFormat(pattern = "#,###")
    private double price;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private String  avatar;
    @ManyToOne
    @JoinColumn(name = "itemCategory_id")
    private ItemCategory itemCategory;

    @OneToMany(mappedBy = "tour", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;
}