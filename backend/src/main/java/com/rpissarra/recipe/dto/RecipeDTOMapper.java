package com.rpissarra.recipe.dto;

import com.rpissarra.recipe.Recipe;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RecipeDTOMapper implements Function<Recipe, RecipeDTO> {

    @Override
    public RecipeDTO apply(Recipe recipe) {
        return new RecipeDTO(
                recipe.getName(),
                recipe.getIngredients()
                        .stream()
                        .map(i -> i.getName())
                        .collect(Collectors.toList()),
                recipe.getSteps()
                        .stream()
                        .map(r -> r.getDescription())
                        .collect(Collectors.toList()));
    }
}
