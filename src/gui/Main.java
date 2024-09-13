/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import data.FileManagement;
import ingredient.IngredientManagement;
import java.io.File;
import java.util.List;
import object.Ingredient;
import object.Recipe;
import order.Order;
import order.OrderManagement;
import recipe.RecipeManagement;
import report.ReportManagement;
import utils.Utils;

/**
 *
 * @author Dell
 */
public class Main {

    public static void main(String[] args) {
        Menu mMenu = new Menu();
        int choice;
        boolean changeStatus = false;

        IngredientManagement im = new IngredientManagement();
        List<Ingredient> ingreList = im.getList();
        RecipeManagement rm = new RecipeManagement();
        List<Recipe> reList = rm.getReList();
        OrderManagement om = new OrderManagement();
        List<Order> orderList = om.getOrder();
        ReportManagement reMn = new ReportManagement();
        do {
        mMenu.addItem("========== *COFFEE STORE MANAGEMENT* =============");
        mMenu.addItem("Manage ingredients");
        mMenu.addItem("Manage beverage recipes");
        mMenu.addItem("Dispensing beverages");
        mMenu.addItem("Report");
        mMenu.addItem("Store data to files");
        mMenu.addItem("Exit");
            if (im.isChangeStatus() || rm.isChangeStatus() || om.isChangeStatus()) {
                changeStatus = true;
            }
            mMenu.showItem();
            choice = Utils.getInteger("Select your choice: ", "Can not be left blank", 1, 6);
            switch (choice) {
                case 1:
                    im.manageIngredientsMenu(rm, om);
                    break;
                case 2:
                    rm.manageBeverageRecipesMenu(rm, ingreList, om);
                    break;
                case 3:
                    om.orderMenu(om, ingreList, reList);
                    break;
                case 4:
                    reMn.reportMenu(reMn, orderList, om, reList, rm, ingreList, im);
                    break;
                case 5:
                    if (im.saveIngreList()&& rm.saveMenuToFile() && om.saveOrderToFile()) {
                        System.out.println("Save to file successfully");
                        changeStatus = false;
                    }
                    break;
                case 6:
                    
                    System.err.println("EXIT PROGRAM");
                    break;
            }
        } while (choice != 6);

    }
}
