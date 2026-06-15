package com.brookenovosad.recipe_saver.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.brookenovosad.recipe_saver.service.CreateRecipe;
import com.brookenovosad.recipe_saver.model.Recipe;

@RestController
public class RecipeSaverContoller {

    private final CreateRecipe createRecipe;

    public RecipeSaverContoller(CreateRecipe createRecipe) {
        this.createRecipe = createRecipe;
    }

    @GetMapping(value = "/createRecipe")
    public ResponseEntity<Recipe> createRecipe(@RequestParam String InstagramUrl, @RequestParam String recipeTitle) throws InterruptedException {
        Recipe recipe = createRecipe.createRecipe(InstagramUrl, recipeTitle);
        if (recipe == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        System.out.println("Recipe title is: " + recipe.getTitle() + "\n Recipe Ingredients are: " + recipe.getIngredients() + "\n Recipe Instructions are: " + recipe.getInstructions());
        return ResponseEntity.ok(recipe);
    }   
}
