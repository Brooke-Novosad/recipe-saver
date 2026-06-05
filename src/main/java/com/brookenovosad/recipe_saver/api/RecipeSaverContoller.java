package com.brookenovosad.recipe_saver.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.brookenovosad.recipe_saver.service.CreateRecipe;

@RestController
public class RecipeSaverContoller {

    private final CreateRecipe createRecipe;

    public RecipeSaverContoller(CreateRecipe createRecipe) {
        this.createRecipe = createRecipe;
    }

    @GetMapping(value = "/createRecipe")
    public String createRecipe(@RequestParam String InstagramUrl) throws InterruptedException {
        return createRecipe.createRecipe(InstagramUrl);
    }
    
}
