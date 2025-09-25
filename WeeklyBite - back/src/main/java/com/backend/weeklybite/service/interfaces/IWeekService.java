package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import com.backend.weeklybite.dto.week.GetWeekDTO;

import java.util.Collection;

public interface IWeekService {
    GetWeekDTO getWeekById(Long id);

    GetWeekDTO getWeekByUserId(Long id);


    GetWeekDTO createCurrentWeek(String userEmail);

    GetWeekDTO create();

    void checkWeeks();

    GetWeekDTO getCurrentWeekByUserId(Long id);

    GetWeekDTO getNextWeekByUserId(Long id);

    Collection<GetRecipeDTO> getCurrentWeekRecipes(Long id);

    Collection<GetWeekDTO> getPastWeeksByUserId(Long id);
}
