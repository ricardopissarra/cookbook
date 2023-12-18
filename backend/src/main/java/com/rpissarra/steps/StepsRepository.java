package com.rpissarra.steps;

import com.rpissarra.ingredients.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StepsRepository extends JpaRepository<Steps, Long> {

    @Query(value = "select s from Steps s where s.recipe.idrecipe = :id")
    List<Steps> findAllStepsByRecipeId(@Param("id") Long id);
}
