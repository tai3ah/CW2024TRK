package com.example.demo.managers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The LoginManager class handles user login, registration, and progress management.
 * It stores user data including username, password, and progress in a file.
 */
public class LoginManager {
    private static final String FILE_PATH = "progress.txt"; // Save file path
    private final Map<String, UserData> userDatabase = new HashMap<>(); // Stores username, password, and progress

    /**
     * Constructs a LoginManager and loads user progress from the file.
     */
    public LoginManager() {
        loadProgress();
    }

    /**
     * Validates the login credentials of a user.
     *
     * @param username the username of the user
     * @param password the password of the user
     * @return true if the login is successful, false otherwise
     */
    public boolean login(String username, String password) {
        UserData userData = userDatabase.get(username);
        return userData != null && userData.password.equals(password);
    }

    /**
     * Gets the progress of a user.
     *
     * @param username the username of the user
     * @return the progress of the user, or 1 if the user is not found
     */
    public int getUserProgress(String username) {
        UserData userData = userDatabase.get(username);
        return userData != null ? userData.progress : 1; // Default to Level 1
    }

    /**
     * Saves or updates the progress for a user.
     *
     * @param username the username of the user
     * @param level the level to save
     */
    public void saveProgress(String username, int level) {
        UserData userData = userDatabase.get(username);
        if (userData != null) {
            userData.progress = Math.max(userData.progress, level); // Update only if new level is higher
        } else {
            // Register a new user with a default password if none exists
            userDatabase.put(username, new UserData("defaultPassword", level));
        }
        saveToFile();
    }

    /**
     * Registers a new user with a username and password.
     *
     * @param username the username of the new user
     * @param password the password of the new user
     * @return true if the registration is successful, false if the username already exists
     */
    public boolean register(String username, String password) {
        if (userDatabase.containsKey(username)) {
            return false; // Username already exists
        }
        userDatabase.put(username, new UserData(password, 1)); // Default progress to Level 1
        saveToFile();
        return true;
    }

    /**
     * Loads user progress from the file.
     */
    private void loadProgress() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    int progress = Integer.parseInt(parts[2]);
                    userDatabase.put(username, new UserData(password, progress));
                }
            }
        } catch (IOException e) {
            System.out.println("Progress file not found. Starting fresh.");
        }
    }

    /**
     * Saves user data to the file.
     */
    private void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (var entry : userDatabase.entrySet()) {
                String username = entry.getKey();
                UserData userData = entry.getValue();
                writer.write(username + ":" + userData.password + ":" + userData.progress);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inner class to store user data including password and progress.
     */
    private static class UserData {
        private final String password;
        private int progress;

        /**
         * Constructs a UserData instance with the specified password and progress.
         *
         * @param password the password of the user
         * @param progress the progress of the user
         */
        public UserData(String password, int progress) {
            this.password = password;
            this.progress = progress;
        }
    }
}