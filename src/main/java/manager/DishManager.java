package manager;

import datastorage.DishDAO;
import domain.Dish;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Dennis
 */
public class DishManager {

    private DishDAO dishDAO;
    private List<Dish> dishList;

    public DishManager() {
        dishDAO = new DishDAO();
        
        dishList = new ArrayList<>();
    }
    
    public ArrayList<Dish> getDishListBySort(String sort) {
        List<Dish> dishes = new ArrayList<>();

        for (Dish dish : dishList) {
            if (dish.getSortDish().equals(sort)) {
                dishes.add(dish);
            }
        }

        return (ArrayList<Dish>) dishes;
    }
    
    public void addDish(Dish dish) {
        dishList.add(dish);
    }
    
    public void findMenuItems() {
        for (Dish dish : dishDAO.findMenuItems()) {
            dishList.add(dish);
        }
    }
}
