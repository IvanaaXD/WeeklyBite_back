package com.backend.weeklybite.dto.week_day;

import com.backend.weeklybite.domain.enums.Day;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetWeekDayDTO {

    private Long id;
    private Day day;
    private Long weekId;
    private List<GetRecipeDTO> recipes;
}
