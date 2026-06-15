package com.brookenovosad.recipe_saver.service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.brookenovosad.recipe_saver.helper.ExtractRecipe;
import com.brookenovosad.recipe_saver.model.Recipe;

@Service
public class CreateRecipe {
    private WebDriver driver;

    @Value("${instagram.username}")
    private String username;

    @Value("${instagram.password}")
    private String password;

    public Recipe createRecipe(String InstagramUrl, String recipeTitle) throws InterruptedException {
        if (InstagramUrl == null || InstagramUrl.isBlank()) {
            throw new IllegalArgumentException("InstagramUrl must not be empty");
        }
        Recipe newRecipe = null;
        driver = new ChromeDriver();
        try {
            driver.get(InstagramUrl);
            TimeUnit.SECONDS.sleep(5);

            //click off of signin popup
            Actions actions = new Actions(driver);
            actions.moveByOffset(1, 10).click().perform();

            // code to login, does not work
            // driver.findElement(By.name("button[type='Log in']")).click();
            // driver.findElement(By.name("input[name='username']")).clear();
            // driver.findElement(By.name("input[name='password']")).clear();
            // driver.findElement(By.name("input[name='username']")).sendKeys(username);
            // driver.findElement(By.name("input[name='password']")).sendKeys(password);
            // driver.findElement(By.name("button[type='submit']")).click();

            //Match caption on key words
            String[] recipeWords = {"Ingredients", "Directions", "Instructions", "ingredients"};
            String xpath = "//*[" +
                Arrays.stream(recipeWords)
                      .map(w -> "contains(normalize-space(.), '" + w + "')")
                      .collect(Collectors.joining(" or ")) +
                "]";

            List<WebElement> match = driver.findElements(By.xpath(xpath));

            //Check if we can find recipe
            if (!match.isEmpty()) {
                String fullText = match.get(0).getText();
                System.out.println(fullText);
                newRecipe = ExtractRecipe.extractRecipe(fullText, InstagramUrl);
                newRecipe.setTitle(recipeTitle);
            } else {
                System.out.println("sorry we can't get the recipe");
            }

        } finally {
            driver.quit();
        }
        return newRecipe;
    }
}
