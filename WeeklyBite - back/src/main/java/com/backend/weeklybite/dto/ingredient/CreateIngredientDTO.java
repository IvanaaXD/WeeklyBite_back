package com.backend.weeklybite.dto.ingredient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateIngredientDTO {
    private String name;
    private Integer quantity;
    private String unit;
}
