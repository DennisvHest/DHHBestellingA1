package domain;

import java.awt.image.BufferedImage;
import java.net.URL;

/**
 *
 * @author Dennis
 */
public class Item {
    private int id;
    private String name;
    private double price;
    private BufferedImage image;
    
    public Item(int id, String name, double price, BufferedImage image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
    }
    
    //Getters
    public int getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public double getPrice() {
        return price;
    }
    
    public BufferedImage getImage() {
        return image;
    }
}
