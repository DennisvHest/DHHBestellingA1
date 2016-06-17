package manager;

import datastorage.ItemDAO;
import domain.Dish;
import domain.Drink;
import domain.Item;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class ItemManager {

    private ItemDAO dishDAO;
    private List<Dish> dishList;
    private List<Drink> drinkList;

    public ItemManager() {
        dishDAO = new ItemDAO();
        
        dishList = new ArrayList<>();
        drinkList = new ArrayList<>();
    }
    
    /**
     * Gives an ArrayList of dishes with the given sort
     * @param sort, the type of dish
     * @return an ArrayList of dishes that are of the given sort
     */
    public ArrayList<Dish> getDishListBySort(String sort) {
        List<Dish> dishes = new ArrayList<>();

        for (Dish dish : dishList) {
            if (dish.getSortDish().equals(sort)) {
                dishes.add(dish);
            }
        }

        return (ArrayList<Dish>) dishes;
    }
    
    public ArrayList<Drink> getDrinkList() {
        List<Drink> drinks = new ArrayList<>();
        
        for (Drink drink : drinkList) {
            drinks.add(drink);
        }
        
        return (ArrayList<Drink>) drinks;
    }
    
    public void addDish(Dish dish) {
        dishList.add(dish);
    }
    
    public void findMenuItems() {
        for (Item item : dishDAO.findMenuItems()) {
            if (item instanceof Dish) {
                dishList.add((Dish) item);
            }
            if (item instanceof Drink) {
                drinkList.add((Drink) item);
            }
        }
    }
    
    public String findAllergy(String ingredientName) {
        return dishDAO.findAllergy(ingredientName);
    }
}
