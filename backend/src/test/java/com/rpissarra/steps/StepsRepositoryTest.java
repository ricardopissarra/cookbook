package com.rpissarra.steps;

import com.rpissarra.AbstractDaoUnitTest;
import com.rpissarra.ingredients.IngredientRepository;
import com.rpissarra.ingredients.Ingredients;
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
import static org.junit.jupiter.api.Assertions.*;

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

        randomStep = FAKER.funnyName().name();
        Steps s2 = new Steps(
                randomStep, new Date(), recipe
        );
        underTest.save(s2);

        randomStep = FAKER.funnyName().name();
        Steps s3 = new Steps(
                randomStep, new Date(), recipe
        );
        underTest.save(s3);

        Long id = recipeRepository.findAll()
                .stream()
                .filter(r -> r.getName().equals(recipeName))
                .map(r -> r.getIdrecipe())
                .findFirst()
                .orElseThrow();

        // When
        List<Steps> lstSteps = underTest.findAllStepsByRecipeId(id);

        // Then
        assertThat(lstSteps).containsExactly(s1, s2, s3);
    }

    @Test
    void findAllStepsByRecipeIdIsEmpty() {
        // Given
        Long id = -1L;
        // When
        List<Steps> lstSteps = underTest.findAllStepsByRecipeId(id);

        // Then
        assertThat(lstSteps).isEmpty();
    }
}