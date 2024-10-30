package com.rpissarra.steps;

import com.github.javafaker.Faker;
import com.rpissarra.recipe.Recipe;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class StepsServiceTest {

    private StepsService underTest;

    @Mock
    private StepsRepository stepsRepository;
    private AutoCloseable autoCloseable;
    private Faker faker;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StepsService(stepsRepository);
        faker = new Faker();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void findStepsByRecipeId() {
        // GIVEN
        Long id = faker.number().randomNumber();
        String recipeName = faker.name().fullName();
        String stepsDescription = faker.name().fullName();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );

        Steps steps = new Steps(
                stepsDescription,
                createDate,
                recipe);
        recipe.setSteps(steps);

        when(stepsRepository.findStepsByRecipeId(id))
                .thenReturn(steps);

        //WHEN
        Steps actual = underTest.findStepsByRecipeId(id);
        Steps expected = steps;

        // THEN
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void save() {
        // GIVEN
        Date currDate = new Date();

        String recipeName = faker.name().firstName();
        String stepDescription = faker.name().firstName();

        Recipe recipe = new Recipe(recipeName,
                currDate);
        Steps steps = new Steps(stepDescription,
                currDate,
                recipe);

        // When
        underTest.save(steps);

        // Then
        ArgumentCaptor<Steps> stepsArgumentCaptor = ArgumentCaptor.forClass(
                Steps.class
        );
        verify(stepsRepository).save(stepsArgumentCaptor.capture());

        Steps capturedSteps = stepsArgumentCaptor.getValue();

        assertThat(capturedSteps.getDescription()).isEqualTo(steps.getDescription());
    }

    @Test
    void delete() {
        // GIVEN
        Steps steps = new Steps(
                faker.name().nameWithMiddle(),
                new Date(),
                new Recipe(
                        faker.name().fullName(),
                        new Date()
                )
        );
        //WHEN
        underTest.delete(steps);
        //THEN
        verify(stepsRepository).delete(steps);
    }
}