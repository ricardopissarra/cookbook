package com.rpissarra.recipe;

import com.rpissarra.exception.RequestValidationException;
import com.rpissarra.exception.ResourceNotFoundException;
import com.rpissarra.ingredients.Ingredients;
import com.rpissarra.ingredients.IngredientsDAL;
import com.rpissarra.recipe.dto.RecipeDTO;
import com.rpissarra.recipe.dto.RecipeDTOMapper;
import com.rpissarra.steps.Steps;
import com.rpissarra.steps.StepsDAL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class RecipeService {

    private final RecipeDAL recipeDAL;
    private final IngredientsDAL ingredientsDAL;
    private final StepsDAL stepsDAL;

    private final RecipeDTOMapper recipeDTOMapper;

    public RecipeService(RecipeDAL recipeDAL, IngredientsDAL ingredientsDAL, StepsDAL stepsDAL, RecipeDTOMapper recipeDTOMapper) {
        this.recipeDAL = recipeDAL;
        this.ingredientsDAL = ingredientsDAL;
        this.stepsDAL = stepsDAL;
        this.recipeDTOMapper = recipeDTOMapper;
    }

    public List<RecipeDTO> getAllRecipes() {

        return recipeDAL.getAllRecipes()
                .stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> getAllRecipesWithIngredient(String ingredient) {
        List<Recipe> lstRecipes = recipeDAL.getAllRecipesWithIngredient(ingredient.toLowerCase());

        return lstRecipes
                .stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    public void addRecipe(RecipeRegistrationRequest recipeRegistrationRequest) {
        if (recipeRegistrationRequest.ingredients().isEmpty() || recipeRegistrationRequest.steps().isEmpty()) {
            throw new RequestValidationException("The list of ingredients/steps can't be empty");
        }
        Recipe recipe = new Recipe();
        Date createDate = new Date();
        recipe.setName(recipeRegistrationRequest.name().toLowerCase());
        recipe.setCreatedate(createDate);
        recipe.setUpdatedate(null);
        recipeDAL.insertRecipe(recipe);

        List<Ingredients> ingredientsList = convertToIngredientsList(recipeRegistrationRequest.ingredients(), recipe);
        ingredientsDAL.insertAllIngredients(ingredientsList);

        List<Steps> stepsList = convertToStepsList(recipeRegistrationRequest.steps(), recipe);
        stepsDAL.insertAllSteps(stepsList);
    }


    public Optional<Recipe> getRecipeById(Long id) {
        return recipeDAL.getRecipeById(id);
    }

    public List<RecipeDTO> getRecipeByName (String name) {
        List<Recipe> lstRecipes = recipeDAL.findByNameLike(name.toLowerCase());

        return lstRecipes.stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    public RecipeDTO findRecipeById(Long id) {
        if (!recipeDAL.existsRecipeWithId(id)) {
            throw new ResourceNotFoundException("Could not find any Recipe with id [%s]".formatted(id));
        }

        return recipeDTOMapper.apply(recipeDAL.getRecipeById(id).get());
    }

    public List<RecipeDTO> getAllRecipesByNameOrIngredient(String word) {
        List<Recipe> lstRecipe = recipeDAL.getAllRecipesWithKeyword(word.toLowerCase());
        return lstRecipe.stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    public void updateRecipe(Long id, RecipeUpdateRequest recipeUpdateRequest) {
        Recipe recipe = getRecipeById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Recipe with id [%s] not found.".formatted(id))
                );

        boolean hasChanges = false;

        if (recipeUpdateRequest.name() != null && !recipeUpdateRequest.name().equals(recipe.getName())) {
            recipe.setName(recipeUpdateRequest.name().toLowerCase());
            hasChanges = true;
        }
        List<String> listOriginalIngredients = recipe.getIngredients()
                                    .stream()
                                    .map(i -> i.getName())
                                    .collect(Collectors.toList());

        if (recipeUpdateRequest.ingredients() != null && !recipeUpdateRequest.ingredients().isEmpty()
        && !recipeUpdateRequest.ingredients().equals(listOriginalIngredients)) {
            deleteAllIngredients(recipe.getIdrecipe());
            ingredientsDAL.insertAllIngredients(convertToIngredientsList(recipeUpdateRequest.ingredients(), recipe));

            hasChanges = true;
        }

        List<String> listOriginalSteps = recipe.getSteps()
                        .stream()
                        .map(s -> s.getDescription())
                        .collect(Collectors.toList());

        if (recipeUpdateRequest.steps() != null && !recipeUpdateRequest.steps().isEmpty()
        && !recipeUpdateRequest.steps().equals(listOriginalSteps)) {

            deleteAllSteps(recipe.getIdrecipe());
            stepsDAL.insertAllSteps(convertToStepsList(recipeUpdateRequest.steps(), recipe));

            hasChanges = true;
        }



        if (!hasChanges) {
            throw new RequestValidationException("No changes found in recipe with id [%s]".formatted(id));
        }
        recipe.setUpdatedate(new Date());
        recipeDAL.updateRecipe(recipe);
    }

    public void deleteRecipe(Long id) {
        getRecipeById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Recipe with id [%s] not found.".formatted(id))
                );
        List<Ingredients> ingredientsList = ingredientsDAL.findAllIngredientsByRecipeId(id);
        if (ingredientsList != null && !ingredientsList.isEmpty()) {
            ingredientsDAL.deleteAllIngredients(ingredientsList);
        }

        List<Steps> stepsList = stepsDAL.findAllStepsByRecipeId(id);
        if (stepsList != null && !stepsList.isEmpty()) {
            stepsDAL.deleteAllSteps(stepsList);
        }
        recipeDAL.deleteRecipeById(id);
    }

    private List<Ingredients> convertToIngredientsList(List<String> inputList, Recipe recipe) {
        List<Ingredients> ingredientsList = new ArrayList<>();

        for (String i : inputList) {
            Ingredients ingredients = new Ingredients();
            ingredients.setName(i.toLowerCase());
            ingredients.setCreatedate(new Date());
            ingredients.setRecipe(recipe);
            ingredientsList.add(ingredients);
        }
        return ingredientsList;
    }

    private List<Steps> convertToStepsList(List<String> inputList, Recipe recipe) {
        List<Steps> stepsList = new ArrayList<>();

        for (String s : inputList) {
            Steps steps = new Steps();
            steps.setDescription(s.toLowerCase());
            steps.setCreatedate(new Date());
            steps.setRecipe(recipe);
            stepsList.add(steps);
        }
        return stepsList;
    }

    private void deleteAllIngredients(Long id) {
        List<Ingredients> ingredientsList = ingredientsDAL.findAllIngredientsByRecipeId(id);

        if (ingredientsList == null) {
            throw new ResourceNotFoundException("Could not find any Ingredients for the recipe with id [%s]".formatted(id));
        }

        ingredientsDAL.deleteAllIngredients(ingredientsList);
    }

    private void deleteAllSteps(Long id) {
        List<Steps> stepsList = stepsDAL.findAllStepsByRecipeId(id);

        if (stepsList == null) {
            throw new ResourceNotFoundException("Could not find any Steps for the recipe with id [%s]".formatted(id));
        }

        stepsDAL.deleteAllSteps(stepsList);
    }

}
