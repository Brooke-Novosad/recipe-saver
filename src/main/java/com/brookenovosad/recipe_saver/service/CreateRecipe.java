package com.brookenovosad.recipe_saver.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.interactions.Actions;

@Service
public class CreateRecipe {
    private WebDriver driver;

    @Value("${instagram.username}")
    private String username;

    @Value("${instagram.password}")
    private String password;

    public String createRecipe(String InstagramUrl) throws InterruptedException {
        if (InstagramUrl == null || InstagramUrl.isBlank()) {
            throw new IllegalArgumentException("InstagramUrl must not be empty");
        }

        driver = new ChromeDriver();
        try {
            driver.get(InstagramUrl);

            //<span class="x1lliihq x1plvlek xryxfnj x1n2onr6 xyejjpt x15dsfln x193iq5w xeuugli x1fj9vlw x13faqbe x1vvkbs x1s928wv xhkezso x1gmr53x x1cpjm7i x1fgarty x1943h6x x1i0vuye xvs91rp xo1l8bm x9bdzbf" dir="auto" style="--x---base-line-clamp-line-height: 18px; --x-lineHeight: 18px;">This is the end 🥲<br><br><a class="x1i10hfl xjbqb8w x1ejq31n x18oe1m7 x1sy0etr xstzfhl x972fbf x10w94by x1qhh985 x14e42zd x9f619 x1ypdohk xt0psk2 x3ct3a4 xdj266r x14z9mp xat24cr x1lziwak xexx8yu xyri2b x18d9i69 x1c1uobl x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz x1g9anri xvs91rp x1s688f x1vlc3oy  _aa9_ _a6hd" href="/explore/tags/gym/" role="link" tabindex="0">#gym</a> <a class="x1i10hfl xjbqb8w x1ejq31n x18oe1m7 x1sy0etr xstzfhl x972fbf x10w94by x1qhh985 x14e42zd x9f619 x1ypdohk xt0psk2 x3ct3a4 xdj266r x14z9mp xat24cr x1lziwak xexx8yu xyri2b x18d9i69 x1c1uobl x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz x1g9anri xvs91rp x1s688f x1vlc3oy  _aa9_ _a6hd" href="/explore/tags/training/" role="link" tabindex="0">#training</a> <a class="x1i10hfl xjbqb8w x1ejq31n x18oe1m7 x1sy0etr xstzfhl x972fbf x10w94by x1qhh985 x14e42zd x9f619 x1ypdohk xt0psk2 x3ct3a4 xdj266r x14z9mp xat24cr x1lziwak xexx8yu xyri2b x18d9i69 x1c1uobl x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz x1g9anri xvs91rp x1s688f x1vlc3oy  _aa9_ _a6hd" href="/explore/tags/boxing/" role="link" tabindex="0">#boxing</a> <a class="x1i10hfl xjbqb8w x1ejq31n x18oe1m7 x1sy0etr xstzfhl x972fbf x10w94by x1qhh985 x14e42zd x9f619 x1ypdohk xt0psk2 x3ct3a4 xdj266r x14z9mp xat24cr x1lziwak xexx8yu xyri2b x18d9i69 x1c1uobl x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz x1g9anri xvs91rp x1s688f x1vlc3oy  _aa9_ _a6hd" href="/explore/tags/funny/" role="link" tabindex="0">#funny</a></span>
            //<span class="x1lliihq x1plvlek xryxfnj x1n2onr6 xyejjpt x15dsfln x193iq5w xeuugli x1fj9vlw x13faqbe x1vvkbs x1s928wv xhkezso x1gmr53x x1cpjm7i x1fgarty x1943h6x x1i0vuye xvs91rp xo1l8bm x9bdzbf" dir="auto" style="--x---base-line-clamp-line-height: 18px; --x-lineHeight: 18px;">This is the end 🥲<br><br><a class="x1i10hfl xjbqb8w x1ejq31n x18oe1m7 x1sy0etr xstzfhl x972fbf x10w94by x1qhh985 x14e42zd x9f619 x1ypdohk xt0psk2 x3ct3a4 xdj266r x14z9mp xat24cr x1lziwak xexx8yu xyri2b x18d9i69 x1c1uobl x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz x1g9anri xvs91rp x1s688f x1vlc3oy  _aa9_ _a6hd" href="/explore/tags/gym/" role="link" tabindex="0">#gym</a> <a class="x1i10hfl xjbqb8w x1ejq31n x18oe1m7 x1sy0etr xstzfhl x972fbf x10w94by x1qhh985 x14e42zd x9f619 x1ypdohk xt0psk2 x3ct3a4 xdj266r x14z9mp xat24cr x1lziwak xexx8yu xyri2b x18d9i69 x1c1uobl x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz x1g9anri xvs91rp x1s688f x1vlc3oy  _aa9_ _a6hd" href="/explore/tags/training/" role="link" tabindex="0">#training</a> <a class="x1i10hfl xjbqb8w x1ejq31n x18oe1m7 x1sy0etr xstzfhl x972fbf x10w94by x1qhh985 x14e42zd x9f619 x1ypdohk xt0psk2 x3ct3a4 xdj266r x14z9mp xat24cr x1lziwak xexx8yu xyri2b x18d9i69 x1c1uobl x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz x1g9anri xvs91rp x1s688f x1vlc3oy  _aa9_ _a6hd" href="/explore/tags/boxing/" role="link" tabindex="0">#boxing</a> <a class="x1i10hfl xjbqb8w x1ejq31n x18oe1m7 x1sy0etr xstzfhl x972fbf x10w94by x1qhh985 x14e42zd x9f619 x1ypdohk xt0psk2 x3ct3a4 xdj266r x14z9mp xat24cr x1lziwak xexx8yu xyri2b x18d9i69 x1c1uobl x16tdsg8 x1hl2dhg xggy1nq x1a2a7pz x1g9anri xvs91rp x1s688f x1vlc3oy  _aa9_ _a6hd" href="/explore/tags/funny/" role="link" tabindex="0">#funny</a></span>
            System.out.println("Page title is: " + driver.getTitle());

            TimeUnit.SECONDS.sleep(5);

            Actions actions = new Actions(driver);
            actions.moveByOffset(1, 10).click().perform();

            // code to login, does not work
            // driver.findElement(By.name("button[type='Log in']")).click();
            // driver.findElement(By.name("input[name='username']")).clear();
            // driver.findElement(By.name("input[name='password']")).clear();
            // driver.findElement(By.name("input[name='username']")).sendKeys(username);
            // driver.findElement(By.name("input[name='password']")).sendKeys(password);
            // driver.findElement(By.name("button[type='submit']")).click();

            String[] recipeWords = {"Ingredients", "Directions", "Instructions", "ingredients"};
            String xpath = "//*[" +
                Arrays.stream(recipeWords)
                      .map(w -> "contains(normalize-space(.), '" + w + "')")
                      .collect(Collectors.joining(" or ")) +
                "]";

            List<WebElement> match = driver.findElements(By.xpath(xpath));
            
            if (!match.isEmpty()) {
                String fullText = match.get(0).getText();
                System.out.println(fullText);
            } else {
                System.out.println("sorry we can't get the recipe");
            }

        } finally {
            driver.quit();
        }

        return null;
    }
    
}
