package com.backend.weeklybite.dto.week_day;

import com.backend.weeklybite.domain.enums.RecipeCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateWeekDayDTO {

    private Long id;
    private RecipeCategory recipeCategory;
    private Long recipeId;
}
