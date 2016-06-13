package domain;

/**
 *
 * @author Dennis
 */
public class BarOrder extends ItemOrder{
    private Drink drink;
    
    public BarOrder(int itemId, Drink drink, int amount) {
        super(itemId, amount);
        this.drink = drink;
    }
    
    //Getters
    @Override
    public Item getItem() {
        return drink;
    }
}
