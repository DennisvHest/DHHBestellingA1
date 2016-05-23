package domain;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Dennis
 */
public class Order {

    private int orderNr;
    private Date orderDate;
    private boolean payed;
    private String orderStatus;
    private int tableNr;
    private ArrayList<DishOrder> dishOrders;
    //private ArrayList<DrinkOrder> drinkOrders;

    public Order(int tableNr) {
        this.tableNr = tableNr;

        payed = false;
        orderStatus = "pending";
        
        dishOrders = new ArrayList<>();
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

    public ArrayList<DishOrder> getDishOrders() {
        return dishOrders;
    }
    
    public void addDishOrder(DishOrder order) {
        dishOrders.add(order);
    }
}
