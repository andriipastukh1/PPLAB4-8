package service;

import model.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {


    public void saveToFile(List<Person> persons, String filePath) {


        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(persons);


            System.out.println("Saved " + persons.size() + " persons to: " + filePath);
        } catch (IOException e) {
            System.out.println("Save error: " + e.getMessage());


        }
    }

    @SuppressWarnings("unchecked")
    public List<Person> loadFromFile(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            Object obj = ois.readObject();


            if (obj instanceof List) {
                return (List<Person>) obj;
            }


        } catch (FileNotFoundException fnf) {
            System.out.println("File not found: " + filePath);


        } catch (Exception e) {


            System.out.println("Load error: " + e.getMessage());
        }


        return new ArrayList<>();
    }
}
