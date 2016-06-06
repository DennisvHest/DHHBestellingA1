package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Mathijs, Dennis
 */
public class Dish {

    private int id;
    private String nameDish;
    private String sortDish;
    private String descriptionDish;
    private double priceDish;
    private List<Ingredient> ingredients;

    public Dish(int id, String nameDish, String sortDish, String descriptionDish, double priceDish) {
        this.id = id;
        this.nameDish = nameDish;
        this.sortDish = sortDish;
        this.descriptionDish = descriptionDish;
        this.priceDish = priceDish;
        
        ingredients = new ArrayList<>();
    }

    //Getters
    public int getId() {
        return id;
    }
    
    public String getNameDish() {
        return nameDish;
    }

    public String getSortDish() {
        return sortDish;
    }

    public String getDescriptionDish() {
        return descriptionDish;
    }

    public double getpriceDish() {
        return priceDish;
    }
    
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }
}
