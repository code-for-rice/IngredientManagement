/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipe;

import data.FileManagement;
import gui.Menu;
import ingredient.IngredientManagement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import object.Ingredient;
import object.Recipe;
import order.OrderManagement;
import utils.Utils;

/**
 *
 * @author Dell
 */
public class RecipeManagement {

    private static FileManagement fm = new FileManagement();
    private List<Recipe> reList = new ArrayList();
    private static String menuFilename = "Menu.dat";
    private Menu menu = new Menu();
    private boolean changeStatus = false;

    public RecipeManagement() {
        if (fm.readFromFile(menuFilename, reList)) {
            System.out.println("Loaded Menu successfully");
        } else {
            System.out.println("Menu loading failed");
        }
    }

    public boolean isChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(boolean changeStatus) {
        this.changeStatus = changeStatus;
    }

    public List<Recipe> getReList() {
        return reList;
    }

    public boolean saveMenuToFile() {
        if (fm.saveToFile(menuFilename, reList)) {
            changeStatus = false;
            return true;
        }
        return false;
    }

    public static void main(String[] args) {//main là static -> fm phải là static "line 22"
        List<Recipe> list = new ArrayList();
        if (fm.saveToFile(menuFilename, list)) {
            System.out.println("successfully");
        }
    }

    private void addRecipeMenu() {
        menu.addItem("\n==========* RECIPE *==========");
        menu.addItem("Add the drink to menu");
        menu.addItem("Update the drink information");
        menu.addItem("Delete the drink from menu");
        menu.addItem("Show menu");
        menu.addItem("Quit");
    }

    public void manageBeverageRecipesMenu(RecipeManagement rm, List<Ingredient> ingreList, OrderManagement om) {
        
        int choice = 0;
        do {
            addRecipeMenu();
            menu.showItem();
            choice = Utils.getInteger("Select your choice: ", "Choice can not be empty", 1, 5);
            switch (choice) {
                case 1:
                    rm.add(ingreList);
                    changeStatus = true;
                    break;
                case 2:
                    rm.update(ingreList, om);
                    changeStatus = true;
                    break;
                case 3:
                    rm.delete(om);
                    changeStatus = true;
                    break;
                case 4:
                    rm.display();
                    break;
                case 5:
                    System.err.println("QUIT RECIPE MENU");
            }
        } while (choice != 5);
        
    }

    public void recipeDashFormat() {
        for (int i = 0; i < 141; i++) {
            System.out.print("-");
        }
    }

    public void showARecipe(Recipe recipe) {
        recipeDashFormat();
        System.out.printf("\n|   Code   |%20sName%20s|Ingredient Code|%17sIngredient Name%17s| Quantity | Unit |\n", " ", " ", " ", " ");
        recipeDashFormat();
        System.out.println("");
        recipe.getRecipeInfo();
        recipeDashFormat();
        System.out.println("");
    }

    public void showListRecipes(List<Recipe> list) {
        recipeDashFormat();
        System.out.printf("\n|   Code   |%20sName%20s|Ingredient Code|%17sIngredient Name%17s| Quantity | Unit |\n", " ", " ", " ", " ");
        recipeDashFormat();
        System.out.println("");
        for (Recipe re : list) {
            re.getRecipeInfo();
            recipeDashFormat();
            System.out.println("");
        }
    }

    public void ingredientSync(List<Ingredient> list, Ingredient ingre, boolean deleOrUpdate, OrderManagement om) {
        if (deleOrUpdate == false) {
            for (Recipe r : reList) {
                for (Ingredient in : r.getIngreList().keySet()) {
                    if (in.getCode().equalsIgnoreCase(ingre.getCode())) {
                        r.setStatus(false);
                        om.orderListSync(r, false);
                        break;
                    }
                }
            }
        } else {
            for (Recipe r : reList) {
                for (Ingredient in : r.getIngreList().keySet()) {
                    if (in.getCode().equalsIgnoreCase(ingre.getCode())) {
                        in.setName(ingre.getName());
                        r.setStatus(true);
                        om.orderListSync(r, true);
                    }
                }
            }
        }
    }

    public static Recipe searchCode(List<Recipe> list, String code) {
        if (list.isEmpty()) {
            return null;
        }
        for (Recipe r : list) {
            if (r.getRecipeCode().equalsIgnoreCase(code)) {
                return r;
            }
        }
        return null;
    }

    public boolean checkExist(Set<Ingredient> list, String ingreCode) {
        for (Ingredient in : list) {
            if (in.getCode().equalsIgnoreCase(ingreCode)) {
                return true;
            }
        }
        return false;
    }

    public void add(List<Ingredient> list) {
        if (list.isEmpty()) {
            System.out.println("No Ingredients were found. Cannot add new Recipe");
            return;
        }
//        System.out.println("\nNOTE:");
//        System.out.println("* The code must only contain letters and numbers, the digit only can be at the end");
        do {
            System.out.println("");
            String code, name;
            int ingreQuantity;
            Ingredient in;
            do {
                code = Utils.getString("Enter Recipe Code: ", "Code cannot be empty");
                if (searchCode(reList, code) == null) {
                    break;
                } else {
                    System.out.println("Code Duplicated\nPlease re-enter the code");
                }
            } while (true);
            boolean check = false;
            do {
                name = Utils.getString("Enter Recipe Name: ", "Name cannot be empty");
                if (reList.isEmpty()) {
                    check = false;
                } else {
                    for (Recipe re : reList) {
                        if (re.getRecipeName().equalsIgnoreCase(name)) {
                            System.out.println("Name cannot be duplicated");
                            check = true;
                            break;
                        }
                    }
                }
            } while (check);

            Recipe r = new Recipe(code, name);
            IngredientManagement im = new IngredientManagement();
            im.showAllIngre();
            
            do {
                do {
                    String msg = "Enter Ingredient Code #" + (r.getIngreList().size() + 1) + ": ";
                    String ingreCode = Utils.getString(msg, "Code cannot be empty");
                    in = IngredientManagement.searchCode(list, ingreCode);
                    if (in == null) {
                        System.out.println("Ingredient Code does not exist\nPlease re-enter");
                    } else if (checkExist(r.getIngreList().keySet(), ingreCode)) {
                        System.out.println("This Ingredient already exists");
                    } else {
                        break;
                    }
                } while (true);
                ingreQuantity = Utils.getAnInteger("Enter Quantity/" + in.getUnit().toLowerCase() + ": ", "Quantity cannot be empty", 1);
                r.getIngreList().put(in, ingreQuantity);
            } while (Utils.confirmYesNo("Do you want to adding new Ingredient? (Yes(Y)/Other Key: No): "));

            reList.add(r);
            showARecipe(r);
            System.out.println("The new Recipe has been added successfully!");
        } while (Utils.confirmYesNo("\nDo you want to continue adding new Recipe? (Yes(Y)/No(N)): "));
    }

    public void update(List<Ingredient> list, OrderManagement om) {
        if (reList.isEmpty()) {
            System.out.println("No Recipes were found. Unable to continue updating");
            return;
        }
        
        String code = Utils.updateString("\nEnter Recipe Code to be update: ", "Code format is wrong");
        Recipe r = searchCode(reList, code);
        if (r == null) {
            System.out.println("The drink does not exist");
            return;
        }
        String name = Utils.updateString("Old NAME: " + r.getRecipeName()+ "\nNew NAME: ", r.getRecipeName());
        r.setRecipeName(name);
        om.orderListSync(r, true);
        System.out.println("\nWhat kind of updates do you want?");
        System.out.println("1. Add ingredients");
        System.out.println("2. Delete ingredients");
        System.out.println("3. Edit ingredients and quantity");
        int opts = Utils.getAnInteger("Choose your option: ", "Number format is wrong", 1, 3);
        boolean check = true;
        String msg;
        switch (opts) {
            case 1:
                Ingredient addIngre;
                do {
                    String addIngreCode;
                    System.out.println("");
                    do {
                        addIngreCode = Utils.getString("Enter Ingredient Code to be added: ", "Code cannot be empty");
                        addIngre = IngredientManagement.searchCode(list, addIngreCode);
                        if (addIngre == null) {
                            System.out.println("Ingredient Code does not exist");
                        } else {
                            if (checkExist(r.getIngreList().keySet(), addIngreCode)) {
                                System.out.println("This Ingredient already exists in this Recipe");
                                check = true;
                            } else {
                                check = false;
                            }
                        }
                    } while (check);
                    int ingreQuantity = Utils.getAnInteger("Enter Quantity/" + addIngre.getUnit().toLowerCase() + ": ", "Quantity cannot be empty", 1);
                    r.getIngreList().put(addIngre, ingreQuantity);
                } while (Utils.confirmYesNo("Do you want to continue adding new ingredients to this recipe? (Yes(Y)/Other keys: No): "));
                om.orderListSync(r, true);
                showARecipe(r);
                break;
            case 2:
                if (r.getIngreList().isEmpty()) {
                    System.out.println("\nIngredient list is empty\nYou cannot delete ingredients from this recipe");
                    break;
                }
                do {
                    String deleIngreCode;
                    System.out.println("");
                    do {
                        deleIngreCode = Utils.getString("Enter Ingredient Code to be delete: ", "Code cannot be empty");
                        if (IngredientManagement.searchCode(list, deleIngreCode) == null) {
                            System.out.println("Ingredient Code does not exist");
                        } else {
                            break;
                        }
                    } while (true);

                    for (Ingredient in : r.getIngreList().keySet()) {
                        if (deleIngreCode.equalsIgnoreCase(in.getCode())) {
                            if (Utils.confirmYesNo("Do you want to delete this Ingredient? (Yes(Y)/No(N): ")) {
                                r.getIngreList().remove(in);
                            }
                            check = false;
                            break;
                        } else {
                            check = true;
                        }
                    }
                    if (check == true) {
                        System.out.println("No ingredients found");
                    }
                } while (Utils.confirmYesNo("Do you want to continue deleting recipe ingredients? (Yes(Y)/No(N)): "));

                showARecipe(r);
                break;
            case 3:
                HashMap<Ingredient, Integer> newIngreList = new HashMap();
                Ingredient editIngre;
                int quantity;
                int count = 1;
                for (Ingredient in : r.getIngreList().keySet()) {
                    System.out.println("\nOld Ingredient Code #" + count + "/" + r.getIngreList().size() + ": " + in.getCode());
                    System.out.println("Old Ingredient Quantity #" + count + "/" + r.getIngreList().size() + ": " + r.getIngreList().get(in));
                    do {
                        msg = "Enter New Ingredient Code #" + count + "/" + r.getIngreList().size() + ": ";
                        String ingreCode = Utils.getString(msg);
                        if (ingreCode.isEmpty()) {
                            editIngre = in;
                            break;
                        }
                        editIngre = IngredientManagement.searchCode(list, ingreCode);
                        if (editIngre == null) {
                            System.out.println("Ingredient Code does not exist\nPlease re-enter");
                            check = true;
                        } else {
                            if (!(in.getCode().equalsIgnoreCase(ingreCode)) && checkExist(r.getIngreList().keySet(), ingreCode)) {
                                System.out.println("This Ingredient already exists in this Recipe");
                                check = true;
                            } else {
                                check = false;
                            }
                        }
                    } while (check);
                    msg = "Number must be between " + 1 + " to " + Integer.MAX_VALUE;
                    quantity = Utils.updateNumber("Enter quantity/" + editIngre.getUnit()+ ": ", msg, r.getIngreList().get(in), 1, Integer.MAX_VALUE);
                    newIngreList.put(editIngre, quantity);
                    count++;
                }
                r.setIngreList(newIngreList);
                showARecipe(r);
                break;
        }

    }

    public void delete(OrderManagement om) {
        if (reList.isEmpty()) {
            System.out.println("No Recipes were found. Unable to continue deleting");
            return;
        }
        System.out.println("\n* NOTE");
        System.out.println("* The code must only contain letters and numbers, the digit only can be at the end");
        String code = Utils.getString("Enter Recipe Code to be delete: ","Code cannot be empty");
        Recipe r = searchCode(reList, code);
        if (r == null) {
            System.out.println("The drink does not exist");
            return;
        }
        if (Utils.confirmYesNo("Do you want to delete this recipe? (Yes(Y)/No(N)): ") == false) {
            System.out.println("\nDelete failed");
            return;
        }
        reList.remove(r);
        om.orderListSync(r, false);
        System.out.println("\nDelete successfully");
    }

    public void display() {
        if (reList.isEmpty()) {
            System.out.println("No Recipes were found");
            return;
        }

        reList.sort((r1, r2) -> r1.getRecipeName().compareToIgnoreCase(r2.getRecipeName()));
        showListRecipes(reList);
        System.out.println("Total: " + reList.size() + " recipe(s)");
    }
}
