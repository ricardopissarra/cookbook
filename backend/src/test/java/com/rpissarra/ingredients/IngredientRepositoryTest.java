package com.rpissarra.ingredients;

import com.rpissarra.AbstractDaoUnitTest;
import com.rpissarra.recipe.Recipe;
import com.rpissarra.recipe.RecipeRepository;
import com.rpissarra.steps.StepsRepository;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase( replace = AutoConfigureTestDatabase.Replace.NONE)
class IngredientRepositoryTest extends AbstractDaoUnitTest {

    @Autowired
    private IngredientRepository underTest;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private StepsRepository stepsRepository;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        stepsRepository.deleteAll();
        recipeRepository.deleteAll();
    }

    @Test
    void findAllIngredientsByRecipeIdIsNotEmpty() {
        // Given
        String recipeName = FAKER.name().name();

        Recipe recipe = new Recipe(
                recipeName, new Date()
        );
        recipeRepository.save(recipe);

        String randomIngredientName = FAKER.funnyName().name();
        Ingredients i1 = new Ingredients(
                randomIngredientName, new Date(), recipe
        );
        underTest.save(i1);

        randomIngredientName = FAKER.funnyName().name();
        Ingredients i2 = new Ingredients(
                randomIngredientName, new Date(), recipe
        );
        underTest.save(i2);

        randomIngredientName = FAKER.funnyName().name();
        Ingredients i3 = new Ingredients(
                randomIngredientName, new Date(), recipe
        );
        underTest.save(i3);

        Long id = recipeRepository.findAll()
                .stream()
                .filter(r -> r.getName().equals(recipeName))
                .map(r -> r.getIdrecipe())
                .findFirst()
                .orElseThrow();

        // When
        List<Ingredients> lstIngredients = underTest.findAllIngredientsByRecipeId(id);

        // Then
        assertThat(lstIngredients).containsExactly(i1, i2, i3);
    }

    @Test
    void findAllIngredientsByRecipeIdIsEmpty() {
        // Given
        Long id = -1L;
        // When
        List<Ingredients> lstIngredients = underTest.findAllIngredientsByRecipeId(id);

        // Then

        assertThat(lstIngredients).isEmpty();
    }
}