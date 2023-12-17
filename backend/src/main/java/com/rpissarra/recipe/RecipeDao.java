package com.rpissarra.recipe;

import java.util.List;
import java.util.Optional;

public interface RecipeDao {
    List<Recipe> getAllRecipes();
    Optional<Recipe> getRecipeById(Long id);
    void insertRecipe(Recipe recipe);
    boolean existsRecipeWithId(Long id);
    void updateRecipe(Recipe recipe);
}
