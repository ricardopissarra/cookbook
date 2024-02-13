package com.rpissarra.recipe;

import com.rpissarra.exception.RequestValidationException;
import com.rpissarra.exception.ResourceNotFoundException;
import com.rpissarra.ingredients.IngredientRepository;
import com.rpissarra.ingredients.Ingredients;
import com.rpissarra.recipe.dto.RecipeDTO;
import com.rpissarra.recipe.dto.RecipeDTOMapper;
import com.rpissarra.steps.Steps;
import com.rpissarra.steps.StepsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final StepsRepository stepsRepository;

    private final RecipeDTOMapper recipeDTOMapper;

    public RecipeService(RecipeRepository recipeRepository,
                         IngredientRepository ingredientRepository,
                         StepsRepository stepsRepository,
                         RecipeDTOMapper recipeDTOMapper) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.stepsRepository = stepsRepository;
        this.recipeDTOMapper = recipeDTOMapper;
    }

    public List<RecipeDTO> getAllRecipes() {

        return recipeRepository.findAll()
                .stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    public List<RecipeDTO> getAllRecipesWithIngredient(String ingredient) {
        List<Recipe> lstRecipes = recipeRepository.getAllRecipesWithIngredient(ingredient.toLowerCase());

        return lstRecipes
                .stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    public void addRecipe(RecipeRegistrationRequest recipeRegistrationRequest) {
        if (recipeRegistrationRequest.ingredients().isEmpty() || recipeRegistrationRequest.steps().isEmpty()) {
            throw new RequestValidationException("The list of ingredients/steps can't be empty");
        }
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                recipeRegistrationRequest.name().toLowerCase(),
                createDate
        );
        recipeRepository.save(recipe);

        List<Ingredients> ingredientsList = convertToIngredientsList(recipeRegistrationRequest.ingredients(), recipe, createDate);
        ingredientRepository.saveAll(ingredientsList);

        stepsRepository.save(new Steps(
                recipeRegistrationRequest.steps(),
                createDate,
                recipe
        ));
    }

    public List<RecipeDTO> getAllRecipesByName(String name) {
        List<Recipe> lstRecipes = recipeRepository.findByNameContaining(name.toLowerCase());

        return lstRecipes.stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    public RecipeDTO findRecipeById(Long id) {
        return recipeRepository.findById(id)
                .map(recipeDTOMapper)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Could not find any Recipe with id [%s]".formatted(id))
                );
    }

    public List<RecipeDTO> getAllRecipesByNameOrIngredient(String nameOrIngredient) {
        List<Recipe> lstRecipe = recipeRepository.getAllRecipesByNameOrIngredient(nameOrIngredient.toLowerCase());
        return lstRecipe.stream()
                .map(recipeDTOMapper)
                .collect(Collectors.toList());
    }

    public void updateRecipe(Long id, RecipeUpdateRequest recipeUpdateRequest) {
        Recipe recipe = null;
        Date updateDate = new Date();
        if (recipeRepository.existsRecipeByIdrecipe(id)) {
            recipe = recipeRepository.findById(id).get();
        } else {
            throw new ResourceNotFoundException("Could not find any Recipe with id [%s]".formatted(id));
        }
        boolean hasChanges = false;

        if (recipeUpdateRequest.name() != null && !recipeUpdateRequest.name().equals(recipe.getName())) {
            recipe.setName(recipeUpdateRequest.name().toLowerCase());
            hasChanges = true;
        }

        // TODO: Maybe use Set Instead of List and try to really update instead of delete and insert
        List<String> listOriginalIngredients = recipe.getIngredients()
                                    .stream()
                                    .map(i -> i.getName())
                                    .collect(Collectors.toList());

        if (recipeUpdateRequest.ingredients() != null && !recipeUpdateRequest.ingredients().isEmpty()
        && !recipeUpdateRequest.ingredients().equals(listOriginalIngredients)) {
            deleteAllIngredientsByRecipeId(recipe.getIdrecipe());
            ingredientRepository.saveAll(convertToIngredientsList(recipeUpdateRequest.ingredients(),
                    recipe,
                    updateDate));

            hasChanges = true;
        }

        String originalStepDescription = recipe.getSteps().getDescription();

        if (recipeUpdateRequest.steps() != null && !recipeUpdateRequest.steps().isEmpty()
        && !recipeUpdateRequest.steps().equals(originalStepDescription)) {
            Steps updatedSteps = recipe.getSteps();
            updatedSteps.setDescription(recipeUpdateRequest.steps());
            stepsRepository.save(updatedSteps);
            hasChanges = true;
        }



        if (!hasChanges) {
            throw new RequestValidationException("No changes found in recipe with id [%s]".formatted(id));
        }
        recipe.setUpdatedate(updateDate);
        recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsRecipeByIdrecipe(id)) {
            throw new ResourceNotFoundException("Could not find any Recipe with id [%s]".formatted(id));
        }
        deleteAllIngredientsByRecipeId(id);
        deleteAllStepsByRecipeId(id);
        recipeRepository.deleteById(id);
    }

    private List<Ingredients> convertToIngredientsList(List<String> inputList, Recipe recipe, Date createDate) {
        List<Ingredients> ingredientsList = new ArrayList<>();

        for (String i : inputList) {
            Ingredients ingredients = new Ingredients(
                    i.toLowerCase(),
                    createDate,
                    recipe
            );
            ingredientsList.add(ingredients);
        }
        return ingredientsList;
    }

    private void deleteAllIngredientsByRecipeId(Long id) {
        List<Ingredients> ingredientsList = ingredientRepository.findAllIngredientsByRecipeId(id);

        if (!ingredientsList.isEmpty()) {
            ingredientRepository.deleteAll(ingredientsList);
        }
    }

    private void deleteAllStepsByRecipeId(Long id) {
        List<Steps> stepsList = stepsRepository.findAllStepsByRecipeId(id);

        if (!stepsList.isEmpty()) {
            stepsRepository.deleteAll(stepsList);
        }
    }

}
