/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Dennis
 */
public abstract class ItemOrder {
    protected int itemOrderId;
    protected int amount;
    protected String status;
    
    public ItemOrder(int itemId, int amount) {
        this.itemOrderId = itemId;
        this.amount = amount;
    }
    
    public int getAmount() {
        return amount;
    }
    
    abstract public Item getItem();
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
