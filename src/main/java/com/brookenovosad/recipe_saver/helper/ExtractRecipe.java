package com.brookenovosad.recipe_saver.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.brookenovosad.recipe_saver.model.Ingredient;
import com.brookenovosad.recipe_saver.model.Recipe;

public class ExtractRecipe {
    private enum Section {
        NONE,
        INGREDIENTS,
        INSTRUCTIONS
    }

    private enum LineType {
        NEUTRAL,
        INGREDIENT,
        INSTRUCTION
    }

    public static Recipe extractRecipe(String caption, String instagramUrl) {
        String normalized = caption.replace("\r", "").trim();

        Set<String> ingredientHeaders = Set.of("ingredients", "ingredient", "ingredients:", "ingredient:");
        Set<String> instructionHeaders = Set.of("directions", "instructions", "method", "how to", "steps", "how:", "method:");
        Set<String> stopWords = Set.of("api", "privacy", "reply");

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

        Boolean ingredientsDone = false;
        Boolean ingredientHeader = false;
        Boolean instructionHeader = false;

        for (String line : lines) {
            String lowerLine = line.toLowerCase();

            if (!ingredientHeader && isHeaderLine(lowerLine, ingredientHeaders)) {
                section = Section.INGREDIENTS;
                ingredientHeader = true;
                continue;
            }
            if (!instructionHeader && isHeaderLine(lowerLine, instructionHeaders)) {
                section = Section.INSTRUCTIONS;
                instructionHeader = true;
                ingredientsDone = true;
                continue;
            }
            if (stopWords.stream().anyMatch(lowerLine::contains)) {
                System.out.println("Stopping parsing at line: " + line);
                break;
            }

            LineType lineType = classifyLine(lowerLine);

            if (section == Section.INGREDIENTS && lineType == LineType.INGREDIENT) {
                ingredients.add(line);
                continue;
            }
            if (section == Section.INGREDIENTS && lineType == LineType.INSTRUCTION) {
                section = Section.INSTRUCTIONS;
                instructions.add(line);
                continue;
            }
            if (section == Section.INSTRUCTIONS && lineType == LineType.INSTRUCTION) {
                instructions.add(line);
                continue;
            }
            if (isLikelySectionHeader(lowerLine)) {
                section = Section.NONE;
                continue;
            }
            if (lineType == LineType.INGREDIENT && !ingredientsDone) {
                section = Section.INGREDIENTS;
                ingredients.add(line);
            } else if (lineType == LineType.INSTRUCTION) {
                section = Section.INSTRUCTIONS;
                instructions.add(line);
            }
        }
        List<Ingredient> ing = new ArrayList<>();
        for (String i : ingredients) {
            ing.add(extractIngredients(i));
        }
        
        //make user input the title
        return new Recipe("", ing, instructions, instagramUrl);
    }

    private static boolean isIngredientLine(String text) {
        // Exclude numbered steps (e.g., "1. Saute...", "2. Add...")
        // Exclude timestamps and UI noise (e.g., "14h", "3d")
        if (text.matches("^\\d+\\.\\s+.*") || text.matches("^\\d+[hd]$") || isNonRecipeNoise(text) || text.matches(".*\\b(ingredients?)\\b.*")) {
            return false;
        }

        // Match quantity + unit (existing behavior)
        if (text.matches(".*\\b(?:\\d+\\.\\d+|\\d+\\/\\d+|\\d+)\\s*(cup|cups|tablespoon|tablespoons|tbsp|teaspoon|teaspoons|tsp|gram|grams|g|kg|oz|ounce|ounces|ml|l|pinch|dash|slice|slices|piece|pieces|can|package|pkg|stick|clove|cloves|bunch|sprig)\\b.*") || (text.contains("optional:") && text.matches(".*\\b(?:\\d+\\.\\d+|\\d+\\/\\d+|\\d+)\\b.*")) || text.matches(".*\\b(?:to taste|optional|fresh|dried|ground|minced|chopped|sliced|grated|shredded|rinsed|drained|pinch)\\b.*")) {
            return true;
        }

        // Match unitless quantity like "2 egg" or "1 egg" or "2 eggs"
        if (text.matches(".*\\b(?:\\d+\\.\\d+|\\d+\\/\\d+|\\d+)\\s+[a-zA-Z]{1,20}(?:\\s+[a-zA-Z]{1,20}){0,2}\\b.*")) {
            // Exclude obvious time phrases like "5 days ago" or "3 hours ago"
            if (text.matches(".*\\b(days?|day|hours?|hrs?|minutes?|mins?|ago|h)\\b.*")) {
                return false;
            }
            return true;
        }
        return false;
    }

    private static boolean isInstructionLine(String text) {  
        System.out.println("checking: "+ text);
        if (isNonRecipeNoise(text)) {
            return false;
        }
        // Check if it's likely a step (has text after the number)
        return text.matches("^\\d+[.\\):\\-]\\s+.*") || text.matches(".*\\b(step|first|then|next|finally|after|while|when)\\b.*") ||
                text.matches(".*\\b(mix|stir|cook|bake|combine|heat|whisk|simmer|fold|serve|chop|pour|blend|preheat|sauté|saute|deglaze|cover|boil|return|garnish|let|place|transfer|drain|rinse|slice|dice|mince|toss|season|taste|prepare|make|set|keep|bring|soften|saute|sauté)\\b.*");
    }
    
    private static boolean isNonRecipeNoise(String text) { 
        // Check if line is only a timestamp or UI element
        return text.matches("^\\d+[hd]$") || text.matches(".*\\b(sign up|follow|reply|like|share|comment).*") || text.startsWith("#");
    }

    private static boolean isHeaderLine(String lowerLine, Set<String> headers) {
        for (String h : headers) {
            if (lowerLine.equals(h)) return true;
            if (lowerLine.startsWith(h + ":") || lowerLine.startsWith(h + " -") || lowerLine.startsWith(h + " —") || lowerLine.startsWith(h + " –")) return true;
        }
        return false;
    }

    private static LineType classifyLine(String text) {
        if (isIngredientLine(text)) {
            return LineType.INGREDIENT;
        }
        if (isInstructionLine(text)) {
            return LineType.INSTRUCTION;
        }
        return LineType.NEUTRAL;
    }

    
    private static boolean isLikelySectionHeader(String text) {
        String lower = text.toLowerCase();
        return lower.matches("^(ingredients|directions|instructions|method|steps|how to).*" );
    }

    private static Ingredient extractIngredients(String ingredients) {
        String REGEX = "^([\\d\\s\\/\\.\\-]+)?\\b(cups?|tbsp|tablespoons?|tsps?|teaspoons?|grams?|oz|ounces?|pinches?|pinch|stalks?|cloves?|dash(es)?|cans?)\\b\\s*(.*)$";
        Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

        Matcher matcher = PATTERN.matcher(ingredients.trim());
        String amount = "";
        String ingredient = "";

        if (matcher.find()) {
            String number = matcher.group(1) != null ? matcher.group(1).trim() : "";
            String unit = matcher.group(2).trim();
            
            amount = (number + " " + unit).trim();
            ingredient = matcher.group(4).trim().replaceAll("^(of\\s+)", "");
        } else {
            ingredient = ingredients;
        }
        return new Ingredient(amount, ingredient);
    }
}
