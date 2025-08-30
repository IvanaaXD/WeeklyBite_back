package com.backend.weeklybite.dto.recipe;

import com.backend.weeklybite.domain.Step;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RecipeFilterDTO {

    private String name;
    private List<Step> description;
    private String searchQuery;
    private Integer duration;
    private String content;
    private Integer numberOfPeople;
    private String category;

    private Boolean breakfast;
    private Boolean lunch;
    private Boolean dinner;
    private Boolean dessert;
    private Boolean snack;
}
