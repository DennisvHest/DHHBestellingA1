package datastorage;

import domain.RestaurantOrder;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Dennis
 */
public class OrderDAO {

    public OrderDAO() {

    }

    public boolean insertOrder(RestaurantOrder order) {
        boolean result = false;
        // First open the database connection.
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            //Insert the order
            result = connection.executeSQLIUDStatement(
                    "INSERT INTO ;");

            // Finished with the connection, so close it.
            connection.closeConnection();
        }
        // else an error occurred 

        return result;
    }

    public int getAutoIncrementValue() {
        int value = 0;

        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '23ivp4a' AND TABLE_NAME   = 'restaurantorder';");

            if (resultset != null) {
                try {
                    if (resultset.next()) {
                        value = resultset.getInt("AUTO_INCREMENT");
                    }
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
        return value;
    }
}
