package com.rpissarra.ingredients.dto;

import com.rpissarra.ingredients.Ingredients;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class IngredientsDTOMapper implements Function<Ingredients, IngredientsDTO> {
    @Override
    public IngredientsDTO apply(Ingredients ingredients) {
        return new IngredientsDTO(
                ingredients.getIdingredient(),
                ingredients.getName()
        );
    }
}
