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
    private String orderStatus;
    private int tableNr;
    private List<KitchenOrder> kitchenOrders;
    private List<BarOrder> barOrders;

    public RestaurantOrder(int tableNr) {
        this.tableNr = tableNr;

        orderStatus = "pending";
        
        kitchenOrders = new ArrayList<>();
        barOrders = new ArrayList<>();
    }

    //Getters
    public int getOrderNr() {
        return orderNr;
    }

    public Date getOrderDate() {
        return orderDate;
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
    
    public ArrayList<BarOrder> getBarOrders() {
        return (ArrayList<BarOrder>) barOrders;
    }
    
    public ArrayList<ItemOrder> getItemOrders() {
        List<ItemOrder> itemOrders = new ArrayList<>();
        itemOrders.addAll(kitchenOrders);
        itemOrders.addAll(barOrders);
        
        return (ArrayList<ItemOrder>) itemOrders;
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
    
    public void addBarOrder(BarOrder order) {
        barOrders.add(order);
    }
    
    public void removeItemOrder(ItemOrder order) {
        if (order instanceof KitchenOrder) {
            kitchenOrders.remove((KitchenOrder)order);
        }
        
        if (order instanceof BarOrder) {
            barOrders.remove((BarOrder) order);
        }
    }
}
