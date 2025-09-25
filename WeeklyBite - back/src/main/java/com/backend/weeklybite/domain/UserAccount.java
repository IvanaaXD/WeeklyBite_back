package com.backend.weeklybite.domain;

import com.backend.weeklybite.domain.enums.AccountStatus;
import com.backend.weeklybite.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Transient
    private String jwt;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_account_favorite_recipes",
            joinColumns = @JoinColumn(name = "user_account_id"),
            inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private Collection<Recipe> favoriteRecipes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Week> weeks;

    @OneToOne
    @JoinColumn(name = "current_week_id")
    private Week currentWeek;

    @OneToOne
    @JoinColumn(name = "next_week_id")
    private Week nextWeek;
}
