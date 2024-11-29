package recipyrecommendation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class RecipeRecommendationSystem{
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/recipe_db", "root", "gaya@#1234@#")) {
            RecipeService recipeService = new RecipeService(connection);
            try (Scanner scanner = new Scanner(System.in)) {
				
				System.out.println("Enter the number of ingredients you have:");
				int ingredientCount = scanner.nextInt();
				scanner.nextLine();
				List<Ingredient> userIngredients = new ArrayList<>();
				for (int i = 0; i < ingredientCount; i++) {
				    System.out.println("Enter ingredient name:");
				    String name = scanner.nextLine();
				    System.out.println("Enter ingredient quantity:");
				    double quantity = scanner.nextDouble();
				    scanner.nextLine(); 
				    userIngredients.add(new Ingredient(name, quantity));
				}

				
				try {
				    List<Recipe> recipes = recipeService.findRecipes(userIngredients);
				    System.out.println("Recommended Recipes:");
				    for (Recipe recipe : recipes) {
				        System.out.println(recipe);
				    }
				} catch (IngredientNotAvailableException e) {
				    System.out.println(e.getMessage());
				}
			}
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}


class Recipe {
    private int id;
    private String name;
    private String instructions;

    public Recipe(int id, String name, String instructions) {
        this.id = id;
        this.name = name;
        this.instructions = instructions;
    }

    @Override
    public String toString() {
        return "Recipe ID: " + id + ", Name: " + name + ", Instructions: " + instructions;
    }
}


class Ingredient {
    private String name;
    private double quantity;

    public Ingredient(String name, double quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public double getQuantity() {
        return quantity;
    }
}

class IngredientNotAvailableException extends Exception {
   
	
	public IngredientNotAvailableException(String message) {
        super(message);
    }
}

interface Recommendable {
    List<Recipe> findRecipes(List<Ingredient> userIngredients) throws IngredientNotAvailableException;
}

class RecipeService implements Recommendable {
    private Connection connection;

    public RecipeService(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Recipe> findRecipes(List<Ingredient> userIngredients) throws IngredientNotAvailableException {
        List<Recipe> recommendedRecipes = new ArrayList<>();
        StringBuilder ingredientNames = new StringBuilder();

        for (Ingredient ingredient : userIngredients) {
            ingredientNames.append("'").append(ingredient.getName()).append("', ");
        }

        if (ingredientNames.length() > 0) {
            ingredientNames.setLength(ingredientNames.length() - 2); // Remove last comma and space
        }

        String query = "SELECT DISTINCT r.recipe_id, r.name, r.instructions " +
                       "FROM Recipe r " +
                       "JOIN RecipeIngredient ri ON r.recipe_id = ri.recipe_id " +
                       "JOIN Ingredient i ON ri.ingredient_id = i.ingredient_id " +
                       "WHERE i.name IN (" + ingredientNames + ") " +
                       "GROUP BY r.recipe_id";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int recipeId = rs.getInt("recipe_id");
                String recipeName = rs.getString("name");
                String instructions = rs.getString("instructions");

                Recipe recipe = new Recipe(recipeId, recipeName, instructions);
                recommendedRecipes.add(recipe);
                logRecommendation(recipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (recommendedRecipes.isEmpty()) {
            throw new IngredientNotAvailableException("No recipes found with the provided ingredients.");
        }

        return recommendedRecipes;
    }

        private void logRecommendation(Recipe recipe) {
        System.out.println("Logging recommendation: " + recipe);
    }
}
