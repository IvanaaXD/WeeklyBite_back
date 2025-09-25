package com.backend.weeklybite.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IngredientWithQuantityDTO {
    private String name;
    private String unit;
    private Double quantity;
}
