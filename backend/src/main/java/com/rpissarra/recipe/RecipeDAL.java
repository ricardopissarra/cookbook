package com.rpissarra.recipe;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecipeDAL {

    private final RecipeRepository recipeRepository;

    public RecipeDAL(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }


    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }


    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }


    public void insertRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }


    public boolean existsRecipeWithId(Long id) {
        return recipeRepository.existsRecipeByIdrecipe(id);
    }


    public void updateRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

   public List<Recipe> getAllRecipesWithIngredient(String ingredient) {
        return recipeRepository.getAllRecipesWithIngredient(ingredient);
    }

    public void deleteRecipeById(Long id) {
        recipeRepository.deleteById(id);
    }

    public List<Recipe> findByNameLike (String name) {
        return recipeRepository.findByNameContaining(name);
    }

    public List<Recipe> getAllRecipesWithKeyword(String nameOrIngredient) {
       return recipeRepository.getAllRecipesWithKeyword(nameOrIngredient);
    }
}
