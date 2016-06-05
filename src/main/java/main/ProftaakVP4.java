/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import datastorage.DatabaseConnection;
import datastorage.DishDAO;
import datastorage.OrderDAO;
import presentation.SysteemUI;

/**
 *
 * @author Mathijs
 */
public class ProftaakVP4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SysteemUI ui = new SysteemUI();
        OrderDAO test = new OrderDAO();
        System.out.println(test.getAutoIncrementValue());
    }
    
}

// update push test 2

//Hallo

// branched to hotfix