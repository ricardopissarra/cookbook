package com.rpissarra.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    boolean existsRecipeByIdrecipe(Long idrecipe);

    @Query(value = "SELECT r FROM Recipe r, Ingredients i WHERE r.idrecipe = i.recipe.idrecipe AND i.name like %:name%")
    List<Recipe> getAllRecipesWithIngredient(@Param("name") String ingredient);

    List<Recipe> findByNameContaining(String name);

    @Query(value = "(SELECT r.* FROM Recipe r, Ingredients i WHERE r.idrecipe = i.idrecipe AND i.name_ingredient like %:nameOrIngredient%)" +
            " UNION" +
            " (SELECT * FROM Recipe r WHERE r.name like %:nameOrIngredient%)", nativeQuery = true)
    List<Recipe> getAllRecipesWithKeyword(@Param("nameOrIngredient") String nameOrIngredient);
}
