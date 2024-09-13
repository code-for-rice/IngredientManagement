/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package object;

import java.io.Serializable;

/**
 *
 * @author Dell
 */
public class Ingredient implements Serializable, Comparable<Ingredient> {

    private int weight;
    private String code;
    private String name;
    private String unit;

    public Ingredient(String code, String name, int weight, String unit) {
        this.code = code;
        this.name = name;
        this.weight = weight;
        this.unit = unit;
    }

    public Ingredient() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    
    public String getIngreInfo() {
        return String.format("\n|%-10s|%-44s|%10d|%6s|\n", code.toUpperCase(), name, weight, unit.toLowerCase());
    }

    @Override
    public int compareTo(Ingredient o) {
        if (this.name.equalsIgnoreCase(o.name)) {
            return 0;
        }
        if (this.name.compareToIgnoreCase(o.name) > 0) {
            return 1;
        } else {
            return -1;
        }
    }


}
