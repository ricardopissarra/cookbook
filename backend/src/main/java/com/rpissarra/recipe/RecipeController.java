package com.rpissarra.recipe;


import com.rpissarra.exception.ApiError;
import com.rpissarra.recipe.dto.RecipeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/recipe")
@Tag(name = "Recipe Rest Interface", description = "All operations regarding recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "Get all recipes",
            description = "Displays all recipes available at the time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded")}
    )
    @GetMapping(produces = "application/json")
    public List<RecipeDTO> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @Operation(summary = "Get recipe by Id",
            description = "Displays the recipe with the id passed as a param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded"),
            @ApiResponse(responseCode = "404", description = "there's no recipe with that id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))}
    )
    @GetMapping(value = "{id}", produces = "application/json")
    public RecipeDTO getRecipeById(@PathVariable(required = true, name = "id")  Long id) {
        return recipeService.findRecipeById(id);
    }

    @Operation(summary = "Get all recipes that contain the keyword in the name or ingredients",
            description = "Get all recipes that contain the keyword in the name or ingredients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded")}
    )
    @GetMapping(value = "search", produces = "application/json")
    public List<RecipeDTO> getAllRecipesByNameOrIngredient(@RequestParam String keyWord) {
        return recipeService.getAllRecipesByNameOrIngredient(keyWord);
    }

    @Operation(summary = "Get all recipes that contain some ingredient",
            description = "Get all recipes that contain the the ingredient request param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded")}
    )
    @GetMapping(value = "ingredients", produces = "application/json")
    public List<RecipeDTO> getAllRecipesWithIngredient(@RequestParam String ingredientName) {
        return recipeService.getAllRecipesWithIngredient(ingredientName);
    }

    @Operation(summary = "Get all recipes that contain some ingredient",
            description = "Get all recipes that contain the the ingredient request param")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded")}
    )
    @GetMapping(value = "name", produces = "application/json")
    public List<RecipeDTO> getRecipesWithNameLike(@RequestParam String recipeName) {
        return recipeService.getRecipeByName(recipeName);
    }

    @Operation(summary = "Edit a recipe",
            description = "Edit the recipe name, ingredients or steps of the recipe with the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded"),
            @ApiResponse(responseCode = "404", description = "there's no recipe with that id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "400", description = "there's no changes to the recipe",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))}
    )
    @PutMapping(value = "{id}", consumes = "application/json")
    public void updateRecipe(@PathVariable(required = true, name = "id") Long id,
                                          @RequestBody RecipeUpdateRequest recipeUpdateRequest) {
        recipeService.updateRecipe(id, recipeUpdateRequest);
    }

    @Operation(summary = "Delete a recipe",
            description = "Delete the recipe with the id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded"),
            @ApiResponse(responseCode = "404", description = "there's no recipe with that id",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class)))}
    )
    @DeleteMapping(value = "{id}")
    public void deleteRecipe(@PathVariable(required = true, name="id") Long id) {
        recipeService.deleteRecipe(id);
    }

    @Operation(summary = "Create a recipe",
            description = "Insert a new recipe to the list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "recipe created",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RecipeRegistrationRequest.class)))}
    )
    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> insertRecipe(
            @RequestBody RecipeRegistrationRequest recipeRegistrationRequest
    ) {
        recipeService.addRecipe(recipeRegistrationRequest);

        return new ResponseEntity<>(recipeRegistrationRequest, HttpStatus.CREATED);
    }
}
