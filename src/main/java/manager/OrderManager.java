package manager;

import domain.DishOrder;
import domain.Order;
import java.util.ArrayList;

/**
 *
 * @author Dennis
 */
public class OrderManager {

    private ArrayList<Order> orders;

    public OrderManager() {
        orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public boolean pendingOrderExist() {
        //Check if there is a pending order
        boolean exists = false;

        for (Order order : orders) {
            if ("pending".equals(order.getOrderStatus())) {
                exists = true;
            }
        }

        return exists;
    }

    public Order getPendingOrder() {
        //Return the pending order
        Order returnOrder = null;

        for (Order order : orders) {
            if ("pending".equals(order.getOrderStatus())) {
                returnOrder = order;
            }
        }

        return returnOrder;
    }
    
    public String printPendingOrders() {
        StringBuffer buffer = new StringBuffer();
        
        for (Order order : orders)
        {
            if ("pending".equals(order.getOrderStatus())) {
                for (DishOrder dishOrder : order.getDishOrders()) {
                    buffer.append(dishOrder.getDish().getNameDish());
                    buffer.append("\n");
                }
            }
        }
        
        return buffer.toString();
    }
}
