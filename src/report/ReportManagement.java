package report;

import gui.Menu;
import ingredient.IngredientManagement;
import java.util.ArrayList;
import java.util.List;
import object.Ingredient;
import object.Recipe;
import order.Order;
import order.OrderManagement;
import recipe.RecipeManagement;
import utils.Utils;

public class ReportManagement {


    private Menu reMenu = new Menu();
    
    private void insertMenu() {
        
        reMenu.addItem("============= *REPORT* ===============");
        reMenu.addItem("The ingredients are available");
        reMenu.addItem("The drinks for which the store is out of ingredients");
        reMenu.addItem("Show all the dispensing drink");
        reMenu.addItem("Quit");
    }

    public ReportManagement() {
    }
    
    public void reportMenu(ReportManagement reMn, List<Order> orderList, OrderManagement om, List<Recipe> reList, RecipeManagement rm, List<Ingredient> ingreList, IngredientManagement im) {
        int choice = 0;
        do {
            insertMenu();
            reMenu.showItem();
            choice = Utils.getInteger("Select your choice: ", "Can not be left blank", 1, 4);
            switch (choice) {
                case 1:
                    reMn.ingredientsStatus(ingreList, im);
                    break;
                case 2:
                    reMn.outOfStockDrink(reList, ingreList, rm);
                    break;
                case 3:
                    reMn.orderList(orderList, om);
                    break;
                case 4:
                    System.err.println("EXIT REPORT MENU");
                    break;
            }
        } while (choice != 4);
        
    }
    
    public void ingredientsStatus(List<Ingredient> ingreList, IngredientManagement im) {
        if(ingreList.isEmpty()){
            System.out.println("No ingredients are on the list");
            return;
        }
        List<Ingredient> availableIngre = new ArrayList();
        for (Ingredient in : ingreList) {
            if (in.getWeight()> 0) {
                availableIngre.add(in);
            }
        }
        im.showListIngredients(availableIngre);
        System.out.println("Total: " + ingreList.size() + " ingredient(s)");
    }

    public void outOfStockDrink(List<Recipe> reList, List<Ingredient> ingreList, RecipeManagement rm) {
        List<Recipe> outOfStock = new ArrayList();
        for (Recipe r : reList) {
            if (r.isStatus() == false || OrderManagement.checkMaxQuantity(r, ingreList) == 0) {
                outOfStock.add(r);
            }
        }
        if (outOfStock.isEmpty()) {
            System.out.println("\nNone of the beverages are out of stock");
        } else {
            rm.showListRecipes(outOfStock);
            System.out.println("Total: " + outOfStock.size() + " recipe(s)");
        }
    }

    public void orderList(List<Order> orderList, OrderManagement om) {
        if(orderList.isEmpty()) {
            System.out.println("\nNo drinks have been prepared yet");
            return;
        }
        orderList.sort((o1, o2) -> o1.getOrderNumber() - o2.getOrderNumber());
        om.showListOrder(orderList);

        String msg = "Total: " + orderList.size() + " order(s)";
        System.out.printf(msg);
    }
}
