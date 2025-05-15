package TaskManager.view;

import TaskManager.model.Task;
import TaskManager.service.TaskManager;
import TaskManager.service.TaskService;
import TaskManager.service.TaskUtility;
import java.util.List;

// Importing necessary JavaFX classes
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class TaskManagerGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Menu Bar Setup
        // Creating a menu bar to hold the primary menu options
        MenuBar menuBar = new MenuBar();

        // File menu for actions related to tasks and application
        Menu fileMenu = new Menu("File");
        MenuItem addTaskItem = new MenuItem("Add Task");       // Option to add a new task
        MenuItem exportItem = new MenuItem("Export Tasks");    // Option to export task list
        MenuItem exitItem = new MenuItem("Exit");              // Option to exit the application
        fileMenu.getItems().addAll(addTaskItem, exportItem, new SeparatorMenuItem(), exitItem); // Adding items to File menu

        // View menu for displaying different task views
        Menu viewMenu = new Menu("View");
        MenuItem allTasksItem = new MenuItem("All Tasks");         // Show all tasks
        MenuItem completedTasksItem = new MenuItem("Completed Tasks"); // Show only completed tasks
        MenuItem pendingTasksItem = new MenuItem("Pending Tasks"); // Show only pending tasks
        viewMenu.getItems().addAll(allTasksItem, completedTasksItem, pendingTasksItem); // Adding items to View menu

        // Adding both menus to the menu bar
        menuBar.getMenus().addAll(fileMenu, viewMenu);

        // Task List Area Setup
        // Creating a ListView to display tasks in a list format
        ListView<String> taskListView = new ListView<>();
        // Adding some sample tasks for testing
        taskListView.getItems().addAll("Sample Task 1", "Sample Task 2", "Sample Task 3", "Sample Task 4");
        // Setting a placeholder message when the list is empty
        taskListView.setPlaceholder(new Label("No tasks available"));

        // Event Handling for Task Clicks
        // Adding an event listener for double-clicking a task in the list
        taskListView.setOnMouseClicked(event -> {
            // Check if the click count is 2 (double-click)
            if (event.getClickCount() == 2) {
                // Get the selected task from the list
                String selectedTask = taskListView.getSelectionModel().getSelectedItem();
                // If a task is selected, open the task details
                if (selectedTask != null) {
                    showTaskDetails(selectedTask); // Call the method to display task info
                }
            }
        });

        // Status Bar Setup
        // Creating a label to display application status messages
        Label statusLabel = new Label("Ready");
        // Creating an HBox to hold the status label
        HBox statusBar = new HBox(statusLabel);
        // Adding styling to the status bar for a clean look
        statusBar.setStyle("-fx-padding: 5; -fx-background-color: #dcdcdc;");

        // Layout Setup using BorderPane
        // BorderPane is used to structure the main components: top, center, and bottom
        BorderPane root = new BorderPane();
        root.setTop(menuBar);          // Menu bar at the top
        root.setCenter(taskListView);  // Task list in the center
        root.setBottom(statusBar);     // Status bar at the bottom

        // Scene Configuration
        // Creating a scene using the root layout with specified dimensions
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Task Manager");   // Setting the window title
        primaryStage.setScene(scene);            // Assigning the scene to the primary stage
        primaryStage.show();                     // Displaying the window
    }

    // Method to display detailed information about a selected task
    private void showTaskDetails(String task) {
        // Creating an alert of type INFORMATION to display task details
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Details");                      // Setting the title of the alert dialog
        alert.setHeaderText("Task: " + task);              // Setting the header text
        // Setting the content text to show the selected task and placeholder info
        alert.setContentText("Task description here\n[Edit and Delete options will be here]");
        alert.showAndWait();   // Displaying the alert and waiting for user interaction
    }

    // Main method to launch JavaFX application
    public static void main(String[] args) {
        launch(args);   // Launches JavaFX application
    }
}