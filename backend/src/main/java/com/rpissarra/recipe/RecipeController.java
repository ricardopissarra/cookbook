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

    @GetMapping("{ingredient}")
    public List<RecipeDTO> getAllRecipesWithIngredient(@PathVariable(required = true, name = "ingredient")  String ingredient) {
        return recipeService.getAllRecipesWithIngredient(ingredient);
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
