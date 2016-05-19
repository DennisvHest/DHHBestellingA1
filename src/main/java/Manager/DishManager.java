package Manager;

import Datastorage.DishDAO;
import Domain.Dish;
import java.util.ArrayList;

/**
 *
 * @author Dennis
 */
public class DishManager
{
    private DishDAO dishDAO;
    
    public DishManager()
    {
        dishDAO = new DishDAO();
    }
    
    public ArrayList<Dish> findDishes(String sort)
    {
        return dishDAO.getDishListBySort(sort);
    }
}
