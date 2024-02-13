package com.rpissarra.recipe;

import com.rpissarra.AbstractDaoUnitTest;
import com.rpissarra.ingredients.IngredientRepository;
import com.rpissarra.ingredients.Ingredients;
import com.rpissarra.steps.StepsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
@AutoConfigureTestDatabase( replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipeRepositoryTest extends AbstractDaoUnitTest {

    @Autowired
    private RecipeRepository underTest;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private StepsRepository stepsRepository;


    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll();
        stepsRepository.deleteAll();
        underTest.deleteAll();
    }

    @Test
    void existsRecipeByIdrecipeIsTrue() {
        // Given
        String name = FAKER.name().nameWithMiddle().toLowerCase();
        Recipe recipe = new Recipe(
                name, new Date()
        );

        underTest.save(recipe);
        // When
        Long id = underTest.findAll()
                .stream()
                .filter(r -> r.getName().equals(name))
                .map(r -> r.getIdrecipe())
                .findFirst()
                .orElseThrow();

        boolean result = underTest.existsRecipeByIdrecipe(id);
        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsRecipeByIdrecipeIsFalse() {
        // Given
        Long id = -1L;
        // When

        boolean result = underTest.existsRecipeByIdrecipe(id);
        // Then
        assertThat(result).isFalse();
    }
    @Test
    void getAllRecipesWithIngredientIsNotEmpty() {
        // Given
        String recipeName = FAKER.name().nameWithMiddle().toLowerCase();
        String ingredientName = "carrot";
        Recipe recipe1 = new Recipe(
                recipeName, new Date()
        );
        Ingredients ingredients1 = new Ingredients(
                ingredientName, new Date(), recipe1
        );
        underTest.save(recipe1);
        ingredientRepository.save(ingredients1);

        recipeName = FAKER.name().nameWithMiddle().toLowerCase();
        String randomIngredientName = FAKER.name().nameWithMiddle();
        Recipe recipe2 = new Recipe(
                recipeName, new Date()
        );
        Ingredients ingredients2 = new Ingredients(
                randomIngredientName, new Date(), recipe2
        );
        underTest.save(recipe2);
        ingredientRepository.save(ingredients2);

        // When
        List<Recipe> lstRecipe = underTest.getAllRecipesWithIngredient(ingredientName);

        //Then
        assertThat(lstRecipe).contains(recipe1);
    }

    @Test
    void getAllRecipesWithIngredientIsEmpty() {
        // Given
        String recipeName = FAKER.name().nameWithMiddle().toLowerCase();
        String ingredientName = "carrot";
        String randomIngredientName = FAKER.name().nameWithMiddle();
        Recipe recipe1 = new Recipe(
                recipeName, new Date()
        );
        Ingredients ingredients1 = new Ingredients(
                randomIngredientName, new Date(), recipe1
        );
        underTest.save(recipe1);
        ingredientRepository.save(ingredients1);

        recipeName = FAKER.name().nameWithMiddle().toLowerCase();
        Recipe recipe2 = new Recipe(
                recipeName, new Date()
        );
        Ingredients ingredients2 = new Ingredients(
                randomIngredientName, new Date(), recipe2
        );
        underTest.save(recipe2);
        ingredientRepository.save(ingredients2);

        // When
        List<Recipe> lstRecipe = underTest.getAllRecipesWithIngredient(ingredientName);

        //Then
        assertThat(lstRecipe).isEmpty();
    }

    @Test
    void findByNameContainingIsNotEmpty() {
        String name = FAKER.name().nameWithMiddle().toLowerCase();
        Recipe recipe = new Recipe(
                name, new Date()
        );

        underTest.save(recipe);

        List<Recipe> lstRecipes = underTest.findByNameContaining(name);

        assertThat(recipe).isEqualTo(lstRecipes.get(0));
    }

    @Test
    void findByNameContainingIsEmpty() {
        // Given
        String name = FAKER.name().nameWithMiddle().toLowerCase();

        // When
        List<Recipe> lstRecipes = underTest.findByNameContaining(name);

        // Then
        assertThat(lstRecipes).isEmpty();
    }

    @Test
    void getAllRecipesWithKeywordIsNotEmpty() {
        // Given
        String recipeName = FAKER.name().nameWithMiddle().toLowerCase();
        String keyword = FAKER.nation().language();
        Recipe recipe1 = new Recipe(
               recipeName, new Date()
        );
        Ingredients ingredients1 = new Ingredients(
                keyword, new Date(), recipe1
        );
        underTest.save(recipe1);
        ingredientRepository.save(ingredients1);

        recipeName = FAKER.name().nameWithMiddle().toLowerCase();
        String randomIngredient = FAKER.name().nameWithMiddle();
        Recipe recipe2 = new Recipe(
                recipeName + " " + keyword, new Date()
        );
        Ingredients ingredients2 = new Ingredients(
                randomIngredient, new Date(), recipe2
        );
        underTest.save(recipe2);
        ingredientRepository.save(ingredients2);

        // When
        List<Recipe> lstRecipe = underTest.getAllRecipesByNameOrIngredient(keyword);

        // Then
        assertThat(lstRecipe).hasSize(2);
        assertThat(lstRecipe).contains(recipe1, recipe2);
    }

    @Test
    void getAllRecipesWithKeywordIsEmpty() {
        // Given
        String recipeName = FAKER.name().nameWithMiddle().toLowerCase();
        String keyword = FAKER.nation().capitalCity();
        String ingredientName = FAKER.name().nameWithMiddle();
        Recipe recipe1 = new Recipe(
                recipeName, new Date()
        );
        Ingredients ingredients1 = new Ingredients(
                ingredientName, new Date(), recipe1
        );
        underTest.save(recipe1);
        ingredientRepository.save(ingredients1);

        recipeName = FAKER.name().nameWithMiddle().toLowerCase();
        Recipe recipe2 = new Recipe(
               recipeName, new Date()
        );
        Ingredients ingredients2 = new Ingredients(
                ingredientName, new Date(), recipe2
        );
        underTest.save(recipe2);
        ingredientRepository.save(ingredients2);

        // When
        List<Recipe> lstRecipe = underTest.getAllRecipesByNameOrIngredient(keyword);

        // Then
        assertThat(lstRecipe).isEmpty();
    }
}