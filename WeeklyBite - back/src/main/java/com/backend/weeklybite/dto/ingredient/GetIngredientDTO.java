package com.backend.weeklybite.dto.ingredient;

import com.backend.weeklybite.domain.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetIngredientDTO {

    private Long id;
    private String name;
    private Double quantity;
    private String unit;

    public GetIngredientDTO(Ingredient product) {
        this.id = product.getId();
        this.name = product.getName();
        this.quantity = product.getQuantity();
        this.unit = unit;
    }
}
