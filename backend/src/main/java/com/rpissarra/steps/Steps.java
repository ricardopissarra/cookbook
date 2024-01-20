package com.rpissarra.steps;

import com.rpissarra.recipe.Recipe;
import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "steps")
public class Steps {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long idstep;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Date createdate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idrecipe")
    private Recipe recipe;

    public Steps() {}

    public Steps(String description, Date createdate, Recipe recipe) {
        this.description = description;
        this.createdate = createdate;
        this.recipe = recipe;
    }

    public Steps(Long idstep,String description, Date createdate, Recipe recipe) {
        this.idstep = idstep;
        this.description = description;
        this.createdate = createdate;
        this.recipe = recipe;
    }

    public Long getIdstep() {
        return idstep;
    }

    public void setIdstep(Long idstep) {
        this.idstep = idstep;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        Steps steps = (Steps) o;
        return idstep.equals(steps.idstep) && description.equals(steps.description) && createdate.equals(steps.createdate) && recipe.equals(steps.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idstep, description, createdate, recipe);
    }
}
