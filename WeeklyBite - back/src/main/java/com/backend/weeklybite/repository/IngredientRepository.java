package com.backend.weeklybite.repository;

import com.backend.weeklybite.domain.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IngredientRepository extends JpaRepository<Ingredient, Long>, JpaSpecificationExecutor<Ingredient> {
    Ingredient findByNameAndQuantityAndUnit(String name, Double quantity, String unit);
}
