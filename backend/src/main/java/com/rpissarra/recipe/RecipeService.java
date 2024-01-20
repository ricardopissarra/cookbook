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
import java.util.Optional;
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
        Recipe recipe = new Recipe();
        Date createDate = new Date();
        recipe.setName(recipeRegistrationRequest.name().toLowerCase());
        recipe.setCreatedate(createDate);
        recipe.setUpdatedate(null);
        recipeRepository.save(recipe);

        List<Ingredients> ingredientsList = convertToIngredientsList(recipeRegistrationRequest.ingredients(), recipe);
        ingredientRepository.saveAll(ingredientsList);

        List<Steps> stepsList = convertToStepsList(recipeRegistrationRequest.steps(), recipe);
        stepsRepository.saveAll(stepsList);
    }
    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
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
        Recipe recipe = getRecipeById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Could not find any Recipe with id [%s]".formatted(id))
                );

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
            deleteAllIngredients(recipe.getIdrecipe());
            ingredientRepository.saveAll(convertToIngredientsList(recipeUpdateRequest.ingredients(), recipe));

            hasChanges = true;
        }

        List<String> listOriginalSteps = recipe.getSteps()
                        .stream()
                        .map(s -> s.getDescription())
                        .collect(Collectors.toList());

        if (recipeUpdateRequest.steps() != null && !recipeUpdateRequest.steps().isEmpty()
        && !recipeUpdateRequest.steps().equals(listOriginalSteps)) {

            deleteAllSteps(recipe.getIdrecipe());
            stepsRepository.saveAll(convertToStepsList(recipeUpdateRequest.steps(), recipe));

            hasChanges = true;
        }



        if (!hasChanges) {
            throw new RequestValidationException("No changes found in recipe with id [%s]".formatted(id));
        }
        recipe.setUpdatedate(new Date());
        recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        getRecipeById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Could not find any Recipe with id [%s]".formatted(id))
                );
        deleteAllIngredients(id);

        deleteAllSteps(id);
        recipeRepository.deleteById(id);
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
        List<Ingredients> ingredientsList = ingredientRepository.findAllIngredientsByRecipeId(id);

        if (!ingredientsList.isEmpty())
            ingredientRepository.deleteAll(ingredientsList);
    }

    private void deleteAllSteps(Long id) {
        List<Steps> stepsList = stepsRepository.findAllStepsByRecipeId(id);

        if (!stepsList.isEmpty())
            stepsRepository.deleteAll(stepsList);
    }

}
