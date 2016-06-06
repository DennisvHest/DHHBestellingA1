package manager;

import datastorage.OrderDAO;
import domain.KitchenOrder;
import domain.RestaurantOrder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class OrderManager {

    private List<RestaurantOrder> orders;
    private OrderDAO orderDAO;

    public OrderManager() {
        orderDAO = new OrderDAO();
        orders = new ArrayList<>();
    }

    public void addOrder(RestaurantOrder order) {
        orders.add(order);
    }

    public ArrayList<RestaurantOrder> getOrders() {
        return (ArrayList<RestaurantOrder>) orders;
    }

    public boolean pendingOrderExist() {
        //Check if there is a pending order
        boolean exists = false;

        for (RestaurantOrder order : orders) {
            if ("pending".equals(order.getOrderStatus())) {
                exists = true;
            }
        }

        return exists;
    }

    public RestaurantOrder getPendingOrder() {
        //Return the pending order
        RestaurantOrder returnOrder = null;

        for (RestaurantOrder order : orders) {
            if ("pending".equals(order.getOrderStatus())) {
                returnOrder = order;
            }
        }

        return returnOrder;
    }

    public String printPendingOrders() {
        StringBuffer buffer = new StringBuffer();

        for (RestaurantOrder order : orders) {
            if ("pending".equals(order.getOrderStatus())) {
                for (KitchenOrder dishOrder : order.getKitchenOrders()) {
                    buffer.append(dishOrder.getDish().getNameDish());
                    buffer.append("\n");
                }
            }
        }

        return buffer.toString();
    }

    public boolean placedOrderExist() {
        //Check if there is a placed order
        boolean exists = false;

        for (RestaurantOrder order : orders) {
            if (!"pending".equals(order.getOrderStatus())) {
                exists = true;
            }
        }

        return exists;
    }

    public ArrayList<RestaurantOrder> getPlacedOrders() {
        //Return the pending order
        List placedOrders = new ArrayList<RestaurantOrder>();

        for (RestaurantOrder order : orders) {
            if (!"pending".equals(order.getOrderStatus())) {
                placedOrders.add(order);
            }
        }

        return (ArrayList<RestaurantOrder>) placedOrders;
    }
    
    public void insertRestaurantOrder() {
        RestaurantOrder pendingOrder = getPendingOrder();
        
        orderDAO.insertOrder(pendingOrder);
    }
    
    public void insertItemOrder() {
        orderDAO.insertItemOrders(getPendingOrder());
    }
    
    public int getAutoIncrementValue() {
        return orderDAO.getAutoIncrementValue("restaurantorder");
    }
}
