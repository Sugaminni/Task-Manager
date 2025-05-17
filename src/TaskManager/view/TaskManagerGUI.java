package TaskManager.view;

import TaskManager.model.Task;
import TaskManager.service.TaskManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.layout.Priority;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManagerGUI extends Application {

    private ListView<Task> taskListView;
    private TaskManager taskManager;
    private TextField searchBar;

    @Override
    public void start(Stage primaryStage) {
        // Initialize task manager
        taskManager = new TaskManager();

        // Root layout
        HBox root = new HBox();
        root.setStyle("-fx-background-color: #2e2e2e;");

        // Sidebar layout adjustment
        VBox sidebar = new VBox(10);
        sidebar.setPrefWidth(150);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setStyle("-fx-background-color: #3e3e3e; -fx-border-color: #505050;");

        // Search bar
        searchBar = new TextField();
        searchBar.setPromptText("🔍 Search Tasks");
        searchBar.setStyle("-fx-background-color: #3e3e3e; -fx-text-fill: #dcdcdc; -fx-border-color: #505050; -fx-font-size: 14px; -fx-padding: 5px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        searchBar.setOnKeyReleased(this::performSearch);

        // Task list view
        taskListView = new ListView<>();
        taskListView.setPlaceholder(new Label("No tasks available"));
        taskListView.setStyle("-fx-background-color: #424242; -fx-text-fill: #dcdcdc;");
        HBox.setHgrow(taskListView, Priority.ALWAYS);

        // Sidebar labels with separators
        Label addTaskLabel = createSidebarLabel("➕ Add Task");
        Label deleteTaskLabel = createSidebarLabel("🗑️ Delete Task");
        Label editTaskLabel = createSidebarLabel("✏️ Edit Task");
        Label viewTasksLabel = createSidebarLabel("👁️ View Tasks");
        Label filterTasksLabel = createSidebarLabel("🔄 Filter Tasks");

        // Add event handlers for label clicks
        addTaskLabel.setOnMouseClicked(e -> addTask(taskListView));
        deleteTaskLabel.setOnMouseClicked(e -> deleteTask());
        editTaskLabel.setOnMouseClicked(e -> editTask());
        viewTasksLabel.setOnMouseClicked(e -> viewTasks());
        filterTasksLabel.setOnMouseClicked(e -> filterTasks());

        // Add labels with separators to sidebar
        sidebar.getChildren().addAll(searchBar, addTaskLabel, new Separator(), deleteTaskLabel, new Separator(), editTaskLabel, new Separator(), viewTasksLabel, new Separator(), filterTasksLabel);

        // Add sidebar and task list to root layout
        root.getChildren().addAll(sidebar, taskListView);

        // Scene setup
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Create a styled label for the sidebar
    private Label createSidebarLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color: transparent; -fx-text-fill: #dcdcdc; -fx-font-size: 16px; -fx-padding: 8px; -fx-alignment: center-left;");


        // Hover effect
        label.setOnMouseEntered(e -> label.setStyle("-fx-background-color: #505050; -fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-padding: 8px; -fx-alignment: center-left;"));
        label.setOnMouseExited(e -> label.setStyle("-fx-background-color: transparent; -fx-text-fill: #dcdcdc; -fx-font-size: 16px; -fx-padding: 8px; -fx-alignment: center-left;"));

        return label;
    }

    // Method to search tasks based on keyword input
    private void performSearch(KeyEvent event) {
        String query = searchBar.getText().trim().toLowerCase();

        // Show all tasks if the search bar is empty
        if (query.isEmpty()) {
            taskListView.getItems().setAll(taskManager.getTasks());
            return;
        }

        // Show tasks that contain the keyword in the title or description
        List<Task> matchingTasks = taskManager.getTasks().stream()
                .filter(task -> task.getTitle().toLowerCase().contains(query) || task.getDescription().toLowerCase().contains(query))
                .collect(Collectors.toList());

        // Update the task list view with the matching tasks
        taskListView.getItems().setAll(matchingTasks);
    }


    // Placeholder methods for task actions
    private void addTask(ListView<Task> taskListView) {
        GUIUtility.addTask(taskListView);
        System.out.println("Add task clicked");
    }
    private void deleteTask() { System.out.println("Delete task clicked"); }
    private void editTask() { System.out.println("Edit task clicked"); }
    private void viewTasks() { System.out.println("View tasks clicked"); }
    private void filterTasks() { System.out.println("Filter tasks clicked"); }

    public static void main(String[] args) {
        launch(args);
    }
}
