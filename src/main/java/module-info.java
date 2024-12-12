/*module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.demo.controller to javafx.fxml;
    exports com.example.demo.controller;
    opens com.example.demo.levels to javafx.fxml;
    opens com.example.demo.ui to javafx.fxml;
    opens com.example.demo.actors to javafx.fxml;
    opens com.example.demo.managers to javafx.fxml;
}
*/

/**
 * Module definition for the Sky Battle game application.
 * <p>
 * This module requires JavaFX libraries for controls, FXML processing, and media playback.
 * It exports and opens packages to enable JavaFX runtime access and FXML-based UI loading.
 * </p>
 */
module com.example.demo {

    // Requires JavaFX modules for controls, FXML parsing, and media playback
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    /**
     * Opens the `com.example.demo.controller` package to JavaFX's FXML module
     * to allow JavaFX to reflectively access the controller classes.
     */
    opens com.example.demo.controller to javafx.fxml;

    /**
     * Exports the `com.example.demo.controller` package to make it available
     * to other modules and the JavaFX runtime.
     */
    exports com.example.demo.controller;

    /**
     * Opens the `com.example.demo.levels` package to JavaFX's FXML module
     * for FXML-based scene loading and controller access.
     */
    opens com.example.demo.levels to javafx.fxml;

    /**
     * Opens the `com.example.demo.ui` package to JavaFX's FXML module
     * for loading FXML files and related UI components.
     */
    opens com.example.demo.ui to javafx.fxml;

    /**
     * Opens the `com.example.demo.actors` package to JavaFX's FXML module
     * for enabling FXML-based instantiation of game entities.
     */
    opens com.example.demo.actors to javafx.fxml;

    /**
     * Opens the `com.example.demo.managers` package to JavaFX's FXML module
     * for reflective access to classes that manage game logic and state.
     */
    opens com.example.demo.managers to javafx.fxml;
}

