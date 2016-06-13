package domain;

public class KitchenOrder extends ItemOrder{

    private Dish dish;
    private double totalPriceDish;

    public KitchenOrder(int itemId, int amount) {
        super(itemId, amount);
    }
    
    public KitchenOrder(int itemId, Dish dish, int amount) {
        super(itemId, amount);
        this.dish = dish;
    }

    //Getters
    @Override
    public Item getItem() {
        return dish;
    }
}
