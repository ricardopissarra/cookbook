package com.rpissarra.recipe.dto;


import com.rpissarra.ingredients.dto.IngredientsDTO;
import com.rpissarra.steps.dto.StepsDTO;

import java.util.List;

public record RecipeDTO(
        Long id,
        String name,
        List<IngredientsDTO> ingredients,
        StepsDTO steps
) {
}
