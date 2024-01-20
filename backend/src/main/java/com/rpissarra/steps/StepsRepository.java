package com.rpissarra.steps;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StepsRepository extends JpaRepository<Steps, Long> {

    @Query(value = "select s from Steps s where s.recipe.idrecipe = :id")
    List<Steps> findAllStepsByRecipeId(@Param("id") Long id);
}
