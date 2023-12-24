package com.rpissarra.recipe;


import com.rpissarra.recipe.dto.RecipeDTO;
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

    @GetMapping("search/{word}")
    public List<RecipeDTO> getAllRecipesByNameOrIngredient(@PathVariable(required = true, name = "word") String word) {
        return recipeService.getAllRecipesByNameOrIngredient(word);
    }

    @GetMapping("search/ingredient/{ingredient}")
    public List<RecipeDTO> getAllRecipesWithIngredient(@PathVariable(required = true, name = "ingredient")  String ingredient) {
        return recipeService.getAllRecipesWithIngredient(ingredient);
    }

    @GetMapping("search/name/{name}")
    public List<RecipeDTO> getRecipesWithNameLike(@PathVariable(required = true, name = "name")  String name) {
        return recipeService.getRecipeByName(name);
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

        return ResponseEntity.ok()
                .build();
    }
}
