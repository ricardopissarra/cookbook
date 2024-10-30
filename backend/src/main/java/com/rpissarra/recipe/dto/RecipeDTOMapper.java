package com.rpissarra.recipe.dto;

import com.rpissarra.ingredients.dto.IngredientsDTOMapper;
import com.rpissarra.recipe.Recipe;
import com.rpissarra.steps.dto.StepsDTOMapper;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class RecipeDTOMapper implements Function<Recipe, RecipeDTO> {
    private final IngredientsDTOMapper ingredientsDTOMapper;
    private final StepsDTOMapper stepsDTOMapper;

    public RecipeDTOMapper(IngredientsDTOMapper ingredientsDTOMapper, StepsDTOMapper stepsDTOMapper) {
        this.ingredientsDTOMapper = ingredientsDTOMapper;
        this.stepsDTOMapper = stepsDTOMapper;
    }

    @Override
    public RecipeDTO apply(Recipe recipe) {
        return new RecipeDTO(
                recipe.getIdrecipe(),
                recipe.getName(),
                recipe.getIngredients()
                        .stream()
                        .map(ingredientsDTOMapper)
                        .toList(),
                stepsDTOMapper.apply(recipe.getSteps()));
    }
}
