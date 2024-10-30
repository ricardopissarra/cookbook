package com.rpissarra.recipe;

import com.github.javafaker.Faker;
import com.rpissarra.exception.RequestValidationException;
import com.rpissarra.exception.ResourceNotFoundException;
import com.rpissarra.ingredients.Ingredients;
import com.rpissarra.ingredients.IngredientsService;
import com.rpissarra.ingredients.dto.IngredientsDTOMapper;
import com.rpissarra.recipe.dto.RecipeDTO;
import com.rpissarra.recipe.dto.RecipeDTOMapper;
import com.rpissarra.steps.Steps;
import com.rpissarra.steps.StepsService;
import com.rpissarra.steps.dto.StepsDTOMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;


class RecipeServiceTest {


    private RecipeService underTest;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private IngredientsService ingredientsService;
    @Mock
    private StepsService stepsService;

    private AutoCloseable autoCloseable;
    private Faker faker;
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
                ingredientsService,
                stepsService,
                recipeDTOMapper);
        faker = new Faker();
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
        String ingredientName = faker.name().name().toLowerCase();
        String stepName = faker.name().name().toLowerCase();
        String recipeName = faker.name().name();
        Long id = faker.number().randomNumber();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                createDate,
                recipe
        )));
        recipe.setSteps(new Steps(
                id,
                stepName,
                createDate,
                recipe
        ));

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
                faker.name().nameWithMiddle().toLowerCase(),
                List.of(faker.name().nameWithMiddle().toLowerCase()),
                faker.name().fullName()
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
        verify(ingredientsService).saveAllIngredients(ingredientsArgumentCaptor.capture());

        List<Ingredients> capturedIngredients = ingredientsArgumentCaptor.getValue();

        ArgumentCaptor<Steps> stepsArgumentCaptor = ArgumentCaptor.forClass(
                Steps.class
        );
        verify(stepsService).save(stepsArgumentCaptor.capture());

        Steps capturedSteps = stepsArgumentCaptor.getValue();

        assertThat(capturedRecipe.getIdrecipe()).isNull();
        assertThat(capturedRecipe.getName()).isEqualTo(recipeRegistrationRequest.name());
        assertThat(capturedIngredients.stream()
                .map(ingredientsDTOMapper)
                .map(i -> i.ingredientName())
                .toList()).isEqualTo(recipeRegistrationRequest.ingredients());
        assertThat(capturedSteps.getDescription()).isEqualTo(recipeRegistrationRequest.steps());

    }

    @Test
    void getAllRecipesByName() {
        // GIVEN
        String ingredientName = faker.name().name().toLowerCase();
        String stepName = faker.name().name().toLowerCase();
        String recipeName = faker.name().name().toLowerCase();
        Long id = faker.number().randomNumber();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                createDate,
                recipe
        )));
        recipe.setSteps(new Steps(
                id,
                stepName,
                createDate,
                recipe
        ));

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
        Long id = faker.number().randomNumber();
        String recipeName = faker.name().fullName();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );
        recipe.setIngredients(List.of(
                new Ingredients(faker.name().name().toLowerCase(),
                        createDate,
                        recipe)
        ));

        recipe.setSteps(
                new Steps(faker.name().name().toLowerCase(),
                        createDate,
                        recipe)
        );

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
        Long id = faker.number().randomNumber();

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
        String ingredientName = faker.name().name().toLowerCase();
        String stepName = faker.name().name().toLowerCase();
        String recipeName = faker.name().name().toLowerCase();
        Long id = faker.number().randomNumber();
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
        recipe.setSteps(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        ));

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
        String ingredientName = faker.name().name().toLowerCase();
        String stepName = faker.name().name().toLowerCase();
        String recipeName = faker.name().name().toLowerCase();
        Long id = faker.number().randomNumber();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                createDate,
                recipe
        )));
        recipe.setSteps(new Steps(
                id,
                stepName,
                createDate,
                recipe
        ));
        when(recipeRepository.existsRecipeByIdrecipe(id))
                .thenReturn(Boolean.TRUE);
        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(faker.name().name().toLowerCase(), null, null);
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
        String ingredientName = faker.name().name().toLowerCase();
        String stepName = faker.name().name().toLowerCase();
        String recipeName = faker.name().name().toLowerCase();
        Long id = faker.number().randomNumber();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                createDate,
                recipe
        )));
        recipe.setSteps(new Steps(
                id,
                stepName,
                createDate,
                recipe
        ));

        when(recipeRepository.existsRecipeByIdrecipe(id))
                .thenReturn(Boolean.TRUE);
        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(null,
                List.of(faker.name().nameWithMiddle().toLowerCase()),
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
        verify(ingredientsService).saveAllIngredients(ingredientsArgumentCaptor.capture());

        List<Ingredients> capturedIngredients = ingredientsArgumentCaptor.getValue();

        assertThat(capturedRecipe.getName()).isEqualTo(recipe.getName());
        assertThat(capturedIngredients.stream()
                .map(ingredientsDTOMapper)
                .map(i -> i.ingredientName())
                .toList()).isEqualTo(request.ingredients());
        assertThat(capturedRecipe.getSteps()).isEqualTo(recipe.getSteps());
    }

    @Test
    void updateRecipe_Steps() {
        // GIVEN
        String ingredientName = faker.name().name().toLowerCase();
        String stepName = faker.name().name().toLowerCase();
        String recipeName = faker.name().name().toLowerCase();
        Long id = faker.number().randomNumber();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                createDate,
                recipe
        )));
        recipe.setSteps(new Steps(
                id,
                stepName,
                createDate,
                recipe
        ));

        when(recipeRepository.existsRecipeByIdrecipe(id))
                .thenReturn(Boolean.TRUE);
        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(null,
                null,
                faker.name().nameWithMiddle());
        underTest.updateRecipe(id, request);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(
                Recipe.class
        );
        verify(recipeRepository).save(recipeArgumentCaptor.capture());

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();

        ArgumentCaptor<Steps> stepsArgumentCaptor = ArgumentCaptor.forClass(
                Steps.class
        );
        verify(stepsService).save(stepsArgumentCaptor.capture());

        Steps capturedSteps = stepsArgumentCaptor.getValue();

        assertThat(capturedRecipe.getName()).isEqualTo(recipe.getName());
        assertThat(capturedRecipe.getIngredients()).isEqualTo(recipe.getIngredients());
        assertThat(capturedSteps.getDescription()).isEqualTo(request.steps());
    }

    @Test
    void updateRecipe_AllFields() {
        // GIVEN
        String ingredientName = faker.name().name().toLowerCase();
        String stepName = faker.name().name().toLowerCase();
        String recipeName = faker.name().name().toLowerCase();
        Long id = faker.number().randomNumber();
        Date createDate = new Date();
        Recipe recipe = new Recipe(
                id,
                recipeName,
                createDate
        );

        recipe.setIngredients(List.of(new Ingredients(
                id,
                ingredientName,
                createDate,
                recipe
        )));
        recipe.setSteps(new Steps(
                id,
                stepName,
                createDate,
                recipe
        ));

        when(recipeRepository.existsRecipeByIdrecipe(id))
                .thenReturn(Boolean.TRUE);
        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(faker.name().fullName().toLowerCase(),
                List.of(faker.funnyName().name().toLowerCase()),
                faker.name().nameWithMiddle().toLowerCase());
        underTest.updateRecipe(id, request);

        ArgumentCaptor<Recipe> recipeArgumentCaptor = ArgumentCaptor.forClass(
                Recipe.class
        );
        verify(recipeRepository).save(recipeArgumentCaptor.capture());

        Recipe capturedRecipe = recipeArgumentCaptor.getValue();

        ArgumentCaptor<List<Ingredients>> ingredientsArgumentCaptor = ArgumentCaptor.forClass(
                List.class
        );
        verify(ingredientsService).saveAllIngredients(ingredientsArgumentCaptor.capture());

        List<Ingredients> capturedIngredients = ingredientsArgumentCaptor.getValue();

        ArgumentCaptor<Steps> stepsArgumentCaptor = ArgumentCaptor.forClass(
                Steps.class
        );
        verify(stepsService).save(stepsArgumentCaptor.capture());

        Steps capturedSteps = stepsArgumentCaptor.getValue();

        assertThat(capturedRecipe.getName()).isEqualTo(request.name());
        assertThat(capturedIngredients.stream()
                .map(ingredientsDTOMapper)
                .map(i -> i.ingredientName())
                .toList()).isEqualTo(request.ingredients());
        assertThat(capturedSteps.getDescription()).isEqualTo(request.steps());
    }

    @Test
    void updateRecipe_NoChanges() {
        // GIVEN
        String ingredientName = faker.name().name().toLowerCase();
        String stepName = faker.name().name().toLowerCase();
        String recipeName = faker.name().name().toLowerCase();
        Long id = faker.number().randomNumber();
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
        recipe.setSteps(new Steps(
                id,
                stepName,
                new Date(),
                recipe
        ));

        when(recipeRepository.existsRecipeByIdrecipe(id))
                .thenReturn(Boolean.TRUE);
        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(recipe));
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(recipe.getName(),
                recipe.getIngredients().stream()
                        .map(ingredientsDTOMapper)
                        .map(i -> i.ingredientName())
                        .toList(),
                recipe.getSteps().getDescription());

        // Then
        assertThatThrownBy(() -> underTest.updateRecipe(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No changes found in recipe with id [%s]".formatted(id));

        verify(recipeRepository, never()).save(any());
    }

    @Test
    void updateRecipe_NotFound() {
        // GIVEN
        Long id = faker.number().randomNumber();

        when(recipeRepository.existsRecipeByIdrecipe(id)).thenReturn(Boolean.FALSE);
        //WHEN
        RecipeUpdateRequest request = new RecipeUpdateRequest(
                faker.name().fullName().toLowerCase(),
                List.of(faker.name().fullName().toLowerCase()),
                faker.name().fullName().toLowerCase()
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
        long id = faker.number().randomNumber();

        when(recipeRepository.existsRecipeByIdrecipe(id))
                .thenReturn(Boolean.TRUE);
        when(recipeRepository.findById(id))
                .thenReturn(Optional.of(new Recipe()));
        //WHEN
        underTest.deleteRecipe(id);
        //THEN
        verify(recipeRepository).deleteById(id);
    }

    @Test
    void deleteRecipe_ThrowsException() {
        // GIVEN
        long id = faker.number().randomNumber();

        when(recipeRepository.existsRecipeByIdrecipe(id))
                .thenReturn(Boolean.FALSE);
        //WHEN
        assertThatThrownBy(() -> underTest.findRecipeById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Could not find any Recipe with id [%s]".formatted(id));
        //THEN
        verify(recipeRepository, never()).deleteById(id);
    }
}