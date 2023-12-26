package com.rpissarra.recipe;


import com.rpissarra.recipe.dto.RecipeDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDTO> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("{id}")
    public RecipeDTO getRecipeById(@PathVariable(required = true, name = "id")  Long id) {
        return recipeService.findRecipeById(id);
    }

    @GetMapping("search")
    public List<RecipeDTO> getAllRecipesByNameOrIngredient(@RequestParam String keyword) {
        return recipeService.getAllRecipesByNameOrIngredient(keyword);
    }

    @GetMapping("search/ingredients")
    public List<RecipeDTO> getAllRecipesWithIngredient(@RequestParam String ingredientName) {
        return recipeService.getAllRecipesWithIngredient(ingredientName);
    }

    @GetMapping("search/name")
    public List<RecipeDTO> getRecipesWithNameLike(@RequestParam String recipeName) {
        return recipeService.getRecipeByName(recipeName);
    }

    @PutMapping("{id}")
    public void updateRecipe(@PathVariable(required = true, name = "id") Long id,
                                          @RequestBody RecipeUpdateRequest recipeUpdateRequest) {
        recipeService.updateRecipe(id, recipeUpdateRequest);
    }

    @DeleteMapping("{id}")
    public void deleteRecipe(@PathVariable(required = true, name="id") Long id) {
        recipeService.deleteRecipe(id);
    }

    @PostMapping
    public ResponseEntity<?> insertRecipe(
            @RequestBody RecipeRegistrationRequest recipeRegistrationRequest
    ) {
        recipeService.addRecipe(recipeRegistrationRequest);

        return new ResponseEntity<>(recipeRegistrationRequest, HttpStatus.CREATED);
    }
}
