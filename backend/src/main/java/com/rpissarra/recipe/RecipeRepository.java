package com.rpissarra.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    boolean existsRecipeByIdrecipe(Long idrecipe);
}
