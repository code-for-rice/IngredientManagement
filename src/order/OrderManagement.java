package order;

import data.FileManagement;
import gui.Menu;
import ingredient.IngredientManagement;
import java.util.ArrayList;
import java.util.List;
import object.Ingredient;
import object.Recipe;
import recipe.RecipeManagement;
import utils.Utils;

public class OrderManagement {

    private static FileManagement fm = new FileManagement();
    private static String orderFilename = "Order.dat";
    private List<Order> orderList = new ArrayList();
    private Menu menu = new Menu();
//    private boolean checkUpdate = false;
    private boolean changeStatus = false;
    private Order latestOrder;

    public OrderManagement() {
        if (fm.readFromFile(orderFilename, orderList)) {
            System.out.println("Loaded Order list successfully");
        } else {
            System.out.println("Order list loading failed");
        }

        for (Order o : orderList) {
            if (o.getOrderNumber() == orderList.size()) {
                latestOrder = o;
            }
        }
    }

    public boolean isChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(boolean changeStatus) {
        this.changeStatus = changeStatus;
    }

    public boolean saveOrderToFile() {
        if (fm.saveToFile(orderFilename, orderList)) {
            changeStatus = false;
            return true;
        }
        return false;
    }

    private void insertMenu() {
        menu.addItem("\n==========* ORDER *==========");
        menu.addItem("Dispensing the drink");
        menu.addItem("Update the dispensing drink");
        menu.addItem("Quit");
    }

    public void orderMenu(OrderManagement om, List<Ingredient> list, List<Recipe> reList) {
        int choice = 0;
        do {
            insertMenu();
            menu.showItem();
            choice = Utils.getInteger("Select your choice: ", "Can not be left blank", 1, 3);
            switch (choice) {
                case 1:
                    om.dispensingDrink(reList, list);
                    changeStatus = true;
                    break;
                case 2:
                    om.updateDispensingDrink(reList, list);
                    changeStatus = true;
                    break;
                case 3:
                    System.err.println("QUIT ORDER MENU");
                    break;
            }
        } while (choice != 3);
        
        
    }

    public List<Order> getOrder() {
        return orderList;
    }

    public void orderDashFormat() {
        for (int i = 0; i < 107; i++) {
            System.out.print("-");
        }
    }

    public void showListOrder(List<Order> list) {
        orderDashFormat();
        System.out.printf("\n|Order Number|   Order Datetime   |Drink Code|%17sDrink Name%17s| Quantity Sold |\n", " ", " ");
        orderDashFormat();
        System.out.println("");
        for (Order o : list) {
            o.showOrderInfo();
            System.out.println("");
            orderDashFormat();
            System.out.println("");
        }
    }

    public void showAOrder(Order order) {
        orderDashFormat();
        System.out.printf("\n|Order Number|   Order Datetime   |Drink Code|%17sDrink Name%17s| Quantity Sold |\n", " ", " ");
        orderDashFormat();
        System.out.println("");
        order.showOrderInfo();
        System.out.println("");
        orderDashFormat();
        System.out.println("");
    }

    public static int checkMaxQuantity(Recipe r, List<Ingredient> ingreList) {
        int maxNumOfOrder = 1000000;
        for (Ingredient in : r.getIngreList().keySet()) {
            Ingredient ingredient = IngredientManagement.searchCode(ingreList, in.getCode());
            int num = ingredient.getWeight()/ r.getIngreList().get(in);
            if (maxNumOfOrder > num) {
                maxNumOfOrder = num;
            }
        }
        return maxNumOfOrder;
    }

    private void preparation(Recipe r, int numOfOrder, List<Ingredient> ingreList) {
        for (Ingredient in : r.getIngreList().keySet()) {
            Ingredient ingredient = IngredientManagement.searchCode(ingreList, in.getCode());
            int num = ingredient.getWeight()- r.getIngreList().get(in) * numOfOrder;
            ingredient.setWeight(num);
        }
    }

    public void orderListSync(Recipe r, boolean deleOrUpdate) {
        if (r.isStatus() == false) {
            for (Order order : orderList) {
                for (Recipe recipe : order.getOrderDetail().keySet()) {
                    if (recipe.getRecipeCode().equalsIgnoreCase(r.getRecipeCode())) {
                        recipe.setStatus(false);
                    }
                }
            }
        } else {
            for (Order order : orderList) {
                for (Recipe recipe : order.getOrderDetail().keySet()) {
                    if (recipe.getRecipeCode().equalsIgnoreCase(r.getRecipeCode())) {
                        recipe.setRecipeName(r.getRecipeName());
                        recipe.setStatus(true);
                    }
                }
            }
        }

    }

    private Recipe checkExist(Recipe r, Order order) {
        for (Recipe recipe : order.getOrderDetail().keySet()) {
            if (recipe.getRecipeCode().equalsIgnoreCase(r.getRecipeCode())) {
                return recipe;
            }
        }
        return null;
    }

    public void dispensingDrink(List<Recipe> reList, List<Ingredient> ingreList) {
//        if (reList.isEmpty() || ingreList.isEmpty()) {
//            System.out.println("\nUnable to dispense the beverage. The recipe or ingredients list does not exist");
//            return;
//        }
        System.out.println("");
        Order newOrder = new Order(orderList.size() + 1);
        do {
            Recipe r;
            String reCode = Utils.getString("\nEnter the Recipe Code: ", "Code cannot be empty");
            if ((r = RecipeManagement.searchCode(reList, reCode)) == null) {
                System.out.println("The drink does not exist");
            } else {
                if (r.isStatus() == false) {
                    System.out.println("This beverage has stopped being served");
                } else {
                    int maxQuantity = checkMaxQuantity(r, ingreList);
                    if (maxQuantity == 0) {
                        System.out.println("\nNot enough ingredients\nBeverage preparation failed");
                    } else {
                        System.out.println("\nThe remaining ingredients are only sufficient to prepare a maximum of " + maxQuantity + " servings of beverages");
                        int numOfOrder = Utils.getAnInteger("Enter the quantity of beverages: ", "The remaining ingredients are only sufficient to prepare a maximum of " + maxQuantity + " servings of beverages", "Quantity cannot be empty", 0, maxQuantity);
                        if (numOfOrder != 0) {
                            preparation(r, numOfOrder, ingreList);
                            System.out.println("Beverage preparation successfully");
                            Recipe isExist;
                            if ((isExist = checkExist(r, newOrder)) != null) {
                                newOrder.getOrderDetail().replace(r, numOfOrder + newOrder.getOrderDetail().get(isExist));
                            } else {
                                newOrder.getOrderDetail().put(r, numOfOrder);
                            }
                        } else {
                            System.out.println("Order canceled successfully");
                        }
                    }

                }
            }
        } while (Utils.confirmYesNo("Would you like to order anything else? (Yes(Y)/No(N)): "));
        if (newOrder.getOrderDetail().isEmpty() == false) {
            orderList.add(newOrder);
        }
        if (newOrder.getOrderDetail().isEmpty()) {
            System.out.println("Order canceled successfully");
            return;
        }
        showAOrder(newOrder);
        latestOrder = newOrder;
    }

    public void updateDispensingDrink(List<Recipe> reList, List<Ingredient> ingreList) {
        if (reList.isEmpty() || ingreList.isEmpty()) {
            System.out.println("\nUnable to dispense the beverage. The recipe or ingredients list does not exist");
            return;
        }
        if (orderList.isEmpty()) {
            System.out.println("\nNo order has ever been placed, cannot update");
            return;
        }
        showAOrder(latestOrder);
        if (Utils.confirmYesNo("Do you want to continue updating? (Yes(Y)/No(N)): ") == false) {
            System.out.println("Update failed");
            return;
        }

        for (Order o : orderList) {
            if (o.getOrderNumber() == orderList.size()) {
                orderList.remove(o);
                break;
            }
        }
        for (Recipe r : latestOrder.getOrderDetail().keySet()) {
            if (r.isStatus()) {
                for (Ingredient in : r.getIngreList().keySet()) {
                    Ingredient ingredient = IngredientManagement.searchCode(ingreList, in.getCode());
                    int quantity = ingredient.getWeight()+ latestOrder.getOrderDetail().get(r) * r.getIngreList().get(in);
                    ingredient.setWeight(quantity);
                }
            }
        }
        Order newOrder = new Order(orderList.size() + 1);
        for (Recipe r : latestOrder.getOrderDetail().keySet()) {
            if (r.isStatus() == false) {
                System.out.println("\nDrink Code: " + r.getRecipeCode());
                System.out.println("Quantity: " + latestOrder.getOrderDetail().get(r));
                System.out.println("*This beverage has stopped being served. Cannot update");
                newOrder.getOrderDetail().put(r, latestOrder.getOrderDetail().get(r));
                continue;
            }
            int maxQuantity = checkMaxQuantity(r, ingreList);
            System.out.println("\nDrink Code: " + r.getRecipeCode());
            if (maxQuantity == 0) {
                System.out.println("\nNot enough ingredients\nBeverage preparation failed");
            } else {
                System.out.println("The remaining ingredients are only sufficient to prepare a maximum of " + maxQuantity + " servings of beverages");
                int newQuantity = Utils.updateNumber("Enter new Quantity: ", "Number format is wrong\nNumber must be greater than 0", latestOrder.getOrderDetail().get(r), 0, maxQuantity);
                if (newQuantity != 0) {
                    preparation(r, newQuantity, ingreList);
                    newOrder.getOrderDetail().put(r, newQuantity);
                }
            }
        }
        while (Utils.confirmYesNo("\nWould you like to order anything else? (Yes(Y)/No(N)): ")) {
            Recipe r;
            String reCode = Utils.getString("Enter the Recipe Code: ", "Code cannot be empty");
            if ((r = RecipeManagement.searchCode(reList, reCode)) == null) {
                System.out.println("The drink does not exist");
            } else {
                if (r.isStatus() == false) {
                    System.out.println("This beverage has stopped being served");
                } else {
                    int maxQuantity = checkMaxQuantity(r, ingreList);
                    if (maxQuantity == 0) {
                        System.out.println("\nNot enough ingredients\nBeverage preparation failed");
                    } else {
                        System.out.println("\nThe remaining ingredients are only sufficient to prepare a maximum of " + maxQuantity + " servings of beverages");
                        int numOfOrder = Utils.getAnInteger("Enter the quantity of beverages: ", "The remaining ingredients are only sufficient to prepare a maximum of " + maxQuantity + " servings of beverages", "Quantity cannot be empty", 0, maxQuantity);
                        if (numOfOrder != 0) {
                            preparation(r, numOfOrder, ingreList);
                            System.out.println("Beverage preparation successfully");
                            Recipe isExist;
                            if ((isExist = checkExist(r, newOrder)) != null) {
                                newOrder.getOrderDetail().replace(r, numOfOrder + newOrder.getOrderDetail().get(isExist));
                            } else {
                                newOrder.getOrderDetail().put(r, numOfOrder);
                            }
                        } else {
                            System.out.println("\nNot enough ingredients\nBeverage preparation failed");
                        }
                    }
                }
            }
        }
        latestOrder = newOrder;
        if (newOrder.getOrderDetail().isEmpty() == false) {
            orderList.add(newOrder);
        } else {
            if (newOrder.getOrderDetail().isEmpty()) {
                System.out.println("Order canceled successfully");
                return;
            }
        }
        showAOrder(newOrder);
    }
}
