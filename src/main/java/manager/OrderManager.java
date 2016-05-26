package manager;

import domain.KitchenOrder;
import domain.RestaurantOrder;
import java.util.ArrayList;

/**
 *
 * @author Dennis
 */
public class OrderManager {

    private ArrayList<RestaurantOrder> orders;

    public OrderManager() {
        orders = new ArrayList<>();
    }

    public void addOrder(RestaurantOrder order) {
        orders.add(order);
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
        
        for (RestaurantOrder order : orders)
        {
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
    
    public RestaurantOrder getPlacedOrder() {
        //Return the pending order
        RestaurantOrder returnOrder = null;

        for (RestaurantOrder order : orders) {
            if (!"pending".equals(order.getOrderStatus())) {
                returnOrder = order;
            }
        }

        return returnOrder;
    }
}
