package com.backend.weeklybite.domain;

import com.backend.weeklybite.domain.enums.RecipeCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate created;
    private LocalDate updated;
    private String name;
    private String content;
    private String description;
    private Integer duration;
    private Integer numberOfPeople;

    @Enumerated(EnumType.STRING)
    private RecipeCategory category;

    private Boolean isDeleted;

    // Relationships

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private UserAccount admin;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private Collection<Ingredient> products;

    @ElementCollection
    @CollectionTable(name = "recipe_pictures", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "picture_path")
    private List<String> pictures;
}
