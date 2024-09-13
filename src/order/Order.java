package order;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import object.Recipe;

public class Order implements Serializable, Comparable<Order> {

    private int orderNumber;
    private LocalDateTime orderDateTime;
    private HashMap<Recipe, Integer> orderDetail = new HashMap();
    

    public Order(int orderNumber) {
        this.orderNumber = orderNumber;
        orderDateTime = LocalDateTime.now();
    }

    private String formatOrderDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
        return formatter.format(orderDateTime);
    }

    @Override
    public String toString() {
        return "Order{" + "orderNumber=" + orderNumber + ", orderDateTime=" + orderDateTime + ", orderDetail=" + orderDetail + '}';
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime() {
        this.orderDateTime = LocalDateTime.now();
    }

    public HashMap<Recipe, Integer> getOrderDetail() {
        return orderDetail;
    }

    public void showOrderInfo() {
        boolean checkFirst = true;
        for (Recipe r : orderDetail.keySet()) {
            if (checkFirst) {
                System.out.printf("|%12d|%-20s|%-10s|%-44s|%15d|", orderNumber, formatOrderDateTime(), r.getRecipeCode().toUpperCase(), r.getRecipeName(), orderDetail.get(r));
                checkFirst = false;
            } else {
                System.out.printf("\n|%12s|%20s|%-10s|%-44s|%15d|", " ", " ", r.getRecipeCode().toUpperCase(), r.getRecipeName(), orderDetail.get(r));
            }
        }
    }

    @Override
    public int compareTo(Order o) {
        if (this.orderNumber > o.orderNumber) {
            return 1;
        } else if (this.orderNumber == o.orderNumber) {
            return 0;
        } else {
            return -1;
        }
    }

}
