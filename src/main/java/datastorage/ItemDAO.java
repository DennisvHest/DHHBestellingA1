package datastorage;

import domain.Dish;
import domain.Drink;
import domain.Ingredient;
import domain.Item;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Dennis
 */
public class ItemDAO {

    public ItemDAO() {

    }

    //Finds all dishes and drinks in the database and creates the Dish- and Drink-objects
    public ArrayList<Item> findMenuItems() {
        ArrayList<Item> dbItems = new ArrayList<>();

        // First open a database connnection
        DatabaseConnection dishConnection = new DatabaseConnection();
        if (dishConnection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = dishConnection.executeSQLSelectStatement(
                    "SELECT * FROM bestelling_getalldishes;");

            if (resultset != null) {
                try {
                    // The membershipnumber for a member is unique, so in case the
                    // resultset does contain data, we need its first entry.
                    
                    int prevId = 0;
                    Dish prevDish = null;
                    while (resultset.next()) {
                        
                        if (prevId == resultset.getInt("id")) {
                            prevDish.addIngredient(new Ingredient(resultset.getString("ingredientName")));
                        } else {
                            int id = resultset.getInt("id");
                        prevId = id;
                        String nameDish = resultset.getString("dishName");
                        String sortDish = resultset.getString("categoryName");
                        String descriptionDish = resultset.getString("description");
                        double priceDish = resultset.getDouble("price");
                        String imageURLString = resultset.getString("imageUrl");
                        String ingredientName = resultset.getString("ingredientName");
                        
                        URL imageURL = null;
                        BufferedImage image = null;
                        try {
                            imageURL = new URL(imageURLString);
                            image = ImageIO.read(imageURL);
                        } catch (MalformedURLException ex) {
                            System.err.println("Not a valid URL: " + ex);
                        } catch (IOException ex) {
                            System.err.println("URL naar image failed: " + ex);
                        }
                        
                        

                        prevDish = new Dish(id, nameDish, sortDish, descriptionDish, priceDish, image);
                        prevDish.addIngredient(new Ingredient(ingredientName));
                        dbItems.add(prevDish);
                        }
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }

            // We had a database connection opened. Since we're finished,
            // we need to close it.
            dishConnection.closeConnection();
        }

        // First open a database connnection
        DatabaseConnection drinkConnection = new DatabaseConnection();
        if (drinkConnection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = drinkConnection.executeSQLSelectStatement(
                    "SELECT * FROM drink;");

            if (resultset != null) {
                try {
                    // The membershipnumber for a member is unique, so in case the
                    // resultset does contain data, we need its first entry.
                    while (resultset.next()) {

//                        ResultSet resultsetIngredient = connection.executeSQLSelectStatement(
//                                "SELECT ingredientName FROM dish JOIN dish_ingredient ON dish.id = dish_ingredient.dishId JOIN ingredient ON dish_ingredient.ingredientId = ingredient.id WHERE dish.id = " + resultset.getInt("id") + ";");
                        int id = resultset.getInt("id");
                        String nameDrink = resultset.getString("drinkName");
                        double priceDrink = resultset.getDouble("price");
                        String imageURLString = resultset.getString("imageUrl");
                        URL imageURL = null;
                        BufferedImage image = null;
                        try {
                            imageURL = new URL(imageURLString);
                            image = ImageIO.read(imageURL);
                        } catch (MalformedURLException ex) {
                            System.err.println("Not a valid URL: " + ex);
                        } catch (IOException ex) {
                            System.err.println("URL naar image failed: " + ex);
                        }

                        Item dbDrink = new Drink(id, nameDrink, priceDrink, image);

                        dbItems.add(dbDrink);
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            // else an error occurred leave 'member' to null.

            // We had a database connection opened. Since we're finished,
            // we need to close it.
            drinkConnection.closeConnection();
        }

        return dbItems;
    }
    
    public String findAllergy(String ingredientName) {
        String allergy = "";
        
        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT allergy.allergyName " +
                    "FROM ingredient, ingredient_allergy, allergy " +
                    "WHERE ingredient.id = ingredient_allergy.ingredientId " +
                    "AND ingredient_allergy.allergyId = allergy.id " +
                    "AND ingredient.ingredientName = '" + ingredientName + "';");
            
            try {
                if (resultset.next()) {
                    allergy = resultset.getString("allergyName");
                }
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        }
        
        return allergy;
    }
}
