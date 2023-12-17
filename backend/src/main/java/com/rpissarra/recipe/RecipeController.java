package com.rpissarra.recipe;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/recipe")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
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
