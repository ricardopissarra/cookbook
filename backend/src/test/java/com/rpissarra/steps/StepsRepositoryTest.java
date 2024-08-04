package com.rpissarra.steps;

import com.rpissarra.AbstractDaoUnitTest;
import com.rpissarra.ingredients.IngredientRepository;
import com.rpissarra.recipe.Recipe;
import com.rpissarra.recipe.RecipeRepository;
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
class StepsRepositoryTest extends AbstractDaoUnitTest {

    @Autowired
    StepsRepository underTest;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;



    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        ingredientRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    @Test
    void findAllStepsByRecipeIdIsNotEmpty() {
        // Given
        String recipeName = FAKER.name().name();

        Recipe recipe = new Recipe(
                recipeName, new Date()
        );
        recipeRepository.save(recipe);

        String randomStep = FAKER.funnyName().name();
        Steps s1 = new Steps(
                randomStep, new Date(), recipe
        );
        underTest.save(s1);

        Long id = recipeRepository.findAll()
                .stream()
                .filter(r -> r.getName().equals(recipeName))
                .map(r -> r.getIdrecipe())
                .findFirst()
                .orElseThrow();

        // When
        Steps recipeSteps = underTest.findStepsByRecipeId(id);

        // Then
        assertThat(recipeSteps).isEqualTo(s1);
    }

    @Test
    void findAllStepsByRecipeIdIsEmpty() {
        // Given
        Long id = -1L;
        // When
        Steps recipeSteps = underTest.findStepsByRecipeId(id);

        // Then
        assertThat(recipeSteps).isNull();
    }
}