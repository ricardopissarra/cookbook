package com.rpissarra.ingredients;

import com.github.javafaker.Faker;
import com.rpissarra.recipe.Recipe;
import com.rpissarra.steps.Steps;
import com.rpissarra.steps.StepsRepository;
import com.rpissarra.steps.StepsService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientsServiceTest {

    private IngredientsService underTest;

    @Mock
    private IngredientRepository ingredientRepository;
    private AutoCloseable autoCloseable;
    private Faker FAKER;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new IngredientsService(ingredientRepository);
        FAKER = new Faker();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findAllIngredientsByRecipeId() {
        // GIVEN
        Long id = FAKER.number().randomNumber();
        String recipeName = FAKER.name().fullName();
        String ingredientName = FAKER.name().fullName();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );

        Ingredients ingredients = new Ingredients(
                ingredientName,
                createDate,
                recipe
        );
        recipe.setIngredients(List.of(ingredients));

        when(ingredientRepository.findAllIngredientsByRecipeId(id))
                .thenReturn(List.of(ingredients));

        //WHEN
        List<Ingredients> actual = underTest.findAllIngredientsByRecipeId(id);
        List<Ingredients> expected = List.of(ingredients);

        // THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void saveAllIngredients() {
        // GIVEN
        Date currDate = new Date();

        String recipeName = FAKER.name().firstName();
        String ingredientName = FAKER.name().firstName();

        Recipe recipe = new Recipe(recipeName,
                currDate);
        Ingredients ingredients = new Ingredients(ingredientName,
                currDate,
                recipe);

        // When
        underTest.saveAllIngredients(List.of(ingredients));

        // Then
        ArgumentCaptor<List<Ingredients>> stepsArgumentCaptor = ArgumentCaptor.forClass(
                List.class
        );
        verify(ingredientRepository).saveAll(stepsArgumentCaptor.capture());

        List<Ingredients> capturedIngredients = stepsArgumentCaptor.getValue();

        assertThat(capturedIngredients).isEqualTo(List.of(ingredients));
    }

    @Test
    void deleteAll() {
        // GIVEN
        Ingredients ingredients = new Ingredients(
                FAKER.name().nameWithMiddle(),
                new Date(),
                new Recipe(
                        FAKER.name().fullName(),
                        new Date()
                )
        );
        //WHEN
        underTest.deleteAll(List.of(ingredients));
        //THEN
        verify(ingredientRepository).deleteAll(List.of(ingredients));
    }
}