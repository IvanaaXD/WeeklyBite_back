package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.dto.ingredient.IngredientWithQuantityDTO;

import java.util.Collection;

public interface IIngredientService {
    Collection<IngredientWithQuantityDTO> getIngredientsByWeekId(Long weekId);
}
