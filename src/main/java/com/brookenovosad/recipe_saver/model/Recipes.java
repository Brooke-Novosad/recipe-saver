package com.brookenovosad.recipe_saver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Recipes {
    

    private String title;

    private String recipe;

    private String recipeUrl;
    
    public Recipes(String title, String recipe, String recipeUrl) {
        this.title = title;
        this.recipe = recipe;
        this.recipeUrl = recipeUrl;
    }
}
