package com.brookenovosad.recipe_saver.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ingredient {
    
    private String amount;
    private String ingredient;

    public Ingredient(String amount, String ingredient) {
        this.amount = amount;
        this.ingredient = ingredient;
    }
}
