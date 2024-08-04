package com.rpissarra.steps;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StepsRepository extends JpaRepository<Steps, Long> {

    @Query(value = "select s from Steps s where s.recipe.idrecipe = :id")
    Steps findStepsByRecipeId(@Param("id") Long id);
}
