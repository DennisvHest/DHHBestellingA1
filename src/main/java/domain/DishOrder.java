package domain;

public class DishOrder {

    private int dishOrderNr;
    private Dish dish;
    private int dishAmount;
    private String statusDish;
    private boolean dishDone;
    private double totalPriceDish;

    public DishOrder(int dishOrderNr, Dish dish, int dishAmount) {
        this.dishOrderNr = dishOrderNr;
        this.dish = dish;
        this.dishAmount = dishAmount;
        
        statusDish = "pending";
        dishDone = false;
        totalPriceDish = dish.getpriceDish() * dishAmount;
    }
    
    public Dish getDish() {
        return dish;
    }
}
