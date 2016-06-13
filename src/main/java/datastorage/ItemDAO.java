package datastorage;

import domain.Dish;
import domain.Ingredient;
import domain.Item;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT * FROM dish JOIN category_dish on dish.id = category_dish.dishId JOIN category on category_dish.categoryId = category.id;");
            
            if (resultset != null) {
                try {
                    // The membershipnumber for a member is unique, so in case the
                    // resultset does contain data, we need its first entry.
                    while (resultset.next()) {

//                        ResultSet resultsetIngredient = connection.executeSQLSelectStatement(
//                                "SELECT ingredientName FROM dish JOIN dish_ingredient ON dish.id = dish_ingredient.dishId JOIN ingredient ON dish_ingredient.ingredientId = ingredient.id WHERE dish.id = " + resultset.getInt("id") + ";");
                        int id = resultset.getInt("id");
                        String nameDish = resultset.getString("dishName");
                        String sortDish = resultset.getString("categoryName");
                        String descriptionDish = resultset.getString("description");
                        double priceDish = resultset.getDouble("price");
                        
                        Item dbDish = new Dish(id, nameDish, sortDish, descriptionDish, priceDish);

//                        if (resultsetIngredient != null) {
//                            while (resultsetIngredient.next()) {
//                                dbDish.addIngredient(new Ingredient(resultsetIngredient.getString("ingredientName")));
//                            }
//                        }

                        dbItems.add(dbDish);
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
            // else an error occurred leave 'member' to null.

            // We had a database connection opened. Since we're finished,
            // we need to close it.
            connection.closeConnection();
        }
        
        return dbItems;
    }
}
