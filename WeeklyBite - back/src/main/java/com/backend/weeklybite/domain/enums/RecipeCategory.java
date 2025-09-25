package com.backend.weeklybite.domain.enums;

public enum RecipeCategory {
    BREAKFAST(1),
    LUNCH(2),
    DINNER(3),
    SNACK(4),
    DESSERT(5);

    private final int value;

    RecipeCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static RecipeCategory fromValue(int value) {
        for (RecipeCategory recipeCategory : RecipeCategory.values()) {
            if (recipeCategory.value == value) {
                return recipeCategory;
            }
        }
        throw new IllegalArgumentException("Invalid recipeCategory number: " + value);
    }
}
