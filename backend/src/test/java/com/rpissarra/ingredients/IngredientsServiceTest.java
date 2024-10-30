package com.rpissarra.ingredients;

import com.github.javafaker.Faker;
import com.rpissarra.recipe.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IngredientsServiceTest {

    private IngredientsService underTest;

    @Mock
    private IngredientRepository ingredientRepository;
    private AutoCloseable autoCloseable;
    private Faker faker;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new IngredientsService(ingredientRepository);
        faker = new Faker();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findAllIngredientsByRecipeId() {
        // GIVEN
        Long id = faker.number().randomNumber();
        String recipeName = faker.name().fullName();
        String ingredientName = faker.name().fullName();
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

        String recipeName = faker.name().firstName();
        String ingredientName = faker.name().firstName();

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
                faker.name().nameWithMiddle(),
                new Date(),
                new Recipe(
                        faker.name().fullName(),
                        new Date()
                )
        );
        //WHEN
        underTest.deleteAll(List.of(ingredients));
        //THEN
        verify(ingredientRepository).deleteAll(List.of(ingredients));
    }
}