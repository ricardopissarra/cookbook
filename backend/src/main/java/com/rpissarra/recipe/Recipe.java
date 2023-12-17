package com.rpissarra.recipe;


import jakarta.persistence.*;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "recipe")
public class Recipe {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long idrecipe;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Date createdate;

    @Column(nullable = true)
    private Date updatedate;

    public Recipe() {}

    public Recipe(Long id, String name, Date createdate) {
        this.idrecipe = id;
        this.name = name;
        this.createdate = createdate;
    }

    public Long getIdrecipe() {
        return idrecipe;
    }

    public void setIdrecipe(Long id) {
        this.idrecipe = id;
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

    public Date getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(Date updatedate) {
        this.updatedate = updatedate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return idrecipe.equals(recipe.idrecipe) && name.equals(recipe.name) && createdate.equals(recipe.createdate) && Objects.equals(updatedate, recipe.updatedate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idrecipe, name, createdate, updatedate);
    }
}
