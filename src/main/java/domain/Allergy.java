package domain;

/**
 *
 * @author Dennis
 */
public class Allergy {
    private String allergyName;
    private String description;
    private Ingredient ingredient;
    
    public Allergy(String name, String description, Ingredient ingredient) {
        allergyName = name;
        this.description = description;
        this.ingredient = ingredient;
    }
    
}
