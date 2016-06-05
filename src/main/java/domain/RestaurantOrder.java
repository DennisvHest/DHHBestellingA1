package domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class RestaurantOrder {

    private int orderNr;
    private Date orderDate;
    private boolean payed;
    private String orderStatus;
    private int tableNr;
    private List<KitchenOrder> kitchenOrders;
    //private ArrayList<DrinkOrder> drinkOrders;

    public RestaurantOrder(int tableNr) {
        this.tableNr = tableNr;

        payed = false;
        orderStatus = "pending";
        
        kitchenOrders = new ArrayList<>();
    }

    //Getters
    public int getOrderNr() {
        return orderNr;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public boolean getPayed() {
        return payed;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public int getTableNr() {
        return tableNr;
    }

    public ArrayList<KitchenOrder> getKitchenOrders() {
        return (ArrayList<KitchenOrder>) kitchenOrders;
    }
    
    //Setters
    public void setOrderNr(int orderNr) {
        this.orderNr = orderNr;
    }
    
    public void setOrderStatus(String status) {
        orderStatus = status;
    }
    
    public void addKitchenOrder(KitchenOrder order) {
        kitchenOrders.add(order);
    }
    
    public void removeKitchenOrder(KitchenOrder order) {
        kitchenOrders.remove(order);
    }
}
