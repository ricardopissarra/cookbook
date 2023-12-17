package com.rpissarra.recipe;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RecipeDAL implements RecipeDao {

    private final RecipeRepository recipeRepository;

    public RecipeDAL(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    @Override
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    @Override
    public void insertRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    @Override
    public boolean existsRecipeWithId(Long id) {
        return recipeRepository.existsRecipeByIdrecipe(id);
    }

    @Override
    public void updateRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }
}
