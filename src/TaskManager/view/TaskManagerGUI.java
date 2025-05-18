package TaskManager.view;

import TaskManager.model.Task;
import TaskManager.service.TaskManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Separator;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.control.ListCell;
import javafx.util.Callback;
import javafx.collections.ObservableList;
import javafx.scene.input.MouseEvent;
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

        // Sidebar layout for menu items
        VBox sidebar = new VBox(15);
        sidebar.setPrefWidth(160);
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

        // Custom cell factory for tasks with checkbox and star
        taskListView.setCellFactory(new TaskCellFactory());

        // Double-click event for editing/deleting tasks
        taskListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Task selectedTask = taskListView.getSelectionModel().getSelectedItem();
                if (selectedTask != null) {
                    handleDoubleClick(selectedTask);
                }
            }
        });

        // Sidebar labels for actions
        Label addTaskLabel = createSidebarLabel("➕ Add Task");
        Label filterTasksLabel = createSidebarLabel("🔄 Filter Tasks");
        Label exportTasksLabel = createSidebarLabel("📤 Export Tasks");
        Label importTasksLabel = createSidebarLabel("📥 Import Tasks");

        // Event handling for labels
        addTaskLabel.setOnMouseClicked(e -> GUIUtility.addTask(taskListView));
        filterTasksLabel.setOnMouseClicked(e -> GUIUtility.filterTasks());
       // exportTasksLabel.setOnMouseClicked(e -> exportTasks());
       // importTasksLabel.setOnMouseClicked(e -> importTasks());

        // Add labels to sidebar
        sidebar.getChildren().addAll(searchBar, addTaskLabel, new Separator(), filterTasksLabel, new Separator(), exportTasksLabel, new Separator(), importTasksLabel);

        // Add sidebar and task list to root layout
        root.getChildren().addAll(sidebar, taskListView);

        // Scene setup
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("Task Manager");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void performSearch(javafx.scene.input.KeyEvent event) {
        String query = searchBar.getText().trim().toLowerCase();
        List<Task> matchingTasks = taskManager.getTasks().stream()
                .filter(task -> task.getTitle().toLowerCase().contains(query) || task.getDescription().toLowerCase().contains(query))
                .collect(Collectors.toList());
        taskListView.getItems().setAll(matchingTasks);
    }


    // Create a styled label for the sidebar
    private Label createSidebarLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-background-color: transparent; -fx-text-fill: #dcdcdc; -fx-font-size: 16px; -fx-padding: 8px; -fx-alignment: center-left;");


        // Hover effect for interactivity
        label.setOnMouseEntered(e -> label.setStyle("-fx-background-color: #505050; -fx-text-fill: #ffffff; -fx-font-size: 16px; -fx-padding: 8px; -fx-alignment: center-left;"));
        label.setOnMouseExited(e -> label.setStyle("-fx-background-color: transparent; -fx-text-fill: #dcdcdc; -fx-font-size: 16px; -fx-padding: 8px; -fx-alignment: center-left;"));


        return label;
    }

    // Custom cell factory for tasks
    private class TaskCellFactory implements Callback<ListView<Task>, ListCell<Task>> {
        @Override
        public ListCell<Task> call(ListView<Task> listView) {
            return new ListCell<>() {
                @Override
                protected void updateItem(Task task, boolean empty) {
                    super.updateItem(task, empty);
                    if (task == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        HBox cellLayout = new HBox(10);
                        CheckBox checkBox = new CheckBox();
                        checkBox.setSelected(task.isComplete());
                        Label taskLabel = new Label(task.getTitle());
                        Label starLabel = new Label(task.isImportant() ? "⭐" : "☆");

                        starLabel.setOnMouseClicked(e -> {
                            task.setImportant(!task.isImportant());
                            starLabel.setText(task.isImportant() ? "⭐" : "☆");
                        });

                        checkBox.setOnAction(e -> task.setComplete(checkBox.isSelected()));

                        cellLayout.getChildren().addAll(checkBox, starLabel, taskLabel);
                        setGraphic(cellLayout);
                    }
                }
            };
        }
    }

    private void handleDoubleClick(Task task) {
        System.out.println("Double-clicked on: " + task.getTitle());
        // Placeholder for editing or deleting the task
    }

    public static void main(String[] args) {
        launch(args);
    }
}
