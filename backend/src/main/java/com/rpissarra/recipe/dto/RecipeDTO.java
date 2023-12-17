package com.rpissarra.recipe.dto;


import java.util.List;

public record RecipeDTO(
        String name,
        List<String> ingredients,
        List<String> steps
) {
}
