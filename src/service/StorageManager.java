package service;

import model.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class StorageManager {


    public void saveToFile(List<Person> persons, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(persons);


            util.AppLogger.LOGGER.info("SSave" + persons.size() + " persons to " + filePath);


        } catch (IOException e) {



            util.AppLogger.LOGGER.log(java.util.logging.Level.SEVERE, "CRITICAL: Failed to save data to " + filePath, e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<Person> loadFromFile(String filePath) {


        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            List<Person> persons = (List<Person>) ois.readObject();


            util.AppLogger.LOGGER.info("Loaded " + persons.size() + " persons from " + filePath);
            return persons;


        } catch (IOException | ClassNotFoundException e) {


            util.AppLogger.LOGGER.warning("Could not load data from " + filePath + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
