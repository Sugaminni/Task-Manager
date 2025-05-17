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

    private ListView<Task> taskListView;

    @Override
    public void start(Stage primaryStage) {

        // Initialize the task list view
        taskListView = new ListView<>();
        taskListView.setPlaceholder(new Label("No tasks available"));

        // Menu Bar Setup
        // Creating a menu bar to hold the primary menu options
        MenuBar menuBar = new MenuBar();

        // File menu for actions related to tasks and application
        Menu fileMenu = new Menu("File");
        MenuItem addTaskItem = new MenuItem("Add Task");       // Option to add a new task
        addTaskItem.setOnAction(event -> GUIUtility.addTask(taskListView)); // Event connector for the add the task option to the method to add a task to the task list

        MenuItem exportItem = new MenuItem("Export Tasks");    // Option to export the task list
        MenuItem exitItem = new MenuItem("Exit");              // Option to exit the application
        fileMenu.getItems().addAll(addTaskItem, exportItem, new SeparatorMenuItem(), exitItem); // Adding items to the File menu

        // View the menu for displaying different task views
        Menu viewMenu = new Menu("View");
        MenuItem allTasksItem = new MenuItem("All Tasks");         // Shows all tasks
        MenuItem completedTasksItem = new MenuItem("Completed Tasks"); // Shows only completed tasks
        MenuItem pendingTasksItem = new MenuItem("Pending Tasks"); // Shows only pending tasks
        viewMenu.getItems().addAll(allTasksItem, completedTasksItem, pendingTasksItem); // Adding items to the View menu

        // Adding both menus to the menu bar
        menuBar.getMenus().addAll(fileMenu, viewMenu);

        // Status Bar Setup
        Label statusLabel = new Label("Ready");
        HBox statusBar = new HBox(statusLabel);
        statusBar.setStyle("-fx-padding: 5; -fx-background-color: #dcdcdc;");

        // Layout Setup using BorderPane
        BorderPane root = new BorderPane();
        root.setTop(menuBar);          // Menu bar at the top
        root.setCenter(taskListView);  // Task list in the center
        root.setBottom(statusBar);     // Status bar at the bottom

        // Scene Configuration
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Task Manager");   // Setting the window title
        primaryStage.setScene(scene);            // Assigning the scene to the primary stage
        primaryStage.show();                     // Displaying the window
    }

    // Method to display detailed information about a selected task
    private void showTaskDetails(Task task) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Task Details");
        alert.setHeaderText("Task: " + task.getTitle());
        alert.setContentText("Description: " + task.getDescription() + "\nPriority: " + task.getPriority() + "\nWorkload: " + task.getWorkload() + "\nDue Date: " + task.getDueDate());
        alert.showAndWait();
    }

    // Main method to launch JavaFX application
    public static void main(String[] args) {
        launch(args);
    }
}
