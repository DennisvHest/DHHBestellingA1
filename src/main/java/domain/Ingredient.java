package domain;

import manager.ItemManager;

/**
 *
 * @author Dennis
 */
public class Ingredient {
    private String ingredientName;
    private ItemManager itemManager;
    private String allergy;
    
    public Ingredient(String name) {
        ingredientName = name;
        
        itemManager = new ItemManager();
        
        allergy = itemManager.findAllergy(ingredientName);
    }
}
