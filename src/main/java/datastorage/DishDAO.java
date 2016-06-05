package datastorage;

import domain.Dish;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Dennis
 */
public class DishDAO {

    public DishDAO() {
        
    }
    
    //Finds all dishes and drinks in the database and creates the Dish- and Drink-objects
    public ArrayList<Dish> findMenuItems() {
        ArrayList<Dish> dbDishes = new ArrayList<>();
        
        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if(connection.openConnection())
        {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = connection.executeSQLSelectStatement(
                "SELECT * FROM dish join category_dish on dish.id = category_dish.dishId join category on category_dish.categoryId = category.id;");

            if(resultset != null)
            {
                try
                {
                    // The membershipnumber for a member is unique, so in case the
                    // resultset does contain data, we need its first entry.
                    if(resultset.next())
                    {
                        String nameDish = resultset.getString("dishName");
                        String sortDish = resultset.getString("categoryName");
                        String descriptionDish = resultset.getString("description");
                        double priceDish = resultset.getDouble("price");
                        
                        dbDishes.add(new Dish(nameDish, sortDish, descriptionDish, priceDish));
                    }
                }
                catch(SQLException e)
                {
                    System.out.println(e);
                }
            }
            // else an error occurred leave 'member' to null.
            
            // We had a database connection opened. Since we're finished,
            // we need to close it.
            connection.closeConnection();
        }
        
        return dbDishes;
    }
}
