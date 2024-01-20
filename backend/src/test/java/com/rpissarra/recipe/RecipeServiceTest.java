package com.rpissarra.recipe;

import com.github.javafaker.Faker;
import com.rpissarra.exception.RequestValidationException;
import com.rpissarra.exception.ResourceNotFoundException;
import com.rpissarra.ingredients.IngredientRepository;
import com.rpissarra.ingredients.Ingredients;
import com.rpissarra.ingredients.dto.IngredientsDTOMapper;
import com.rpissarra.recipe.dto.RecipeDTO;
import com.rpissarra.recipe.dto.RecipeDTOMapper;
import com.rpissarra.steps.Steps;
import com.rpissarra.steps.StepsRepository;
import com.rpissarra.steps.dto.StepsDTOMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


class RecipeServiceTest {


    private RecipeService underTest;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private StepsRepository stepsRepository;

    private AutoCloseable autoCloseable;
    private Faker FAKER;
    private final StepsDTOMapper stepsDTOMapper = new StepsDTOMapper();
    private final IngredientsDTOMapper ingredientsDTOMapper = new IngredientsDTOMapper();
    private final RecipeDTOMapper recipeDTOMapper = new RecipeDTOMapper(
            ingredientsDTOMapper,
            stepsDTOMapper
    );

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new RecipeService(recipeRepository,
                ingredientRepository,
                stepsRepository,
                recipeDTOMapper);
        FAKER = new Faker();
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void getAllRecipes() {
        // WHEN
        underTest.getAllRecipes();
        // THEN
        verify(recipeRepository).findAll();
    }

    @Test
    void getAllRecipesWithIngredientWithResult() {
        // GIVEN
        String ingredientName = FAKER.name().name().toLowerCase();
        String stepName = FAKER.name().name().toLowerCase();
        String recipeName = FAKER.name().name();
        Long id = FAKER.number().randomNumber();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                new Date()
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                new Date(),
                recipe
        )));
        recipe.setSteps(List.of(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        )));

        when(recipeRepository.getAllRecipesWithIngredient(ingredientName))
                .thenReturn(List.of(recipe));

        // WHEN
        List<RecipeDTO> lstActual = underTest.getAllRecipesWithIngredient(ingredientName);
        List<RecipeDTO> lstExpected = List.of(recipeDTOMapper.apply(recipe));
        // THEN
        assertThat(lstActual).isEqualTo(lstExpected);
    }

    @Test
    void addRecipe() {
        // Given
        RecipeRegistrationRequest recipeRegistrationRequest = new RecipeRegistrationRequest(
                FAKER.name().nameWithMiddle().toLowerCase(),
                List.of(FAKER.name().nameWithMiddle().toLowerCase()),
                List.of(FAKER.name().fullName().toLowerCase())
        );

        // When
        underTest.addRecipe(recipeRegistrationRequest);

        // Then
        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(
                Recipe.class
        );
        verify(recipeRepository).save(recipeArgumentCaptor.capture());

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();

        ArgumentCaptor<List<Ingredients>> ingredientsArgumentCaptor = ArgumentCaptor.forClass(
                List.class
        );
        verify(ingredientRepository).saveAll(ingredientsArgumentCaptor.capture());

        List<Ingredients> capturedIngredients = ingredientsArgumentCaptor.getValue();

        ArgumentCaptor<List<Steps>> stepsArgumentCaptor = ArgumentCaptor.forClass(
                List.class
        );
        verify(stepsRepository).saveAll(stepsArgumentCaptor.capture());

        List<Steps> capturedSteps = stepsArgumentCaptor.getValue();

        assertThat(capturedRecipe.getIdrecipe()).isNull();
        assertThat(capturedRecipe.getName()).isEqualTo(recipeRegistrationRequest.name());
        assertThat(capturedIngredients.stream()
                .map(ingredientsDTOMapper)
                .map(i -> i.ingredientName())
                .collect(Collectors.toList())).isEqualTo(recipeRegistrationRequest.ingredients());
        assertThat(capturedSteps.stream()
                .map(stepsDTOMapper)
                .map(s -> s.description())
                .collect(Collectors.toList())).isEqualTo(recipeRegistrationRequest.steps());

    }

    @Test
    void getRecipeById() {
        // Given
        Long id = FAKER.number().randomNumber();
        String recipeName = FAKER.name().fullName();
        Date recipeCreateDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                recipeCreateDate
        );

        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));

        //WHEN
        Recipe actual = underTest.getRecipeById(id).get();

        // Then
        assertThat(actual).isEqualTo(recipe);
    }

    @Test
    void getAllRecipesByName() {
        // GIVEN
        String ingredientName = FAKER.name().name().toLowerCase();
        String stepName = FAKER.name().name().toLowerCase();
        String recipeName = FAKER.name().name().toLowerCase();
        Long id = FAKER.number().randomNumber();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                new Date()
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                new Date(),
                recipe
        )));
        recipe.setSteps(List.of(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        )));

        when(recipeRepository.findByNameContaining(recipeName))
                .thenReturn(List.of(recipe));

        // WHEN
        List<RecipeDTO> lstActual = underTest.getAllRecipesByName(recipeName);
        List<RecipeDTO> lstExpected = List.of(recipeDTOMapper.apply(recipe));
        // THEN
        assertThat(lstActual).isEqualTo(lstExpected);
    }

    @Test
    void findRecipeById() {
        // Given
        Long id = FAKER.number().randomNumber();
        String recipeName = FAKER.name().fullName();
        Date recipeCreateDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                recipeCreateDate
        );
        recipe.setIngredients(List.of(
                new Ingredients(FAKER.name().name().toLowerCase(),
                        new Date(),
                        recipe)
        ));

        recipe.setSteps(List.of(
                new Steps(FAKER.name().name().toLowerCase(),
                        new Date(),
                        recipe)
        ));

        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));

        //WHEN
        RecipeDTO actual = underTest.findRecipeById(id);
        RecipeDTO expected = recipeDTOMapper.apply(recipe);

        // Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findRecipeById_ThrowsException() {
        // Given
        Long id = FAKER.number().randomNumber();

        //WHEN
        when(recipeRepository.findById(id))
                .thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> underTest.findRecipeById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Could not find any Recipe with id [%s]".formatted(id));
    }

    @Test
    void getAllRecipesByNameOrIngredient() {
        // GIVEN
        String ingredientName = FAKER.name().name().toLowerCase();
        String stepName = FAKER.name().name().toLowerCase();
        String recipeName = FAKER.name().name().toLowerCase();
        Long id = FAKER.number().randomNumber();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                new Date()
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                new Date(),
                recipe
        )));
        recipe.setSteps(List.of(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        )));

        when(recipeRepository.getAllRecipesByNameOrIngredient(recipeName))
                .thenReturn(List.of(recipe));

        // WHEN
        List<RecipeDTO> lstActual = underTest.getAllRecipesByNameOrIngredient(recipeName);
        List<RecipeDTO> lstExpected = List.of(recipeDTOMapper.apply(recipe));
        // THEN
        assertThat(lstActual).isEqualTo(lstExpected);
    }

    @Test
    void updateRecipe_Name() {
        // GIVEN
        String ingredientName = FAKER.name().name().toLowerCase();
        String stepName = FAKER.name().name().toLowerCase();
        String recipeName = FAKER.name().name().toLowerCase();
        Long id = FAKER.number().randomNumber();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                new Date()
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                new Date(),
                recipe
        )));
        recipe.setSteps(List.of(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        )));
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(FAKER.name().name().toLowerCase(), null, null);
        underTest.updateRecipe(id, request);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(
                Recipe.class
        );
        verify(recipeRepository).save(recipeArgumentCaptor.capture());

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();


        assertThat(capturedRecipe.getName()).isEqualTo(request.name());
        assertThat(capturedRecipe.getIngredients()).isEqualTo(recipe.getIngredients());
        assertThat(capturedRecipe.getSteps()).isEqualTo(recipe.getSteps());
    }

    @Test
    void updateRecipe_Ingredients() {
        // GIVEN
        String ingredientName = FAKER.name().name().toLowerCase();
        String stepName = FAKER.name().name().toLowerCase();
        String recipeName = FAKER.name().name().toLowerCase();
        Long id = FAKER.number().randomNumber();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                new Date()
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                new Date(),
                recipe
        )));
        recipe.setSteps(List.of(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        )));
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(null,
                List.of(FAKER.name().nameWithMiddle().toLowerCase()),
                null);
        underTest.updateRecipe(id, request);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(
                Recipe.class
        );
        verify(recipeRepository).save(recipeArgumentCaptor.capture());

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();

        ArgumentCaptor<List<Ingredients>> ingredientsArgumentCaptor = ArgumentCaptor.forClass(
                List.class
        );
        verify(ingredientRepository).saveAll(ingredientsArgumentCaptor.capture());

        List<Ingredients> capturedIngredients = ingredientsArgumentCaptor.getValue();

        assertThat(capturedRecipe.getName()).isEqualTo(recipe.getName());
        assertThat(capturedIngredients.stream()
                .map(ingredientsDTOMapper)
                .map(i -> i.ingredientName())
                .collect(Collectors.toList())).isEqualTo(request.ingredients());
        assertThat(capturedRecipe.getSteps()).isEqualTo(recipe.getSteps());
    }

    @Test
    void updateRecipe_Steps() {
        // GIVEN
        String ingredientName = FAKER.name().name().toLowerCase();
        String stepName = FAKER.name().name().toLowerCase();
        String recipeName = FAKER.name().name().toLowerCase();
        Long id = FAKER.number().randomNumber();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                new Date()
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                new Date(),
                recipe
        )));
        recipe.setSteps(List.of(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        )));
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(null,
                null,
                List.of(FAKER.name().nameWithMiddle().toLowerCase()));
        underTest.updateRecipe(id, request);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(
                Recipe.class
        );
        verify(recipeRepository).save(recipeArgumentCaptor.capture());

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();

        ArgumentCaptor<List<Steps>> stepsArgumentCaptor = ArgumentCaptor.forClass(
                List.class
        );
        verify(stepsRepository).saveAll(stepsArgumentCaptor.capture());

        List<Steps> capturedSteps = stepsArgumentCaptor.getValue();

        assertThat(capturedRecipe.getName()).isEqualTo(recipe.getName());
        assertThat(capturedRecipe.getIngredients()).isEqualTo(recipe.getIngredients());
        assertThat(capturedSteps.stream()
                .map(stepsDTOMapper)
                .map(s -> s.description())
                .collect(Collectors.toList())).isEqualTo(request.steps());
    }

    @Test
    void updateRecipe_AllFields() {
        // GIVEN
        String ingredientName = FAKER.name().name().toLowerCase();
        String stepName = FAKER.name().name().toLowerCase();
        String recipeName = FAKER.name().name().toLowerCase();
        Long id = FAKER.number().randomNumber();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                new Date()
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                new Date(),
                recipe
        )));
        recipe.setSteps(List.of(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        )));
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(FAKER.name().fullName().toLowerCase(),
                List.of(FAKER.funnyName().name().toLowerCase()),
                List.of(FAKER.name().nameWithMiddle().toLowerCase()));
        underTest.updateRecipe(id, request);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(
                Recipe.class
        );
        verify(recipeRepository).save(recipeArgumentCaptor.capture());

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();

        ArgumentCaptor<List<Ingredients>> ingredientsArgumentCaptor = ArgumentCaptor.forClass(
                List.class
        );
        verify(ingredientRepository).saveAll(ingredientsArgumentCaptor.capture());

        List<Ingredients> capturedIngredients = ingredientsArgumentCaptor.getValue();

        ArgumentCaptor<List<Steps>> stepsArgumentCaptor = ArgumentCaptor.forClass(
                List.class
        );
        verify(stepsRepository).saveAll(stepsArgumentCaptor.capture());

        List<Steps> capturedSteps = stepsArgumentCaptor.getValue();

        assertThat(capturedRecipe.getName()).isEqualTo(request.name());
        assertThat(capturedIngredients.stream()
                .map(ingredientsDTOMapper)
                .map(i -> i.ingredientName())
                .collect(Collectors.toList())).isEqualTo(request.ingredients());
        assertThat(capturedSteps.stream()
                .map(stepsDTOMapper)
                .map(s -> s.description())
                .collect(Collectors.toList())).isEqualTo(request.steps());
    }

    @Test
    void updateRecipe_NoChanges() {
        // GIVEN
        String ingredientName = FAKER.name().name().toLowerCase();
        String stepName = FAKER.name().name().toLowerCase();
        String recipeName = FAKER.name().name().toLowerCase();
        Long id = FAKER.number().randomNumber();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                new Date()
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                new Date(),
                recipe
        )));
        recipe.setSteps(List.of(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        )));
        when(recipeRepository.findById(id)).thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(recipe.getName(),
                recipe.getIngredients().stream()
                        .map(ingredientsDTOMapper)
                        .map(i -> i.ingredientName())
                        .collect(Collectors.toList()),
                recipe.getSteps().stream()
                        .map(stepsDTOMapper)
                        .map(s -> s.description())
                        .collect(Collectors.toList()));

        // Then
        assertThatThrownBy(() -> underTest.updateRecipe(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No changes found in recipe with id [%s]".formatted(id));

        verify(recipeRepository, never()).save(any());
    }

    @Test
    void updateRecipe_NotFound() {
        // GIVEN
        Long id = FAKER.number().randomNumber();

        when(recipeRepository.findById(id)).thenReturn(Optional.empty());
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(
                FAKER.name().fullName().toLowerCase(),
                List.of(FAKER.name().fullName().toLowerCase()),
                List.of(FAKER.name().fullName().toLowerCase())
        );

        // Then
        assertThatThrownBy(() -> underTest.updateRecipe(id, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Could not find any Recipe with id [%s]".formatted(id));

        verify(recipeRepository, never()).save(any());
    }
    @Test
    void deleteRecipe() {
        // GIVEN
        long id = FAKER.number().randomNumber();

        when(recipeRepository.findById(id)).thenReturn(Optional.of(new Recipe()));
        //WHEN
        underTest.deleteRecipe(id);
        //THEN
        verify(recipeRepository).deleteById(id);
    }

    @Test
    void deleteRecipe_ThrowsException() {
        // GIVEN
        long id = FAKER.number().randomNumber();

        when(recipeRepository.findById(id)).thenReturn(Optional.empty());
        //WHEN
        assertThatThrownBy(() -> underTest.findRecipeById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Could not find any Recipe with id [%s]".formatted(id));
        //THEN
        verify(recipeRepository, never()).deleteById(id);
    }
}