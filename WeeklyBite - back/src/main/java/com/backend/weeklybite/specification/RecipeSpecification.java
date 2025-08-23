package com.backend.weeklybite.specification;

import com.backend.weeklybite.domain.Recipe;
import com.backend.weeklybite.domain.enums.RecipeCategory;
import com.backend.weeklybite.dto.recipe.RecipeFilterDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RecipeSpecification implements Specification<Recipe> {

    private final RecipeFilterDTO dto;

    public RecipeSpecification(RecipeFilterDTO dto) {
        this.dto = dto;
    }

    @Override
    public Predicate toPredicate(Root<Recipe> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        if (dto.getName() != null && !dto.getName().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + dto.getName().toLowerCase() + "%"));
        }

        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("description")), "%" + dto.getDescription().toLowerCase() + "%"));
        }

        if (dto.getContent() != null && !dto.getContent().isBlank()) {
            predicates.add(cb.like(cb.lower(root.get("content")), "%" + dto.getContent().toLowerCase() + "%"));
        }

        if (dto.getSearchQuery() != null && !dto.getSearchQuery().isBlank()) {
            String search = "%" + dto.getSearchQuery().toLowerCase() + "%";
            predicates.add(cb.or(
                    cb.like(cb.lower(root.get("name")), search),
                    cb.like(cb.lower(root.get("description")), search)
            ));
        }

        if (dto.getNumberOfPeople() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("numberOfPeople"), dto.getNumberOfPeople()));
        }

        if (dto.getDuration() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("duration"), dto.getDuration()));
        }

        // Exclude deleted services
        predicates.add(cb.isFalse(root.get("isDeleted")));

        if (Boolean.TRUE.equals(dto.getBreakfast()) ||
                Boolean.TRUE.equals(dto.getLunch()) ||
                Boolean.TRUE.equals(dto.getDinner()) ||
                Boolean.TRUE.equals(dto.getDessert()) ||
                Boolean.TRUE.equals(dto.getSnack())) {

            List<Predicate> categoryPredicates = new ArrayList<>();

            if (Boolean.TRUE.equals(dto.getBreakfast())) {
                categoryPredicates.add(cb.equal(root.get("category"), RecipeCategory.BREAKFAST));
            }
            if (Boolean.TRUE.equals(dto.getLunch())) {
                categoryPredicates.add(cb.equal(root.get("category"), RecipeCategory.LUNCH));
            }
            if (Boolean.TRUE.equals(dto.getDinner())) {
                categoryPredicates.add(cb.equal(root.get("category"), RecipeCategory.DINNER));
            }
            if (Boolean.TRUE.equals(dto.getDessert())) {
                categoryPredicates.add(cb.equal(root.get("category"), RecipeCategory.DESSERT));
            }
            if (Boolean.TRUE.equals(dto.getSnack())) {
                categoryPredicates.add(cb.equal(root.get("category"), RecipeCategory.SNACK));
            }

            predicates.add(cb.or(categoryPredicates.toArray(new Predicate[0])));
        }

        return cb.and(predicates.toArray(new Predicate[0]));
    }
}
