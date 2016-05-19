package Domain;

/**
 *
 * @author Mathijs, Dennis
 */
public class Dish
{
    private String nameDish;
    private String sortDish;
    private String descriptionDish;
    private double priceDish;
    
    public Dish(String nameDish, String sortDish, String descriptionDish, double priceDish)
    {
        this.nameDish = nameDish;
        this.sortDish = sortDish;
        this.descriptionDish = descriptionDish;
        this.priceDish = priceDish;
    }
    
    //Getters
    public String getNameDish()
    {
        return nameDish;
    }
    
    public String getSortDish()
    {
        return sortDish;
    }
    
    public String getDescriptionDish()
    {
        return descriptionDish;
    }
    
    public double getpriceDish()
    {
        return priceDish;
    }
}
