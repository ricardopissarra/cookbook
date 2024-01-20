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
            description = "Returns all the recipes in the DB")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded")}
    )
    @GetMapping(produces = "application/json")
    public List<RecipeDTO> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @Operation(summary = "Get recipe by Id",
            description = "Displays the recipe with the given id. If there's no recipe with that id " +
                    "a 404 response is returned")
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

    @Operation(summary = "Get all recipes that contain the keyword",
            description = "Get all the recipes that contain given keyword, the keyword can be present in the name of" +
                    " the recipe or in the list of ingredients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded")}
    )
    @GetMapping(value = "search", produces = "application/json")
    public List<RecipeDTO> getAllRecipesByNameOrIngredient(@RequestParam String keyWord) {
        return recipeService.getAllRecipesByNameOrIngredient(keyWord);
    }

    @Operation(summary = "Get all recipes by ingredient",
            description = "Get all recipes that contain the given ingredient in their list")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded")}
    )
    @GetMapping(value = "ingredients", produces = "application/json")
    public List<RecipeDTO> getAllRecipesWithIngredient(@RequestParam String ingredientName) {
        return recipeService.getAllRecipesWithIngredient(ingredientName);
    }

    @Operation(summary = "Get all recipes by name",
            description = "Get all recipes that contain the given name in the recipe name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "operation succeeded")}
    )
    @GetMapping(value = "name", produces = "application/json")
    public List<RecipeDTO> getAllRecipesWithNameLike(@RequestParam String recipeName) {
        return recipeService.getAllRecipesByName(recipeName);
    }

    @Operation(summary = "Edit a recipe",
            description = "Edit the recipe name, ingredients or steps of the recipe with the given id. " +
                    "It returns 404 if the recipe with the id is not found, and 400 if the recipe " +
                    "has no changes")
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
            description = "Delete the recipe with the given id. " +
                    "It returns 404 if the recipe with the id is not found")
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
