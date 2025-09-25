package com.backend.weeklybite.dto.recipe;

import com.backend.weeklybite.domain.Step;
import com.backend.weeklybite.domain.enums.RecipeCategory;
import com.backend.weeklybite.dto.ingredient.GetIngredientDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdatedRecipeDTO {

    private Long id;

    private String name;
    private List<Step> description;
    private Integer duration;
    private String content;
    private Integer numberOfPeople;
    private RecipeCategory category;

    private List<String> pictures;
    private List<GetIngredientDTO> products;
}
