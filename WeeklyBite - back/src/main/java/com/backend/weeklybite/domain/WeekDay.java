package com.backend.weeklybite.domain;

import com.backend.weeklybite.domain.enums.Day;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "week_day")
public class WeekDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Day day;

    // Relationships

    @ManyToOne
    @JoinColumn(name = "week_id", nullable = false)
    private Week week;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "week_day_recipes",
            joinColumns = @JoinColumn(name = "week_day_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id")
    )
    private List<Recipe> recipes = new ArrayList<>();
}
