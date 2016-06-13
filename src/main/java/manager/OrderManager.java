package manager;

import datastorage.OrderDAO;
import domain.BarOrder;
import domain.ItemOrder;
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
                for (KitchenOrder kitchenOrder : order.getKitchenOrders()) {
                    buffer.append(kitchenOrder.getItem().getName());
                    buffer.append("   ");
                    buffer.append(kitchenOrder.getAmount());
                    buffer.append("\n");
                }
                
                for (BarOrder barOrder : order.getBarOrders()) {
                    buffer.append(barOrder.getItem().getName());
                    buffer.append("   ");
                    buffer.append(barOrder.getAmount());
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
    
    public double getUnpaidTotal() {
        //Calculate the total price of every unpaid order
        double total = 0.00;
        
        for (RestaurantOrder order : orders) {
            if (!"payed".equals(order.getOrderStatus()) || !"waitingForPayment".equals(order.getOrderStatus())) {
                
                //Go through all the KitchenOrders
                for (KitchenOrder kitchenOrder : order.getKitchenOrders()) {
                    total += kitchenOrder.getItem().getPrice() * kitchenOrder.getAmount();
                }
                
                //Go through all the DrinkOrders
                for (BarOrder drinkOrder : order.getBarOrders()) {
                    total += drinkOrder.getItem().getPrice() * drinkOrder.getAmount();
                }
            }
        }
        
        return total;
    }
    
    public void payUnpaidOrders() {
        for (RestaurantOrder order : orders) {
            if (!"payed".equals(order.getOrderStatus()) || !"waitingForPayment".equals(order.getOrderStatus())) {
                orderDAO.updateOrderStatus(order.getOrderNr(), 5);
            }
        }
    }
    
    public int getAutoIncrementValue(String table) {
        return orderDAO.getAutoIncrementValue(table);
    }
}
