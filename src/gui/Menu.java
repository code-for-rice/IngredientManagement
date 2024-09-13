/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Dell
 */
public class Menu {
    private Scanner sc = new Scanner(System.in);
    List<String> list;
    public Menu() {
        list = new ArrayList<>();
    }
    
    public void addItem(String s){
        list.add(s);
    }
    
    public void showItem(){
        for (int i = 0; i < list.size(); i++) {
            if(i == 0){
                System.out.println(list.get(i));
            } else {   
                System.out.println(i + ". " + list.get(i) + ".");
            }
        }
        list.clear();
    }
    
    

}
