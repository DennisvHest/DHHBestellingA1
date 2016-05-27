package domain;

public class KitchenOrder {

    private int kitchenOrderId;
    private Dish dish;
    private int dishAmount;
    private String statusDish;
    private double totalPriceDish;

    public KitchenOrder(int dishOrderNr, Dish dish, int dishAmount) {
        this.kitchenOrderId = dishOrderNr;
        this.dish = dish;
        this.dishAmount = dishAmount;
        
        statusDish = "pending";
        totalPriceDish = dish.getpriceDish() * dishAmount;
    }
    
    public int getDishAmount() {
        return dishAmount;
    }
    
    public void setDishAmount(int dishAmount) {
        this.dishAmount = dishAmount;
    }
    
    public Dish getDish() {
        return dish;
    }
}
