package com.rpissarra.recipe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    boolean existsRecipeByIdrecipe(Long idrecipe);

    @Query(value = "SELECT r.* FROM Recipe r, Ingredients i WHERE r.idrecipe = i.idrecipe AND i.name_ingredient like %:name% " +
            "order by 1 desc", nativeQuery = true)
    List<Recipe> getAllRecipesWithIngredient(@Param("name") String ingredient);

    List<Recipe> findByNameContaining(String name);

    @Query(value = "(SELECT r.* FROM Recipe r, Ingredients i WHERE r.idrecipe = i.idrecipe AND i.name_ingredient like %:nameOrIngredient% " +
            "order by 1 desc)" +
            " UNION" +
            " (SELECT * FROM Recipe r WHERE r.name like %:nameOrIngredient% " +
            "order by 1 desc)", nativeQuery = true)
    List<Recipe> getAllRecipesByNameOrIngredient(@Param("nameOrIngredient") String nameOrIngredient);
}
