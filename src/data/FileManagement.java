/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;


/**
 *
 * @author Dell
 */
public class FileManagement {

    public <T> boolean readFromFile(String fileName, List<T> list) {
        boolean status = false;
        try {
            File fo = new File(fileName);
            InputStream fis = new FileInputStream(fo);
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                try {
                    T x = (T) ois.readObject();
                    list.add(x);
                } catch (IOException | ClassNotFoundException e) {
                    break;
                }
            }

            ois.close();
            fis.close();
            status = true;
        } catch (FileNotFoundException ex) {
            System.err.println("File not found e");
        } catch (IOException ex) {
            System.err.println("IO e");
        }
        return status;
    }

    public <T> boolean saveToFile(String fileName, List<T> list) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Empty list");
            return false;
        }
        try {
            OutputStream os = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            for (T item : list) {
                oos.writeObject(item);
            }
            oos.flush();
            oos.close();
            os.close();
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }
    
    public <T> boolean saveDataToFile(List<T> list, List<T> list2, String fileName, String msg) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("File not exist!");
            return false;
        }
        
        try {
            OutputStream os = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            for (T item : list) {
                oos.writeObject(item);
            }
            for (T item : list2) {
                oos.writeObject(item);
            }
            oos.flush();
            oos.close();
            os.close();
            System.out.println(msg);
            return true; 
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }

    public <T> boolean saveDataToFile(List<T> list, String fileName, String msg) {
        File f = new File(fileName);
        if (!f.exists()) {
            System.out.println("Empty list");
            return false;
        }
        try {
            OutputStream os = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            for (T item : list) {
                oos.writeObject(item);
            }
            oos.flush();
            oos.close();
            os.close();
            System.out.println(msg);
            return true;
        } catch (IOException e) {
            System.out.println(e);
            return false;
        }
    }
}
