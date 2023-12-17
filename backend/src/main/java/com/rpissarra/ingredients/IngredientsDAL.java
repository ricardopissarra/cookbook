package com.rpissarra.ingredients;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class IngredientsDAL {

    private final IngredientRepository ingredientRepository;

    public IngredientsDAL(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public void insertIngredient(Ingredients ingredients) {
        ingredientRepository.save(ingredients);
    }

    public void insertAllIngredients(List<Ingredients> ingredientsList) {
        ingredientRepository.saveAll(ingredientsList);
    }
}
