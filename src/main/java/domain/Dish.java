package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathijs, Dennis
 */
public class Dish extends Item{

    private String sortDish;
    private String descriptionDish;
    private List<Ingredient> ingredients;

    public Dish(int id, String name, String sortDish, String descriptionDish, double price) {
        super(id, name, price);
        this.sortDish = sortDish;
        this.descriptionDish = descriptionDish;
        
        ingredients = new ArrayList<>();
    }
    
    public String getSortDish() {
        return sortDish;
    }

    public String getDescriptionDish() {
        return descriptionDish;
    }
    
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }
}
