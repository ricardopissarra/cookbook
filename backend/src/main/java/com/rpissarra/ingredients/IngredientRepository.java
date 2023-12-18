package com.rpissarra.ingredients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredients, Long> {

    @Query(value = "select i from Ingredients i where i.recipe.idrecipe = :id")
    List<Ingredients> findAllIngredientsByRecipeId(@Param("id") Long id);
}
