/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author Dell
 */
public class Recipe implements Serializable, Comparable<Recipe>{

    private String code;
    private String name;
    private HashMap<Ingredient, Integer> ingreList = new HashMap();
    private boolean status;

    public Recipe() {
    }

    public Recipe(String reCode, String reName) {
        this.code = reCode;
        this.name = reName;
        this.status = status;
        status = true;
    }

    public String getRecipeCode() {
        return code;
    }

    public String getRecipeName() {
        return name;
    }

    public void setRecipeName(String recipeName) {
        this.name = recipeName;
    }

    public HashMap<Ingredient, Integer> getIngreList() {
        return ingreList;
    }

    public void setIngreList(HashMap<Ingredient, Integer> ingreList) {
        this.ingreList = ingreList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void getRecipeInfo() {
        boolean checkFirst = true;
        for (Ingredient in : ingreList.keySet()) {
            if(checkFirst) {
                System.out.printf("|%-10s|%-44s|%-15s|%-49s|%10d|%6s|\n", code.toUpperCase(), name, in.getCode(), in.getName(), ingreList.get(in), in.getUnit().toLowerCase());
                checkFirst = false;
            } else {
                System.out.printf("|%-10s|%-44s|%-15s|%-49s|%10d|%6s|\n", " ", " ", in.getCode(), in.getName(), ingreList.get(in), in.getUnit().toLowerCase());
            }
        }
    }

    @Override
    public int compareTo(Recipe o) {
        if(this.getRecipeCode().equalsIgnoreCase(o.getRecipeCode())) return 0;
        if(this.getRecipeCode().compareToIgnoreCase(o.getRecipeCode()) > 0) return 1;
        else return -1;
    }

}
