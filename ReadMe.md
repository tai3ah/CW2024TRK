### **__GitHub Link :__**


### **__Compilation instructions :__**

1. *Install IntelliJ IDEA Community* if not already installed.
2. *Open Project* in IntelliJ via File > Open....
3. *Load Maven Dependencies*: IntelliJ auto-loads dependencies. If not, click the Maven tool window and refresh.
4. *Set Correct JDK*: Ensure the project uses a compatible JDK (Java 8+).
5. *Build Project*: Select Build > Build Project.
6. *Run Application*: Open Main.java and Click the green Run button. Run Current File.
7. *Play the Game*: The game window will open. Enjoy!
   *Dependencies*: Maven for dependencies,JavaFX for UI.

## **__Implemented and Working__** 


**1. Menus:**
- **Main Menu Overlay:** Appears on game start, whenever return to main menu is clicked. Contains **"Play"** and **"Quit"** buttons.
- **Pause Menu Overlay:** Triggered by clicking the pause button. Contains **"Resume"** (resumes after countdown) and **"Quit Level"** (returns to the main menu). 
- **Win/Loss End Screen Overlay:** Displays after level completion. Shows **"Play Again"**, **"Next Level"** and **"Main Menu"**. **"Next Level"** appears only if the player wins. 
- **Game Completion Screen Overlay:** Displays victory message after the final level with a **"Main Menu"** button.
---
**2. Improved Movement Patterns:**  
- Supports four-directional movement (**Up**, **Down**, **Left**, **Right**).  
- Ensures the player stays within screen limits.  
- Movement logic follows **SRP (Single Responsibility Principle)** for easier future upgrades.
---
**3. Login System**
- The user details are dynamically saved on progress.txt
- **Login and Guest Play Options:**
    - Users can **log in** (click login and play) or **play as a guest.**
    - **Guest Play:**  (by just clicking the 'play as guest' button) Progress is **not saved** when playing as a guest.

- **Progress Saving:**
    - When logged in, the user’s game **progress is saved.**
    - Upon the next login, users can **resume the game** from where they left off.

- **New User Registration:**
    - **New users** can **register** by entering a username and password and creating an account.
    - After registration, users can **log in** to access game features and **save progress.**

---
**4. New Levels:**  

- **Expanded Gameplay:**
    - Two new levels, **Level Three** and **Level Four**, have been added, each introducing unique gameplay elements, enemies, and mechanics.
    - Each level has a distinct background, enemy types, and gameplay dynamics, advancing in difficulty.

- **Gameplay Adjustment:**
    - Removed the original mechanic where the player lost a life if an enemy penetrated the player’s defenses.
    - Instead, enemies passing through the screen are **destroyed but not counted as kills**, enabling continuous enemy spawning without penalizing the player.

---

 **Level Three Overview:**

- **Main Enemy:** Final Boss (an enemy plane).
- **Behavior:**
    - Moves in **randomized patterns** across all directions on the screen.
    - **Shoots projectiles** at the player plane while maneuvering.

- **Health Mechanic:**
    - Final Boss has a health bar that **decreases with each hit** from the player’s bullets.

- **Challenge Mechanic:**
    - **Time-Limited Level:**
        - A **25-second timer** is displayed at the top of the screen.
        - **Objective:** Destroy the Final Boss within the time limit to win the level.
        - **Consequence:** If the player **fails** to destroy the boss before the timer runs out, they **lose the level.**

- **Power-Up Mechanic:**
    - **Time Extension Power-Up:**
        - **Randomly Appears** on the screen.
        - **Effect:** If collected, it **adds 5 seconds** to the timer.
        - **Missed Collection:** If not collected within its appearance window, the power-up **disappears automatically.**

---

**Level Four Overview:**

- **Main Enemy:** Drone (Boss) + Level One Enemy Planes (Reintroduced).

- **Drone Behavior:**
    - Moves **horizontally from left to right** at the top of the screen.
    - **Drops bombs** that cause the player to lose a life upon contact.

- **Boss Fight Mechanic:**
    - The drone requires the player to **shoot it multiple times** until its **health reaches zero.**
    - **Additional Enemies:** Level One enemy planes continuously spawn, requiring the player to **manage multiple threats** simultaneously.

- **Advanced Challenge Mechanic:**
    - **Hidden Timer:**
        - If the player kills the Drone **before** reaching the kill target, a **10-second timer** is activated.
        - **Consequence:**
            - If the player **fails** to reach the kill target within 10 seconds, a **new Drone respawns**, restarting the boss fight while preserving the player's kill progress.

- **Health Power-Up Mechanic:**
    - **Hearts** randomly appear across the screen and **remain for 7 seconds.**
    - **Collection Benefit:**
        - Collecting a heart **increases the player's lives by one.**
        - **Missed Collection:** If the player **fails to collect** the heart within the time window, it **disappears automatically.**  

---

**5. Audio Features**

- **Background Music:**
    - A **continuous soundtrack** plays during gameplay, enhancing the game’s immersive atmosphere.

- **Sound Effects Implemented:**
    - **Win Sound:** Plays when the player **wins a level.**
    - **Lose Sound:** Plays when the player **loses a level.**

---
**6. Improved Hitboxes (Bug-Fix Implementation outside of code)**

- **Problem:**
    - Original hitboxes were **too large**, causing:
        - **Unfair collisions** where the player lost lives even when projectiles visually missed them.
        - **Incorrect kill counts** when the player’s projectiles missed enemies but still registered hits.

- **Solution:**
    - **Cropped blank spaces** from all relevant images.
    - Ensured **precise collision detection** by adjusting hitbox boundaries.

- **Result:**
    - **Accurate collision detection** during gameplay.
    - **Fair and balanced gameplay experience** with reduced collision-related bugs.  
---
## **Implemented but Not Working Properly**
-Managed to fix bugs.
---
## **Features Not Implemented**

1. **Lightning Storm Feature in Level 3:**
    - **Planned:**
        - The lightning storm was intended to **increase difficulty** by periodically reducing visibility on the screen.
    - **Reason for Not Implementing:**
        - The feature **did not work as expected**, causing **significant gameplay issues**.
        - After several failed attempts, I **had to remove** it to focus on higher-priority features.

2. **Level 5 with Advanced Features:**
    - **Planned:**
        - A fifth level with **unique enemies, advanced mechanics, and challenging gameplay** was planned.
    - **Reason for Not Implementing:**
        - Due to **time constraints**, I could not allocate the necessary development time to **design, implement, and test** this additional level.

3. **Additional Audio Features:**
   - **Button-Click Sounds:** Audio feedback for navigating menus.
   - **Explosion Sounds:** Unique sound effects for projectile-plane collisions.
   - **Shield Activation Sound:** For the boss shield in **Level Two.**
   - **Power-up Collection Sound:** When the player collects a power-up in level 3 or level 4.  
   
---
## **New Java Classes and new Packages**

### **New Packages** 
As part of the refactoring process to enhance code organization, maintainability, and readability, several new packages were created. This structure follows best practices such as separation of concerns and modularity, aligning with the SOLID principles.

1. **`actors`**  
   Contains all game elements such as the player's plane, enemies, projectiles, and other in-game entities. This separation ensures better management of game components and supports easier expansion.

2. **`factories`**  
   Implements the **Factory Design Pattern** to create instances of game elements like enemies and projectiles. This design pattern simplifies object creation and promotes code reusability.

3. **`levels`**  
   Contains all classes related to individual game levels. Each level's unique logic, background, and objectives are encapsulated here, supporting better scalability and game progression management.

4. **`managers`**  
   Manages game functionality, including collision detection, score tracking, sound management, and game state management. This package centralizes important game logic, ensuring the game runs smoothly and is easier to debug and enhance.

5. **`ui`**  
   Contains classes for all **User Interface (UI)** elements such as menus, overlays, and buttons. By separating UI logic, the game’s front-end elements are more maintainable and visually distinct from game mechanics.

These packages were introduced to improve the overall project structure, adhering to **Single Responsibility Principle (SRP)** and supporting **modularity**, **readability**, and **future scalability.**

### **New Classes** 
Below are the newly added classes, organized by their packages, with brief descriptions of their purposes.

---

- **`com.example.demo.levels.LevelBuilder`**: Manages level transitions and initializes levels.
- **`com.example.demo.levels.LevelThree`**: Contains game logic, background, and events for Level 3.
- **`com.example.demo.levels.LevelFour`**: Contains game logic, background, and events for Level 4.

---

- **`com.example.demo.actors.FinalBoss`**: Represents the final boss in Level 3.
- **`com.example.demo.actors.FinalBossProjectile`**: Manages projectiles shot by the Final Boss in Level 3.
- **`com.example.demo.actors.Drone`**: Represents the drone enemy in Level 4.
- **`com.example.demo.actors.DroneBomb`**: Manages bombs dropped by the drone in Level 4.

---
**Factory Design Pattern Implemented :** Used for creating game elements (planes, buttons, and screens).
- **`com.example.demo.factories.BossPlaneFactory`**: Creates boss planes for different levels.
- **`com.example.demo.factories.DroneFactory`**: Creates drone objects for Level 4.
- **`com.example.demo.factories.EnemyFactory`**: General factory for creating various enemies.
- **`com.example.demo.factories.EnemyPlaneFactory`**: Produces enemy planes for specific levels.
- **`com.example.demo.factories.FinalBossFactory`**: Generates the Final Boss for Level 3.
- **`com.example.demo.factories.GameButtonFactory`**: Manages creation of various game buttons.
- **`com.example.demo.factories.GameCompletionScreenFactory`**: Creates the game completion screen at end of level 4.
- **`com.example.demo.factories.LevelEndScreenFactory`**: Generates the win/loss screen at the end of each level.
- **`com.example.demo.factories.PauseButtonFactory`**: Creates the pause button displayed in the game.
- **`com.example.demo.factories.PlayButtonFactory`**: Creates the play button for the main menu.
- **`com.example.demo.factories.QuitButtonFactory`**: Creates the quit button for exiting the game.
- **`com.example.demo.factories.QuitLevelButtonFactory`**: Creates the quit level button for navigating to the main menu.
- **`com.example.demo.factories.ResumeButtonFactory`**: Creates the resume button used in the pause menu.
- **`com.example.demo.factories.LoginButtonFactory`**
- **`com.example.demo.factories.PlayAgainButtonFactory`**
- **`com.example.demo.factories.QuitLevelButtonFactory`**

---

- **`com.example.demo.managers.CollisionManager`**: Detects and manages in-game collisions between different objects.
- **`com.example.demo.managers.InputHandler`**: Manages user input events such as key presses.
- **`com.example.demo.managers.LoginManager`**: Handles user login, registration, and game progress saving.
- **`com.example.demo.managers.PauseManager`**: Manages game pausing and resuming, including the pause overlay.
- **`com.example.demo.managers.SoundManager`**: Manages background music, sound effects, and audio playback.

---

- **`com.example.demo.ui.LevelSelectionPage`**: Displays a page for selecting game levels once a user has logged in.
- **`com.example.demo.ui.LevelViewLevelThree`**: Handles UI for Level 3.
- **`com.example.demo.ui.LevelViewLevelFour`**: Handles UI for Level 4.
- **`com.example.demo.ui.LoginPage`**: Manages the login interface, including registration and login options.
- **`com.example.demo.ui.MainMenuPage`**: Manages the main menu interface with options like 'play' , 'login and play' and 'quit'.

---

These additions were designed to improve code organization, support new features, and enhance the overall maintainability of the project. The separation of logic into distinct classes follows best practices like the **Single Responsibility Principle (SRP)** and **Factory Design Pattern**, ensuring scalability and cleaner project structure.

## **Modified Java Classes**

### **`com.example.demo.levels.LevelParent`**

#### **Overview:**
The `LevelParent` class was significantly refactored to improve game structure, enhance maintainability, and follow best coding practices. This refactor focused on creating modular components, simplifying complex logic, and improving game performance.

---

### **Key Changes:**

- **Improved Code Structure:**
    - Replaced JavaFX `Group` with `Pane` for better UI control and cleaner layout management.
    - Separated game logic into individual classes, reducing class responsibility and ensuring Single Responsibility Principle (SRP).

- **Game Logic Separation:**
    - **Collision Logic:** Moved to `com.example.demo.managers.CollisionManager` to handle collisions independently.
    - **Input Handling:** Extracted to `com.example.demo.managers.InputHandler` to manage player controls and key events.
    - **Level Transitioning Logic:** Delegated to `com.example.demo.factories.LevelEndScreenFactory`, improving the flow between game levels and overlays.

- **New Game Features:**
    - Integrated a **pause system**, **sound management**, and **user login and progress saving**, implemented across dedicated classes.
    - Corrected hitbox collision detection by adjusting sprite bounds for better gameplay accuracy.
    - Improved game flow using clean event-driven level transitions.

---

### **Design Patterns Applied:**
- **Singleton Pattern:** Used for managing shared instances like `SoundManager` and `LoginManager`.
- **Observer Pattern:** Applied for level transitions, ensuring seamless game progress updates.

---

### **Why These Changes Matter:**
- **Maintainability:** The modular structure makes the code easier to maintain, update, and debug.
- **Readability:** Improved readability by clearly separating game logic into distinct classes.
- **Performance:** Improved efficiency due to streamlined logic and eliminated redundant code.
- **Extensibility:** Simplified adding new features such as new levels, power-ups, and sounds due to modular design.

---
### **`com.example.demo.ui.LevelViewLevelOne` (previously `com.example.demo.LevelView`)**

#### **Reason for Name Change:**
The class was renamed from `LevelView` to `LevelViewLevelOne` to reflect its specific purpose of managing UI elements for **Level One**.
(Other level-views do inherit the basic ui methods that are required across all levels though)
---

### **Key Changes:**

- **Separation of Responsibilities:**  
  The class now focuses only on managing **Level One**'s specific UI components, simplifying code structure and enabling easier expansion for future levels.

- **Kill Count Display Added:**
    - Introduced a kill count display that updates dynamically as enemies are defeated.
    - Positioned the kill count at the top-right corner with responsive alignment to the screen size.

- **Heart Display Management Improved:**
    - Enhanced heart display logic with methods to **add**, **remove**, and **update** hearts based on player health.
    - The heart display is now modular, improving code clarity and enabling potential reuse.

- **Removed Unnecessary Features:**
    - Removed unrelated components like **WinImage** and **GameOverImage**, delegating those responsibilities to more appropriate classes such as `LevelEndScreenFactory` and `LevelCompletionScreenFactory`.

---

### **Why These Changes Matter:**
- **Maintainability:** Separating UI management into level-specific classes reduces complexity and makes the code easier to maintain.
- **Scalability:** Adding new levels only requires creating new UI classes without affecting other levels.
- **Readability:** The code structure is clearer, focusing solely on Level One's unique requirements.

---
### `com.example.demo.actors.GameEntity`

### **Reason for Refactor:**
The original design involved three separate components: `ActiveActor`, `Destructible`, and `ActiveActorDestructible`. This structure created unnecessary complexity with multiple layers of inheritance and interfaces. To simplify the architecture and improve code maintainability, these components were merged into a single class named `GameEntity`.

---

### **Key Improvements:**

- **Unified Class Design:**  
  Combined `ActiveActor`, `Destructible`, and `ActiveActorDestructible` into a single class `GameEntity`, reducing complexity and enhancing readability.

- **Encapsulation of Properties:**
    - Added `isDestroyed` as a private field with public getters and protected setters.
    - Centralized initialization logic in a single constructor.

- **Core Game Logic:**
    - Integrated core functionalities like movement (`moveHorizontally`, `moveVertically`), damage handling (`takeDamage`, `destroy`), and graphical initialization.
    - Maintained the abstract methods `updatePosition()` and `updateActor()` for subclass-specific implementations.

- **Image Initialization Fix:**
    - Ensured the correct file path for images using `getClass().getResource()`.

---

### **Why These Changes Matter:**

- **Simpler Codebase:** Reduced the need for multiple inheritance and interfaces, making the code easier to follow and maintain.

- **Encapsulation & Modularity:** Centralized game-entity management ensures properties and behaviors are easier to track and modify.

- **Reusability & Extensibility:** Subclasses can now inherit from `GameEntity`, ensuring consistent initialization and behavior across all game elements.

- **Game Performance:** Reduced the complexity of class dependencies, leading to potentially better game performance and reduced technical debt.

---
### `com.example.demo.controller.Controller`

### **Reason for Refactor:**
The original class relied on Java's outdated `Observable` and `Observer` interfaces, which have been deprecated. Additionally, the use of reflection for level loading introduced unnecessary complexity and potential runtime errors. These changes simplify level transitions, enhance readability, and ensure a more modern, maintainable codebase.

---

### **Key Improvements:**

- **Removed Reflection Logic:**  
  Eliminated the use of Java Reflection API, replacing it with the `LevelBuilder` class for level creation, adhering to the Factory Method design pattern.

- **Replaced Observer Pattern:**  
  Replaced the deprecated `Observable` and `Observer` interfaces with a custom listener-based approach using `Consumer<String>` for level changes.

- **Centralized Game Management:**  
  Centralized the game management process, including game launching, level transitions, and resource cleanup.

- **Error Handling Enhancement:**  
  Added better error handling with JavaFX `Alert` dialogs for better user experience.

- **Resource Cleanup Logic:**  
  Introduced a `cleanupCurrentLevel()` method that stops the timeline and clears UI elements to prevent memory leaks.

---

### **Why These Changes Matter:**

- **Simplified Level Management:**  
  Improved readability and code simplicity by removing reflective class loading and deprecated interfaces.

- **Code Maintainability:**  
  Reduced code complexity and improved maintainability through cleaner separation of responsibilities.

- **Improved User Experience:**  
  Enhanced error handling ensures users receive meaningful feedback when issues occur during level transitions.

- **Scalability and Flexibility:**  
  The new design supports easier level additions and custom level transitions, ensuring long-term project scalability.
---
### `com.example.demo.actors.Boss`

---

### **Reason for Refactor:**
The original `Boss` class lacked integration with game UI updates, making it difficult to reflect changes in health and shield status visually. To improve game functionality, a better UI connection, enhanced movement logic, and the addition of a shield visual effect were implemented.

---

### **Key Improvements:**

1. **UI Integration:**
    - Added `LevelViewLevelTwo` reference to update the boss’s health dynamically on the screen.
    - Introduced the `ShieldImage` class to visually represent the boss's shield during gameplay.

2. **Shield Mechanics Enhancement:**
    - Shield visibility is toggled in real-time using `ShieldImage`.
    - Shield activation logic is improved, with clear conditions for enabling and disabling the shield.

3. **Refined Movement Logic:**
    - The boss's vertical movement is now responsive and bound-checked against the screen limits.
    - `updatePosition()` synchronizes shield positioning with the boss’s movement.

4. **Projectile Firing Logic:**
    - Projectiles are now fired based on a predefined probability with accurate Y-position calculations.

5. **Health Management and Damage Handling:**
    - Implemented real-time UI updates for the boss's health when taking damage.
    - Protected damage logic to prevent health reduction when the shield is active.

6. **Game Entity Refactor:**
    - Removed hardcoded values by using constants for screen boundaries and movement variables, ensuring flexibility and better code readability.

---

### **Why These Changes Matter:**

- **Improved Gameplay Feedback:**  
  The integration with UI classes like `LevelViewLevelTwo` provides immediate health updates, improving player experience.

- **Visual Enhancements:**  
  The addition of a shield animation adds depth to the gameplay, making the boss fight more dynamic and visually engaging.

- **Code Maintainability:**  
  By breaking down shield, projectile, and movement logic into distinct sections, the code is cleaner, more modular, and easier to maintain or extend.

- **Scalability and Flexibility:**  
  The updated `Boss` class can now be extended easily for future features, such as additional abilities, new levels, or different boss behaviors.
---
### `com.example.demo.ui.LevelViewLevelTwo`

---

### **Reason for Refactor:**
The original `LevelViewLevelTwo` only managed the boss’s shield visuals. It lacked a health display for the boss, making gameplay less informative. Additionally, the old class was tied to JavaFX's `Group`, limiting layout flexibility.

---

### **Key Improvements:**

1. **Boss Health Display:**
    - Added a dynamic health display for the boss using `Text`.
    - The health display updates in real-time whenever the boss takes damage.

2. **UI Integration Enhancements:**
    - Changed from using `Group` to `Pane` for better layout management.
    - Shield visibility management remains, ensuring the boss’s shield is toggled based on its status.

3. **Better UI Management:**
    - Added a method `updateBossHealth(int)` to update the boss’s health in the UI.
    - Used JavaFX's layout methods to ensure accurate placement of UI elements.

4. **Layout Consistency:**
    - All game-related UI elements (boss health, shield, etc.) are properly positioned and updated during gameplay.

---

### **Why These Changes Matter:**

- **Improved Player Experience:**  
  Players can now see the boss’s health, making the fight more engaging and less confusing.

- **Visual Feedback Integration:**  
  Real-time health updates and shield toggling add dynamic feedback to the gameplay.

- **Code Structure and Maintainability:**  
  Using `Pane` instead of `Group` simplifies layout management, improving code readability and future scalability.  
---
###  `com.example.demo.levels.LevelOne`

---

### **Reason for Refactor:**
The original `LevelOne` class was tightly coupled with game initialization, enemy spawning, and level transition logic. This refactor aimed to improve maintainability, readability, and scalability by applying the Factory and Builder design patterns and enhancing game experience features.

---

### **Key Improvements:**

1. **Enhanced Level Initialization:**
    - **Intro Screen Added:** A new intro screen displays level objectives before the game starts.
    - **Improved Game Start Logic:** The game only begins after the intro screen is shown for 4 seconds.

2. **Game Entity Management:**
    - **Factory Pattern Integration:**
        - An `EnemyPlaneFactory` was introduced to create enemy planes, replacing direct object creation.
    - **Separation of Concerns:**
        - Spawning logic now relies on the factory, decoupling object creation from game logic.

3. **Better Level Progression:**
    - **Level Transition Logic:**
        - Level transitioning was moved to a new method `goToNextLevel()` to align with other level implementations.

4. **UI and Game State Improvements:**
    - **Kill Count and Health Display:** Managed via a new `LevelViewLevelOne` class.
    - **Background Image Update:** Changed from `background1.jpg` to `background1.png` for consistency in asset management.

5. **Code Simplification and Organization:**
    - Improved readability by reorganizing fields and methods.
    - Extracted kill-checking logic to a dedicated method `userHasReachedKillTarget()`.

---

### **Why These Changes Matter:**

- **Enhanced Player Experience:**  
  The intro screen sets clear gameplay expectations, improving engagement and player focus.

- **Maintainability and Readability:**  
  By using the Factory and Builder patterns, the game logic is cleaner, easier to understand, and more modular.

- **Scalability:**  
  This refactor makes adding new levels and features simpler, supporting future game expansion.  
---
### `com.example.demo.levels.LevelTwo`

---

### **Reason for Refactor:**
The original `LevelTwo` class was tightly coupled with enemy creation, UI updates, and gameplay logic, leading to a rigid design. The refactor focused on simplifying class responsibilities by applying the Factory design pattern, decoupling the boss creation process, and enhancing level initialization.

---

### **Key Improvements:**

1. **Enhanced Level Initialization:**
    - **Intro Screen Added:**
        - An introductory screen displays level instructions before the game starts, improving clarity and gameplay immersion.

2. **Boss Plane Management:**
    - **Factory Integration:**
        - Replaced direct boss creation with `BossPlaneFactory`, ensuring better scalability and following the Factory design pattern.
    - **Dynamic Boss Health Updates:**
        - The boss’s health display is dynamically updated through a link with the `LevelViewLevelTwo` class.

3. **Separation of Concerns:**
    - **Decoupled Level Logic:**
        - Transitioning to the next level was extracted to a dedicated `goToNextLevel()` method.
    - **Scene Update Logic:**
        - Moved critical UI updates (e.g., health display) to run on JavaFX’s application thread using `Platform.runLater()`.

4. **Improved User Experience:**
    - **Boss Shield Mechanics Enhanced:**
        - The boss’s shield image is managed within the new UI class `LevelViewLevelTwo`, ensuring clear separation between game logic and UI.
    - **Better Background Management:**
        - The background image was updated to a higher-quality `.png` file for better visuals.

---

### **Why These Changes Matter:**

- **Maintainability and Readability:**  
  Clear separation of the boss creation, UI management, and level transitioning logic simplifies the class structure, making the code easier to understand and extend.

- **Scalability:**  
  By using a factory for the boss, the game supports easier future extensions, such as adding new enemies or bosses.

- **User Experience:**  
  A visually enhanced interface and interactive boss fight improve gameplay engagement and provide real-time feedback to the player.  
---
### `com.example.demo.ui.ShieldImage`

---

### **Reason for Refactor:**
The original `ShieldImage` class was too simplistic, focusing only on initialization and visibility control. The refactor aimed to improve its functionality by adding dynamic position updates and better logging for debugging purposes.

---

### **Key Improvements:**

1. **Enhanced Image Quality:**
    - **Updated Image Source:**
        - Replaced the original shield image file with a higher-quality `.png` file for better in-game visuals.

2. **Dynamic Position Updates:**
    - **Position Update Method Added:**
        - Introduced `updatePosition(double x, double y)` to allow the shield image to move along with its corresponding game entity.

3. **Improved Debugging:**
    - **Console Logs Added:**
        - Added clear debug logs when the shield is activated or deactivated, helping track game state changes during development.

4. **Visual Adjustments:**
    - **Resized Shield Image:**
        - Increased the shield’s size from `200px` to `400px` to enhance its visibility during gameplay.

---

### **Why These Changes Matter:**

- **Maintainability:**  
  By adding a position update method, the shield’s behavior becomes easier to manage and decouple from other classes.

- **Visual Clarity:**  
  The larger, clearer shield image improves the game’s visual presentation and makes gameplay mechanics more intuitive.

- **Debug Support:**  
  Console logs aid in testing and debugging, ensuring easier troubleshooting of the shield's activation and position management.  
---
### `com.example.demo.actors.UserPlane`

---

### **Reason for Refactor:**
The original `UserPlane` class supported only vertical movement and basic projectile firing. The refactor introduced four-directional movement, enhanced firing logic, and better health management, improving both player control and gameplay dynamics.

---

### **Key Improvements:**

1. **Enhanced Movement Capabilities:**
    - **Added Four-Directional Movement:**
        - The player can now move **up, down, left, and right**.
        - Boundaries are enforced to prevent movement outside the screen.

2. **Advanced Firing Logic:**
    - **Projectile Launch Update:**
        - The projectile firing position was adjusted for more accurate launches from the plane's center.

3. **Player Health Management:**
    - **Health Increase Logic:**
        - Implemented a method to **increase player health** dynamically, improving gameplay flexibility.

4. **Refactored Update Logic:**
    - **Clean Movement Update:**
        - Improved `updatePosition()` for more precise and responsive movement.

5. **Improved Gameplay Feedback:**
    - **Kill Count Tracking:**
        - Retained the original kill tracking logic but ensured better integration with other gameplay systems.

---

### **Why These Changes Matter:**

- **Player Experience:**  
  Four-directional movement and accurate projectile firing offer a more **engaging** and **interactive** experience.

- **Maintainability:**  
  Adding health management and projectile adjustments through clear methods makes the class **easier to extend** in future updates.

- **Gameplay Balance:**  
  Better player control and the ability to restore health improve **gameplay fairness**, especially in later, harder levels.  
---
### `com.example.demo.actors.FighterPlane`

---

### **Reason for Refactor:**
The original `FighterPlane` class was closely tied to game logic with limited reusability. The refactor generalized the plane's core properties like health and projectile handling while extending from the refactored `GameEntity` class. This separation enhances modularity and reusability.

---

### **Key Improvements:**

1. **Inheritance and Abstraction:**
    - Refactored to extend `GameEntity`, enabling better code reuse.
    - Maintains core fighter-plane behaviors like firing projectiles and taking damage.

2. **Health Management:**
    - Health tracking and damage-taking logic remain central to the class.
    - Enhanced with a clear check for when health reaches zero (`healthAtZero()`).

3. **Projectile Firing System:**
    - Abstract method `fireProjectile()` retained for specialized subclasses.
    - Clear methods for calculating projectile spawn coordinates ensure flexibility in projectile placement.

4. **Code Modularity and Readability:**
    - **Encapsulated Logic:** Damage handling, health checking, and projectile position calculations are clearly defined.
    - **Simplified Design:** Unnecessary dependencies from the original version removed.

---

### **Why These Changes Matter:**

- **Modularity and Extensibility:**  
  The class now serves as a **core abstraction** for any plane-like game entity. Specialized classes like `UserPlane` and `Boss` can build on this foundation.

- **Simplified Game Logic:**  
  Health and projectile management are centralized, reducing duplicate code.

- **Enhanced Readability and Maintenance:**  
  The updated class design follows **clean code principles**, making future extensions easier.  
---
### `com.example.demo.actors.UserProjectile` 

---

### **Reason for Refactor:**
The original `UserProjectile` class had minimal functionality and was tied to fixed image dimensions. The refactor focuses on enhancing flexibility, improving readability, and ensuring consistency with other game entities.

---

### **Key Improvements:**

1. **Updated Inheritance:**
    - Refactored to extend the improved `Projectile` class, simplifying logic and ensuring consistent projectile behavior.

2. **Image and Size Adjustments:**
    - **Image Size Reduced:**  
      Original height reduced from `125` to `10`, ensuring better hitbox accuracy.

3. **Position Management:**
    - Clear separation of **position updates** and **actor updates**.
    - Use of `moveHorizontally()` ensures the projectile moves smoothly to the right.

4. **Readability and Maintenance:**
    - Simplified design with clear, self-documenting methods.
    - Constructor ensures easy initialization with minimal parameters.

---

### **Why These Changes Matter:**

- **Improved Gameplay Accuracy:**  
  Reducing the projectile's height ensures **precise collisions**.

- **Clean Code Principles:**  
  Methods like `updatePosition()` and `updateActor()` follow **Single Responsibility Principle (SRP)**.

- **Extensibility:**  
  The refactor allows future **custom projectile types** to be created using the same structure.  
---
### `com.example.demo.actors.BossProjectile`**

---

### **Reason for Refactor:**
The original `BossProjectile` class was refactored to improve clarity, maintainability, and consistency with the revised game architecture.

---

### **Key Improvements:**

1. **Code Organization:**
    - Moved to the `actors` package, aligning with the overall refactor strategy.

2. **Encapsulation and Readability:**
    - Constructor parameter names clarified for easier understanding.
    - Organized constants for better readability and future modifications.

3. **Position Management:**
    - `updatePosition()` and `updateActor()` clearly separated for better **Single Responsibility Principle (SRP)** adherence.

4. **Simplified Design:**
    - Constructor remains minimal while ensuring correct initialization.
    - Consistent velocity management using `moveHorizontally()` improves maintainability.

---

### **Why These Changes Matter:**

- **Readability & Structure:**  
  Organized attributes and methods ensure that the class is **self-documenting**.

- **Maintainability:**  
  Future additions like **custom projectiles** or **enhanced fire logic** can be implemented without modifying the core structure.

- **Consistency:**  
  Refactor ensures similar behavior and structure with other projectile-based classes, enabling **code reuse** and reducing **duplicate logic**.
---
## **Unexpected Problems Encountered**

---

1. **Major Project Setback Due to Critical Error:**
    - **Issue:** Encountered a significant, unresolved error that persisted even after reverting the project to its last known working state.
    - **Action Taken:** As a last resort, the entire project had to be deleted, re-forked, and cloned from the source code, forcing a complete restart from scratch.
    - **Impact:** This caused a **two-week setback**, leading to severe time constraints. As a result, many planned features and improvements could not be implemented successfully due to limited development time.


