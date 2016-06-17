package datastorage;

import domain.BarOrder;
import domain.KitchenOrder;
import domain.RestaurantOrder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
                    "INSERT INTO restaurantorder (tableNumber, orderDate, statusId, customerId) VALUES (" + order.getTableNr() + ", NOW(), 1, 1);");

            // Finished with the connection, so close it.
            connection.closeConnection();
        }
        // else an error occurred 

        return result;
    }

    public void insertItemOrders(RestaurantOrder order) {

        // First open the database connection.
        DatabaseConnection dishConnection = new DatabaseConnection();
        if (dishConnection.openConnection()) {
            int kitchenOrderAI = getAutoIncrementValue("kitchenorder");
            int barOrderAI = getAutoIncrementValue("barorder");
            ArrayList<String> querys = new ArrayList<>();
            //If there are any kitchenorders, add them to the database
            if (!order.getKitchenOrders().isEmpty()) {
                //Insert the kitchenOrder
                querys.add("INSERT INTO `kitchenorder` (`id`, `orderDate`, `statusId`, `restaurantOrderId`) VALUES (" + kitchenOrderAI + ",NOW(), 1, " + order.getOrderNr() + ");");

                //For every kitchenOrder add the dishes to the database
                for (KitchenOrder kitchenOrder : order.getKitchenOrders()) {
                    querys.add("INSERT INTO `kitchenorder_dish` (`dishId`, `kitchenOrderId`, `quantity`) VALUES (" + kitchenOrder.getItem().getId() + ", " + kitchenOrderAI + ", " + kitchenOrder.getAmount() + ");");
                }
            }

            if (!order.getBarOrders().isEmpty()) {
                querys.add("INSERT INTO `barorder` (`id`, `orderDate`, `statusId`, `restaurantOrderId`) VALUES (" + barOrderAI + ",NOW(), 1, " + order.getOrderNr() + ");");

                //For every barOrder add the drinks to the database
                for (BarOrder barOrder : order.getBarOrders()) {
                    querys.add("INSERT INTO `barorder_drink` (`barorderId`, `drinkId`, `quantity`) VALUES (" + barOrderAI + ", " + barOrder.getItem().getId() + ", " + barOrder.getAmount() + ");");
                }
            }

            dishConnection.executeSQLAddOrderTransaction(querys);

            // Finished with the connection, so close it.
            dishConnection.closeConnection();
        }
        // else an error occurred 
    }

    public int getAutoIncrementValue(String tableName) {
        int value = 0;

        // First open a database connnection
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.
            ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT `AUTO_INCREMENT` FROM  INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '23ivp4a' AND TABLE_NAME   = '" + tableName + "';");

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

    public boolean updateOrderStatus(int orderNr, int status) {
        boolean result = false;

        // First open the database connection.
        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            result = connection.executeSQLIUDStatement(
                    "UPDATE restaurantorder SET statusId = " + status + " WHERE id = " + orderNr + ";");
        }

        return result;
    }
}
