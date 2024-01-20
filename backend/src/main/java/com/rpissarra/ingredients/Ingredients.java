package com.rpissarra.ingredients;

import com.rpissarra.recipe.Recipe;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ingredients")
public class Ingredients {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long idingredient;

    @Column(nullable = false, name = "NAME_INGREDIENT")
    private String name;

    @Column(nullable = false)
    private Date createdate;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Recipe.class)
    @JoinColumn(name = "idrecipe")
    private Recipe recipe;

    public Ingredients(){}

    public Ingredients(String name, Date createdate, Recipe recipe) {
        this.name = name;
        this.createdate = createdate;
        this.recipe = recipe;
    }

    public Ingredients(Long idingredient,String name, Date createdate, Recipe recipe) {
        this.idingredient = idingredient;
        this.name = name;
        this.createdate = createdate;
        this.recipe = recipe;
    }

    public Long getIdingredient() {
        return idingredient;
    }

    public void setIdingredient(Long idingredient) {
        this.idingredient = idingredient;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredients that = (Ingredients) o;
        return idingredient.equals(that.idingredient) && name.equals(that.name) && createdate.equals(that.createdate) && recipe.equals(that.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idingredient, name, createdate, recipe);
    }
}
