package com.rpissarra.recipe;

import com.rpissarra.ingredients.Ingredients;
import com.rpissarra.ingredients.IngredientsDAL;
import com.rpissarra.steps.Steps;
import com.rpissarra.steps.StepsDAL;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeDAL recipeDAL;
    private final IngredientsDAL ingredientsDAL;
    private final StepsDAL stepsDAL;

    public RecipeService(RecipeDAL recipeDAL, IngredientsDAL ingredientsDAL, StepsDAL stepsDAL) {
        this.recipeDAL = recipeDAL;
        this.ingredientsDAL = ingredientsDAL;
        this.stepsDAL = stepsDAL;
    }

    public void addRecipe(RecipeRegistrationRequest recipeRegistrationRequest) {
        Recipe recipe = new Recipe();
        Date createDate = new Date();
        recipe.setName(recipeRegistrationRequest.name());
        recipe.setCreatedate(createDate);
        recipe.setUpdatedate(null);
        recipeDAL.insertRecipe(recipe);

        List<Ingredients> ingredientsList = new ArrayList<Ingredients>();

        for (String i : recipeRegistrationRequest.ingredients()) {
            Ingredients ingredients = new Ingredients();
            ingredients.setName(i);
            ingredients.setCreatedate(createDate);
            ingredients.setRecipe(recipe);
            ingredientsList.add(ingredients);
        }
        ingredientsDAL.insertAllIngredients(ingredientsList);

        List<Steps> stepsList = new ArrayList<Steps>();

        for (String s : recipeRegistrationRequest.steps()) {
            Steps steps = new Steps();
            steps.setDescription(s);
            steps.setCreatedate(createDate);
            steps.setRecipe(recipe);
            stepsList.add(steps);
        }
        stepsDAL.insertAllSteps(stepsList);

    }
}
