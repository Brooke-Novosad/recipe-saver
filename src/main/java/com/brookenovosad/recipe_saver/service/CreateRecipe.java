package com.brookenovosad.recipe_saver.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.By;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.openqa.selenium.interactions.Actions;

import java.util.concurrent.TimeUnit;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.Set;
import com.brookenovosad.recipe_saver.model.Recipe;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.util.CoreMap;

@Service
public class CreateRecipe {
    private WebDriver driver;

    private enum Section {
        NONE,
        INGREDIENTS,
        INSTRUCTIONS
    }

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
                Recipe newRecipe = extractRecipe(fullText);
            } else {
                System.out.println("sorry we can't get the recipe");
            }

        } finally {
            driver.quit();
        }

        return null;
    }


    private Recipe extractRecipe(String caption) {
        String normalized = caption.replace("\r", "").trim();

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        Set<String> ingredientHeaders = Set.of("ingredients", "ingredient", "ingredients:", "ingredient:");
        Set<String> instructionHeaders = Set.of("directions", "instructions", "method", "how to", "steps", "how:");
        Set<String> stopWords = Set.of("reply", "api", "privacy");

        List<String> ingredients = new ArrayList<>();
        List<String> instructions = new ArrayList<>();

        Section section = Section.NONE;

        List<String> lines = Arrays.stream(normalized.split("\n"))
            .map(String::trim)
            .filter(line -> !line.isBlank())
            .collect(Collectors.toList());

        if (lines.isEmpty()) {
            System.out.println("No lines found for extraction.");
            return null;
        }

        for (String line : lines) {
            String lowerLine = line.toLowerCase();
            if (ingredientHeaders.stream().anyMatch(lowerLine::contains)) {
                section = Section.INGREDIENTS;
                continue;
            }
            if (instructionHeaders.stream().anyMatch(lowerLine::contains)) {
                section = Section.INSTRUCTIONS;
                continue;
            }
            if (stopWords.stream().anyMatch(lowerLine::contains)) {
                System.out.println("Stopping parsing at line: " + line);
                break;
            }

            boolean ingredientCandidate = isIngredientLine(pipeline, line);
            boolean instructionCandidate = isInstructionLine(pipeline, line);

            if (section == Section.INGREDIENTS) {
                if (ingredientCandidate) {
                    ingredients.add(line);
                    continue;
                }
                if (instructionCandidate && !isLikelySectionHeader(line)) {
                    section = Section.INSTRUCTIONS;
                    instructions.add(line);
                    continue;
                }
                if (isLikelySectionHeader(line)) {
                    section = Section.NONE;
                    continue;
                }
            }

            if (section == Section.INSTRUCTIONS) {
                if (instructionCandidate) {
                    instructions.add(line);
                    continue;
                }
                if (isLikelySectionHeader(line)) {
                    section = Section.NONE;
                    continue;
                }
            }

            if (section == Section.NONE) {
                if (ingredientCandidate) {
                    ingredients.add(line);
                    continue;
                }
                if (instructionCandidate) {
                    instructions.add(line);
                }
            }
        }

        System.out.println("--- Extracted Ingredients ---");
        ingredients.forEach(System.out::println);
        System.out.println("--- Extracted Instructions ---");
        instructions.forEach(System.out::println);
        return null;
    }

    private boolean isIngredientLine(StanfordCoreNLP pipeline, String text) {
        String lower = text.toLowerCase();
        if (lower.matches(".*\\b(?:\\d+\\.\\d+|\\d+\\/\\d+|\\d+)\\s*(cup|cups|tablespoon|tablespoons|tbsp|teaspoon|teaspoons|tsp|gram|grams|kg|oz|ounce|ounces|ml|l|pinch|dash|slice|slices|piece|pieces|can|package|pkg|stick|clove|cloves|bunch|sprig)\\b.*")) {
            return true;
        }
        if (lower.contains("optional:") && lower.matches(".*\\b(?:\\d+\\.\\d+|\\d+\\/\\d+|\\d+)\\b.*")) {
            return true;
        }
        if (lower.matches(".*\\b(\bto taste\b|optional|fresh|dried|ground|minced|chopped|sliced|grated|shredded|rinsed|drained|pinch)\\b.*")) {
            return true;
        }
        if (lower.matches(".*\\b(ingredients?)\\b.*")) {
            return false;
        }

        Annotation lineAnnotation = new Annotation(text);
        pipeline.annotate(lineAnnotation);
        List<CoreLabel> tokens = lineAnnotation.get(CoreAnnotations.TokensAnnotation.class);
        if (tokens != null) {
            for (CoreLabel token : tokens) {
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                if ("CD".equals(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInstructionLine(StanfordCoreNLP pipeline, String text) {
        String lower = text.toLowerCase();
        if (lower.matches("^(step\\s*\\d+|\\d+\\.|first|then|next|finally|after that|while|when|when the|when ready)\\b.*")) {
            return true;
        }
        if (lower.matches(".*\\b(mix|stir|cook|bake|combine|heat|whisk|simmer|fold|serve|chop|add|pour|blend|preheat|sauté|saute|deglaze|cover|boil|simmer|return|stirring|garnish|let)\\b.*")) {
            return true;
        }

        Annotation lineAnnotation = new Annotation(text);
        pipeline.annotate(lineAnnotation);
        List<CoreLabel> tokens = lineAnnotation.get(CoreAnnotations.TokensAnnotation.class);
        if (tokens != null && !tokens.isEmpty()) {
            String firstPos = tokens.get(0).get(CoreAnnotations.PartOfSpeechAnnotation.class);
            if (firstPos != null && firstPos.startsWith("VB")) {
                return true;
            }
        }
        return false;
    }

    private boolean isLikelySectionHeader(String text) {
        String lower = text.toLowerCase();
        return lower.matches("^(ingredients|directions|instructions|method|steps|how to).*" );
    }
}
