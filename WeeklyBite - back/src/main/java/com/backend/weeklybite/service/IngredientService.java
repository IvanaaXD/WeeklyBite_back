package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.Ingredient;
import com.backend.weeklybite.dto.ingredient.CreateIngredientDTO;
import com.backend.weeklybite.dto.ingredient.GetIngredientDTO;
import com.backend.weeklybite.dto.ingredient.IngredientWithQuantityDTO;
import com.backend.weeklybite.dto.recipe.GetRecipeDTO;
import com.backend.weeklybite.dto.week.GetWeekDTO;
import com.backend.weeklybite.dto.week_day.GetWeekDayDTO;
import com.backend.weeklybite.repository.IngredientRepository;
import com.backend.weeklybite.service.interfaces.IIngredientService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IngredientService implements IIngredientService {

    @Autowired
    private IngredientRepository allIngredients;

    @Autowired
    private WeekService weekService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthService authService;

    public GetIngredientDTO create(@Valid CreateIngredientDTO ingredientDTO) {

        Ingredient existingIngredient = allIngredients.findByNameAndQuantityAndUnit(ingredientDTO.getName(),
                ingredientDTO.getQuantity(), ingredientDTO.getUnit());

        if (existingIngredient != null) {
            return modelMapper.map(existingIngredient, GetIngredientDTO.class);
        }

        Ingredient ingredient = new Ingredient();

        ingredient.setName(ingredientDTO.getName());
        ingredient.setUnit(ingredientDTO.getUnit());
        ingredient.setQuantity(ingredientDTO.getQuantity());

        Ingredient createdIngredient = allIngredients.save(ingredient);

        return modelMapper.map(createdIngredient, GetIngredientDTO.class);
    }

    @Override
    public Collection<IngredientWithQuantityDTO> getIngredientsByWeekId(Long weekId) {

        GetWeekDTO weekDTO = weekService.getWeekById(weekId);
        List<GetRecipeDTO> recipesDTO = new ArrayList<>();

        Set<String> ingredientNames = new HashSet<>();
        Set<String> ingredientUnits = new HashSet<>();

        for (GetWeekDayDTO weekDay : weekDTO.getWeekDays()) {
            if (weekDay.getRecipes() != null) {
                recipesDTO.addAll(weekDay.getRecipes());
            }
        }

        Map<GetIngredientDTO, Double> ingredients = new HashMap<>();

        for (GetRecipeDTO recipe : recipesDTO) {
            if (recipe.getProducts() != null) {
                for (GetIngredientDTO ingredientDTO : recipe.getProducts()) {
                    String name = ingredientDTO.getName();
                    String unit = ingredientDTO.getUnit();

                    if (ingredientNames.contains(name) && ingredientUnits.contains(unit)) {

                        for (Map.Entry<GetIngredientDTO, Double> entry : ingredients.entrySet()) {
                            GetIngredientDTO existing = entry.getKey();
                            if (existing.getName().equals(name) && existing.getUnit().equals(unit)) {
                                ingredients.put(existing, entry.getValue() + ingredientDTO.getQuantity());
                                break;
                            }
                        }
                    } else {
                        ingredients.put(ingredientDTO, ingredientDTO.getQuantity());
                        ingredientNames.add(name);
                        ingredientUnits.add(unit);
                    }
                }
            }
        }

        return ingredients.entrySet().stream()
                .map(e -> {
                    IngredientWithQuantityDTO dto = new IngredientWithQuantityDTO();
                    dto.setName(e.getKey().getName());
                    dto.setUnit(e.getKey().getUnit());
                    dto.setQuantity(e.getValue());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
