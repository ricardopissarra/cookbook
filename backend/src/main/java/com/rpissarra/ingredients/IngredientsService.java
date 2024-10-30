package com.rpissarra.ingredients;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientsService {

    private final IngredientRepository ingredientRepository;

    public IngredientsService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredients> findAllIngredientsByRecipeId(long id) {
        return ingredientRepository.findAllIngredientsByRecipeId(id);
    }

    public void saveAllIngredients(List<Ingredients > lstIngredients) {
        ingredientRepository.saveAll(lstIngredients);
    }

    public void deleteAll(List<Ingredients> ingredientsList) {
        ingredientRepository.deleteAll(ingredientsList);
    }
}
