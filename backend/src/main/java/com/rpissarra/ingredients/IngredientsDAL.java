package com.rpissarra.ingredients;

import com.rpissarra.exception.ResourceNotFoundException;
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

    public List<Ingredients> findAllIngredientsByRecipeId(Long id) {
        List<Ingredients> lstIngredients = ingredientRepository.findAllIngredientsByRecipeId(id);

        if (lstIngredients.isEmpty()) {
            return null;
        }

        return lstIngredients;
    }

    public void deleteAllIngredients(List<Ingredients> lstIngredients) {
        ingredientRepository.deleteAll(lstIngredients);
    }
}
