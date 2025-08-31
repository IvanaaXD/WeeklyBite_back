package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RecipeRepository   extends JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    @Query("SELECT r From Recipe r WHERE r.isDeleted = false")
    Page<Recipe> findAllLast(Pageable pageable);

    Optional<Recipe> findByIdAndIsDeletedFalse(Long id);
}
