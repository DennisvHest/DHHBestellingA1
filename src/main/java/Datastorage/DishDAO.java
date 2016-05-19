package Datastorage;

import Domain.Dish;
import java.util.ArrayList;

/**
 *
 * @author Dennis
 */
public class DishDAO
{
    private ArrayList<Dish> dishList;
    
    public DishDAO()
    {
        dishList = new ArrayList<>();
        
        //A few hardcoded dishes. Remove when connected to the database!
        Dish dish1 = new Dish("Portie Olijven", "Appetizer", "Portie Olijven", 3.00);
        dishList.add(dish1);
        Dish dish2 = new Dish("Bruschetta", "Appetizer", "Italiaans brood met tomaten, olijfolie, kruiden.", 4.40);
        dishList.add(dish2);
        Dish dish3 = new Dish("Scaloppa Gorgonzola", "MainCourse", "Plakjes varkenshaas met gorgonzola", 15.75);
        dishList.add(dish3);
        Dish dish4 = new Dish("Penne Fantasia", "MainCourse", "Tomatensaus met spek, paprika, champignons, pijnboompitten, spinazie en room", 11.50);
        dishList.add(dish4);
        Dish dish5 = new Dish("Chocolade-ijs", "Dessert", "Chocolade-ijs van verschillende soorten chocola.", 5.50);
        dishList.add(dish5);
    }
    
    public ArrayList<Dish> getDishListBySort(String sort)
    {
        ArrayList<Dish> dishes = new ArrayList<>();
        
        for(Dish dish : dishList)
        {
            if(dish.getSortDish().equals(sort))
            {
                dishes.add(dish);
            }
        }
        
        return dishes;
    }
}
