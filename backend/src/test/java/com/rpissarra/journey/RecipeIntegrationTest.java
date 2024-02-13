package com.rpissarra.journey;

import com.github.javafaker.Faker;
import com.rpissarra.ingredients.dto.IngredientsDTO;
import com.rpissarra.recipe.RecipeRegistrationRequest;
import com.rpissarra.recipe.RecipeUpdateRequest;
import com.rpissarra.recipe.dto.RecipeDTO;
import com.rpissarra.steps.dto.StepsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class RecipeIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String RECIPE_URI = "api/v1/recipe";


    @Test
    void canAddARecipe() {
        // create a registration request
        Faker faker = new Faker();
        String recipeName = faker.name().fullName();
        String ingredientNameOne = faker.name().name().toLowerCase();
        String ingredientNameTwo = faker.name().name().toLowerCase();
        String stepNameOne = faker.name().name().toLowerCase();

        RecipeRegistrationRequest request = new RecipeRegistrationRequest(
                recipeName,
                List.of(ingredientNameOne, ingredientNameTwo),
                stepNameOne
        );

        // send a post request
        webTestClient.post()
                .uri(RECIPE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RecipeRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all recipes
        List<RecipeDTO> allRecipes = webTestClient.get()
                .uri(RECIPE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<RecipeDTO>() {})
                .returnResult()
                .getResponseBody();

        long id = allRecipes.stream()
                .filter(r -> r.name().equals(recipeName.toLowerCase()))
                .map(RecipeDTO::id)
                .findFirst()
                .orElseThrow();

        List<IngredientsDTO> lstIngredients = allRecipes.stream()
                .filter( r -> r.name().equals(recipeName.toLowerCase()))
                .map(RecipeDTO::ingredients)
                .findAny()
                .orElseThrow();

        StepsDTO lstSteps = allRecipes.stream()
                .filter( r -> r.name().equals(recipeName.toLowerCase()))
                .map(RecipeDTO::steps)
                .findAny()
                .orElseThrow();

        RecipeDTO expectedRecipe = new RecipeDTO(
                id,
                recipeName.toLowerCase(),
                lstIngredients,
                lstSteps
        );

        assertThat(allRecipes).contains(expectedRecipe);

        // get recipe by id
        webTestClient.get()
                .uri(RECIPE_URI+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<RecipeDTO>() {})
                .isEqualTo(expectedRecipe);

    }

    @Test
    void canDeleteARecipe() {
        // create a registration request
        Faker faker = new Faker();
        String recipeName = faker.name().fullName();
        String ingredientNameOne = faker.name().name().toLowerCase();
        String ingredientNameTwo = faker.name().name().toLowerCase();
        String stepNameOne = faker.name().name().toLowerCase();

        RecipeRegistrationRequest request = new RecipeRegistrationRequest(
                recipeName,
                List.of(ingredientNameOne, ingredientNameTwo),
                stepNameOne
        );

        // send a post request
        webTestClient.post()
                .uri(RECIPE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RecipeRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all recipes
        List<RecipeDTO> allRecipes = webTestClient.get()
                .uri(RECIPE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<RecipeDTO>() {})
                .returnResult()
                .getResponseBody();

        long id = allRecipes.stream()
                .filter(r -> r.name().equals(recipeName.toLowerCase()))
                .map(RecipeDTO::id)
                .findFirst()
                .orElseThrow();

        // delete recipe by id
        webTestClient.delete()
                .uri(RECIPE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();

        // get recipe by id
        webTestClient.get()
                .uri(RECIPE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateARecipe() {
        // create a registration request
        Faker faker = new Faker();
        String recipeName = faker.name().fullName();
        String ingredientNameOne = faker.name().name().toLowerCase();
        String ingredientNameTwo = faker.name().name().toLowerCase();
        String stepNameOne = faker.name().name().toLowerCase();
        List<String> originalIngredients = List.of(ingredientNameOne, ingredientNameTwo);


        RecipeRegistrationRequest request = new RecipeRegistrationRequest(
                recipeName,
                originalIngredients,
                stepNameOne
        );

        // send a post request
        webTestClient.post()
                .uri(RECIPE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RecipeRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all recipes
        List<RecipeDTO> allRecipes = webTestClient.get()
                .uri(RECIPE_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<RecipeDTO>() {})
                .returnResult()
                .getResponseBody();

        long id = allRecipes.stream()
                .filter(r -> r.name().equals(recipeName.toLowerCase()))
                .map(RecipeDTO::id)
                .findFirst()
                .orElseThrow();

        // create update request
        String updatedName = faker.name().nameWithMiddle();
        List<String> updatedIngredient = List.of(faker.name().nameWithMiddle().toLowerCase(), faker.name().nameWithMiddle().toLowerCase());
        String updatedStep = faker.name().nameWithMiddle();

        RecipeUpdateRequest updateRequest = new RecipeUpdateRequest(
                updatedName.toLowerCase(),
                updatedIngredient,
                updatedStep
        );

        webTestClient.put()
                .uri(RECIPE_URI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), RecipeUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();


        // get recipe by id && validate that is updated
        List<IngredientsDTO> lstIngredients = updatedIngredient.stream()
                .map(IngredientsDTO::new)
                .collect(Collectors.toList());


        RecipeDTO expectedRecipe = new RecipeDTO(
                id,
                updatedName.toLowerCase(),
                lstIngredients,
                new StepsDTO(updatedStep)
        );

        RecipeDTO actualRecipe = webTestClient.get()
                .uri(RECIPE_URI+"/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<RecipeDTO>() {})
                .returnResult()
                .getResponseBody();

        assertThat(actualRecipe).isEqualTo(expectedRecipe);
    }
}
