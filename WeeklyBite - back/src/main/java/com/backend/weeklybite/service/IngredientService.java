package com.backend.weeklybite.service;

import com.backend.weeklybite.domain.Ingredient;
import com.backend.weeklybite.dto.ingredient.CreateIngredientDTO;
import com.backend.weeklybite.dto.ingredient.GetIngredientDTO;
import com.backend.weeklybite.repository.IngredientRepository;
import com.backend.weeklybite.service.interfaces.IIngredientService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IngredientService implements IIngredientService {

    @Autowired
    private IngredientRepository allIngredients;

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
}
