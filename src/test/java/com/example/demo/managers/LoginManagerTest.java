package com.example.demo.managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import static org.junit.jupiter.api.Assertions.*;

class LoginManagerTest {

    private static final String TEST_FILE_PATH = "test_progress.txt";
    private LoginManager loginManager;

    @BeforeEach
    void setUp() {
        // Ensure a clean test file before each test
        File testFile = new File(TEST_FILE_PATH);
        if (testFile.exists()) {
            assertTrue(testFile.delete(), "Failed to delete old test file.");
        }
        loginManager = new LoginManager();
    }

    @Test
    void testRegisterNewUser() {
        boolean registered = loginManager.register("testUser", "testPassword");
        assertTrue(registered, "User should be successfully registered.");
    }

    @Test
    void testRegisterDuplicateUser() {
        loginManager.register("testUser", "testPassword");
        boolean registeredAgain = loginManager.register("testUser", "newPassword");
        assertFalse(registeredAgain, "Duplicate registration should be prevented.");
    }

    @Test
    void testLoginSuccessful() {
        loginManager.register("testUser", "testPassword");
        boolean loginSuccess = loginManager.login("testUser", "testPassword");
        assertTrue(loginSuccess, "Login should be successful.");
    }

    @Test
    void testLoginWrongPassword() {
        loginManager.register("testUser", "testPassword");
        boolean loginFail = loginManager.login("testUser", "wrongPassword");
        assertFalse(loginFail, "Login with wrong password should fail.");
    }

    @Test
    void testLoginNonExistentUser() {
        boolean loginFail = loginManager.login("nonExistentUser", "anyPassword");
        assertFalse(loginFail, "Login for non-existent user should fail.");
    }

    @Test
    void testSaveAndLoadProgress() {
        loginManager.register("testUser", "testPassword");
        loginManager.saveProgress("testUser", 3);

        // Create a new instance to simulate a program restart
        loginManager = new LoginManager();
        int progress = loginManager.getUserProgress("testUser");
        assertEquals(3, progress, "Progress should be correctly loaded after restart.");
    }

    @Test
    void testOverwriteLowerProgress() {
        loginManager.register("testUser", "testPassword");
        loginManager.saveProgress("testUser", 5);
        loginManager.saveProgress("testUser", 2);  // Shouldn't lower progress

        int progress = loginManager.getUserProgress("testUser");
        assertEquals(5, progress, "Progress should not be overwritten by a lower value.");
    }

    @Test
    void testDefaultProgressForNewUser() {
        int progress = loginManager.getUserProgress("newUser");
        assertEquals(1, progress, "Default progress should be Level 1 for new users.");
    }

    // Utility method for simulating file content
    private void writeToFile(String content) throws IOException {
        Files.write(Path.of(TEST_FILE_PATH), content.getBytes(), StandardOpenOption.CREATE);
    }
}
