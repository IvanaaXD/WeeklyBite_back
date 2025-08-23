package com.backend.weeklybite.dto.recipe;

import com.backend.weeklybite.domain.Recipe;
import com.backend.weeklybite.domain.enums.RecipeCategory;
import com.backend.weeklybite.dto.ingredient.GetIngredientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetRecipeDTO {

    private Long id;

    private LocalDate created;
    private LocalDate updated;
    private String name;
    private String description;
    private Integer duration;
    private String content;
    private Integer numberOfPeople;
    private RecipeCategory category;

    private Boolean isDeleted;
    private Long adminId;

    private List<String> pictures;
    private List<GetIngredientDTO> products;

    public GetRecipeDTO(Recipe recipe) {
        this.id = recipe.getId();
        this.created = recipe.getCreated();
        this.updated = recipe.getUpdated();
        this.pictures = recipe.getPictures();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.duration = recipe.getDuration();
        this.content = recipe.getContent();

        this.adminId = recipe.getAdmin().getId();

        this.products = recipe.getProducts()
                .stream()
                .map(GetIngredientDTO::new)
                .collect(Collectors.toList());
    }
}
