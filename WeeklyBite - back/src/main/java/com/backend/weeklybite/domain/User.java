package com.backend.weeklybite.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "authenticated_user")
@PrimaryKeyJoinColumn(name = "id")
public class User extends Person {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    private Collection<Recipe> recipes;
}
