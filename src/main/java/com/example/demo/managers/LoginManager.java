package com.example.demo.managers;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class LoginManager {
    private static final String FILE_PATH = "progress.txt"; // Save file path
    private final Map<String, UserData> userDatabase = new HashMap<>(); // Stores username, password, and progress

    public LoginManager() {
        loadProgress();
    }

    // Login method with username and password validation
    public boolean login(String username, String password) {
        UserData userData = userDatabase.get(username);
        return userData != null && userData.password.equals(password);
    }

    // Get the progress of a user
    public int getUserProgress(String username) {
        UserData userData = userDatabase.get(username);
        return userData != null ? userData.progress : 1; // Default to Level 1
    }

    // Save or update progress for a user
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

    // Register a new user with username and password
    public boolean register(String username, String password) {
        if (userDatabase.containsKey(username)) {
            return false; // Username already exists
        }
        userDatabase.put(username, new UserData(password, 1)); // Default progress to Level 1
        saveToFile();
        return true;
    }

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

    // Inner class to store user data
    private static class UserData {
        private final String password;
        private int progress;

        public UserData(String password, int progress) {
            this.password = password;
            this.progress = progress;
        }
    }
}
