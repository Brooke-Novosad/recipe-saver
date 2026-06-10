package com.brookenovosad.recipe_saver.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipe {
    
    private String title;

    private List<Ingredient> ingredients;

    private List<String> instructions;

    private String recipeUrl;
    
    public Recipe(String title, List<Ingredient> ingredients, List<String> instructions, String recipeUrl) {
        this.title = title;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.recipeUrl = recipeUrl;
    }
}
