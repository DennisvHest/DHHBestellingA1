package Domain;

public class DishOrder
{
    private int dishOrderNr;
    private Dish dish;
    private int dishAmount;
    private boolean dishDone;
    private double totalPriceDish;
    
    public DishOrder(int dishOrderNr, Dish dish, int dishAmount)
    {
        this.dishOrderNr = dishOrderNr;
        this.dish = dish;
        this.dishAmount = dishAmount;
        
        dishDone = false;
        totalPriceDish = dish.getpriceDish() * dishAmount;
    }
}
