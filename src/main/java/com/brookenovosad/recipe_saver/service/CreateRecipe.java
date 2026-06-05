package com.brookenovosad.recipe_saver.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
public class CreateRecipe {
    private WebDriver driver;

    public String createRecipe(String InstagramUrl) throws InterruptedException {
        if (InstagramUrl == null || InstagramUrl.isBlank()) {
            throw new IllegalArgumentException("InstagramUrl must not be empty");
        }

        driver = new ChromeDriver();
        try {
            driver.get(InstagramUrl);

            System.out.println("Page title is: " + driver.getTitle());

            TimeUnit.SECONDS.sleep(10);
        } finally {
            driver.quit();
        }

        return null;
    }
    
}
