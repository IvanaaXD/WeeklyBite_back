package com.backend.weeklybite.service.interfaces;

import com.backend.weeklybite.domain.UserAccount;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;

import java.util.List;

public interface IRecipeService {

    List<GetRecipeDTO> getTopFiveRecipes(UserAccount user);
}
