/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ingredient;

import data.FileManagement;
import gui.Menu;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import object.Ingredient;
import order.OrderManagement;
import recipe.RecipeManagement;
import utils.Utils;

/**
 *
 * @author Dell
 */
public class IngredientManagement {

    private List<Ingredient> listOfIngre = new ArrayList<>();
    private FileManagement fm = new FileManagement();
    private Menu menu = new Menu();
    private String ingreFile = "Ingredient.dat";
    private boolean changeStatus = false;

    public IngredientManagement() {
        if (fm.readFromFile(ingreFile, listOfIngre)) {
            System.out.println("Loaded Ingredientfs list successfully");
        } else {
            System.out.println("Ingredient loading failed");
        }

    }

    public List<Ingredient> getList() {
        return listOfIngre;
    }

    public void addIngreMenu() {
        menu.addItem("\n==========* INGREDIENT *==========");
        menu.addItem("ADD");
        menu.addItem("UPDATE");
        menu.addItem("DELTE");
        menu.addItem("SHOW ALL");
        menu.addItem("QUIT");
    }

    public void manageIngredientsMenu(RecipeManagement rm, OrderManagement om) {
        int choice = 0;
        do {
            addIngreMenu();
            menu.showItem();
            choice = Utils.getInteger("Select your choice: ", "Can not be blank", 1, 5);
            switch (choice) {
                case 1:
                    add(rm, om);
                    changeStatus = true;
                    break;
                case 2:
                    update(rm, om);
                    changeStatus = true;
                    break;
                case 3:
                    delete(rm, om);
                    changeStatus = true;
                    break;
                case 4:
                    display();
                    break;
                case 5:
                    System.err.println("QUIT INGREDIENT MENU");
            }
        } while (choice != 5);

    }

    public boolean saveIngreList() {
        if (fm.saveToFile(ingreFile, listOfIngre)) {
            return true;
        } else {
            return false;
        }
    }

    public Ingredient checkDuplicated(String code) {
        if (listOfIngre.isEmpty()) {
            return null;
        }
        for (Ingredient i : listOfIngre) {
            if (i.getCode().equalsIgnoreCase(code)) {
                return i;
            }
        }
        return null;
    }

    public void addIngre() {
        do {
            String code, name, unit;
            int weight;
            do {
                code = Utils.getString("Enter CODE: ", "CODE can not be left BLANK.");
                if (checkDuplicated(code) != null) {
                    System.err.println("CODE is duplicated in list.");
                } else {
                    break;
                }
            } while (true);
            name = Utils.getString("Enter NAME: ", "NAME can not be left BLANK.");
            weight = Utils.getInteger("Enter WEIGHT(KILOGRAM) (" + 0 + ", " + 10000 + "): ", "WEIGHT can not be left BLANK", 1, 10000);
            do {
                unit = Utils.getString("Enter Ingredient Unit (g/ml): ", "Unit cannot be empty");
                if (unit.equalsIgnoreCase("g") || unit.equalsIgnoreCase("ml")) {
                    break;
                } else {
                    System.err.println("Unit format is wrong\nPlease re-enter the unit");
                }
            } while (true);
            Ingredient i = new Ingredient(code, name, weight, unit);
            listOfIngre.add(i);
            showAIngredient(i);
            System.out.println("=====SUCCESS=====");
            System.out.println("");
        } while (Utils.confirmYesNo("Do you CONTINUE to ADD?\nPress [y/Y] - Yes, other key - No: "));

    }

    public void updateIngre() {
        do {
            String code;
            Ingredient i;
            int count = 0;
            do {
                code = Utils.getString("Enter CODE to UPDATE: ", "CODE can not be left BLANK.");
                i = checkDuplicated(code);
                if (i == null) {
                    System.err.println("The ingredient with code = '" + code + "' DOES NOT existed in list. Please try again!!");
                    System.out.println("Wrong " + (++count) + " time(s) already. 3 times will be OUT.");
                    if (count == 3) {
                        return;
                    }
                } else {
                    break;
                }
            } while (true);
            System.out.println("If you don't want to change, press ENTER.");
            code = Utils.updateString("Old CODE: " + i.getCode() + "\nNew CODE: ", i.getCode());
            String name = Utils.updateString("Old NAME: " + i.getName() + "\nNew NAME: ", i.getName());
            int weight = Utils.updateInteger("Old WEIGHT(KILOGRAM): " + i.getWeight() + "\nNew WEIGHT(KILOGRAM) (" + 0 + ", " + 10000 + "): ", 1, 10000, i.getWeight());
            String unit;
            do {
                unit = Utils.updateString("Old UNIT: " + i.getUnit() + "\nNew UNIT: ", i.getUnit());

                if (unit.equalsIgnoreCase("g") || unit.equalsIgnoreCase("ml")) {
                    break;
                } else {
                    System.out.println("Unit format is wrong\nPlease re-enter the unit");
                }
            } while (true);
            if (Utils.confirmYesNo("Do you want to save this update?\nPress [y/Y] - Yes, other key - No: ")) {
                i.setCode(code);
                i.setName(name);
                i.setWeight(weight);
                i.setUnit(unit);
                showAIngredient(i);
                System.out.println("");
                System.out.println("=====SUCCESS=====\n");
            } else {
                System.out.println("=====FAIL=====\n");
            }

        } while (Utils.confirmYesNo("Do you CONTINUE to UPDATE?\nPress [y/Y] - Yes, other key - No: "));
    }

    public void deleteIngre() {
        do {
            String code;
            Ingredient i;
            int count = 0;
            do {
                code = Utils.getString("Enter CODE to DELETE: ", "CODE can not be left BLANK.");
                i = checkDuplicated(code);
                if (i == null) {
                    System.err.println("The ingredient with code = " + code + " DOES NOT existed in list. Please try again!!");
                    System.out.println("Wrong " + (++count) + " time(s) already. 3 times will be OUT.");
                    if (count == 3) {
                        return;
                    }
                } else {
                    showAIngredient(i);
                    if (Utils.confirmYesNo("\nDo you really want to DELETE this ingredient?\nPress [y/Y] - Yes, other key - No: ")) {
                        listOfIngre.remove(i);
                        System.out.println("=====SUCCESS=====\n");
                    } else {
                        System.out.println("=====FAIL=====\n");
                    }
                    break;
                }
            } while (true);

        } while (Utils.confirmYesNo("Do you CONTINUE to DELETE?\nPress [y/Y] - Yes, other key - No: "));
    }

    public void showAllIngre() {
        if (listOfIngre.isEmpty()) {
            System.err.println("No ingredients were found");
            return;
        }
        listOfIngre.sort((i1, i2) -> i1.getName().compareToIgnoreCase(i2.getName()));
        Utils.showIngreTitle();
        for (Ingredient list : listOfIngre) {
            System.out.print(list.getIngreInfo());
        }
        System.out.println("");
        System.out.println("Total: " + listOfIngre.size() + " ingredient(s)");
    }

    public static Ingredient searchCode(List<Ingredient> list, String code) {
        if (list.isEmpty()) {
            return null;
        }
        for (Ingredient in : list) {
            if (in.getCode().equalsIgnoreCase(code)) {
                return in;
            }
        }
        return null;
    }

    public void ingreDashFormat() {
        for (int i = 0; i < 75; i++) {
            System.out.print("-");
        }
    }

    public boolean isChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(boolean changeStatus) {
        this.changeStatus = changeStatus;
    }

    public void add(RecipeManagement rm, OrderManagement om) {
        do {
            System.out.println("");
            String code, name, unit;
            int quantity;

            do {
                code = Utils.getString("Enter Code: ", "Code cannot be empty");
                if (searchCode(listOfIngre, code) == null) {
                    break;
                } else {
                    System.out.println("Code Duplicated");
                    System.out.println("Please re-enter the code");
                }
            } while (true);

            do {
                name = Utils.getString("Enter Ingredient Name: ", "Name cannot be empty");
                if (searchName(listOfIngre, name) == null) {
                    break;
                }
                System.out.println("Name has already exist");
                System.out.println("Please re-enter the name");
            } while (true);

            quantity = Utils.getAnInteger("Enter Quantity: ", "Quantity format is wrong", 0);
            do {
                unit = Utils.getString("Enter Ingredient Unit (g/ml): ", "Unit cannot be empty");
                if (unit.equalsIgnoreCase("g") || unit.equalsIgnoreCase("ml")) {
                    break;
                } else {
                    System.out.println("Unit format is wrong\nPlease re-enter the unit");
                }
            } while (true);

            listOfIngre.add(new Ingredient(code, name, quantity, unit));
            rm.ingredientSync(listOfIngre, listOfIngre.get(listOfIngre.size() - 1), true, om);
            System.out.println("The new Ingredient has been added successfully!");

        } while (Utils.confirmYesNo("\nDo you want to continue adding new ingredient? (Yes(Y)/No(N)): "));

    }

    public void update(RecipeManagement rm, OrderManagement om) {
        if (listOfIngre.isEmpty()) {
            System.out.println("No ingredients were found. Unable to continue updating");
            return;
        }
        String code, name, unit;
        int quantity;
        Ingredient in;
        code = Utils.getString("\nEnter Code to be update: ", "Code cannot be empty");
        if ((in = searchCode(listOfIngre, code)) == null) {
            System.out.println("The ingredient does not exist");
            return;
        }
        System.out.println("");

        name = Utils.getString("Enter Name: ");
        if (Utils.isBlank(name)) {
            name = in.getName();
        }

        quantity = Utils.updateNumber("Enter Quantity: ", "Quantity format is wrong", in.getWeight(), 0, Integer.MAX_VALUE);

        do {
            unit = Utils.getString("Enter Ingredient Unit (g/ml): ");
            if (unit.isEmpty()) {
                unit = in.getUnit();
                break;
            }
            if (unit.equalsIgnoreCase("g") || unit.equalsIgnoreCase("ml")) {
                break;
            } else {
                System.out.println("Unit format is wrong\nPlease re-enter the unit");
            }
        } while (true);
        in.setName(name);
        in.setWeight(quantity);
        in.setUnit(unit);
        rm.ingredientSync(listOfIngre, in, true, om);
        showAIngredient(in);
    }

    public void delete(RecipeManagement rm, OrderManagement om) {
        if (listOfIngre.isEmpty()) {
            System.out.println("No ingredients were found. Unable to continue deleting");
            return;
        }
        String code;
        System.out.println("\n* NOTE");
        System.out.println("* The code must only contain letters and numbers, the digit only can be at the end");
        code = Utils.getString("\nEnter Code to be delete: ", "Code cannot be empty");
        Ingredient in = searchCode(listOfIngre, code);
        if (in == null) {
            System.out.println("\nThe ingredient does not exist");
            return;
        }
        if (Utils.confirmYesNo("Do you want to delete this ingredient? (Yes(Y)/No(N)): ")) {
            listOfIngre.remove(in);
            rm.ingredientSync(listOfIngre, in, false, om);
            System.out.println("Deleted successfully");
        } else {
            System.out.println("Delete failed");
        }
    }

    public void display() {
        if (listOfIngre.isEmpty()) {
            System.err.println("No ingredients were found");
            return;
        }
        listOfIngre.sort((i1, i2) -> i1.getName().compareToIgnoreCase(i2.getName()));
        showListIngredients(listOfIngre);

    }

    public Ingredient searchName(List<Ingredient> list, String name) {
        if (list.isEmpty()) {
            return null;
        }
        for (Ingredient in : list) {
            if (in.getName().equalsIgnoreCase(name)) {
                return in;
            }
        }
        return null;
    }

    public void showAIngredient(Ingredient in) {
        ingreDashFormat();
        System.out.printf("\n|   Code   |%20sName%20s| Quantity | Unit |\n", " ", " ");
        ingreDashFormat();
        System.out.println("");
        System.out.println(in.getIngreInfo());
        ingreDashFormat();
        System.out.println("");
    }

    public void showListIngredients(List<Ingredient> list) {
        ingreDashFormat();
        System.out.printf("\n|   Code   |%20sName%20s| Quantity | Unit |\n", " ", " ");
        ingreDashFormat();
        System.out.println("");
        for (Ingredient in : list) {
            System.out.println(in.getIngreInfo());
            ingreDashFormat();
            System.out.println("");
        }
    }

}
