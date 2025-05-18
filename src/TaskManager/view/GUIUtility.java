package TaskManager.view;

import TaskManager.model.Priority;
import TaskManager.model.Task;
import TaskManager.model.Workload;
import javafx.scene.control.*;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;

import java.time.LocalDate;

public class GUIUtility {


    // Method to display an alert dialog with a title and message
    public static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Method to add a task to the task list
    public static void addTask(ListView<Task> taskListView) {
        // Step 1: Create the dialog
        Dialog<Task> addTask = new Dialog<>();
        addTask.setTitle("Add Task");

        // Step 2: Create input fields
        TextField titleField = new TextField();
        titleField.setPromptText("Task Title");
        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Task Description");
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        priorityBox.setPromptText("Select Priority");
        ComboBox<String> workloadBox = new ComboBox<>();
        workloadBox.getItems().addAll("High", "Medium", "Low");
        workloadBox.setPromptText("Select Workload");
        DatePicker dueDatePicker = new DatePicker();
        dueDatePicker.setPromptText("Select Due Date");

        // Step 3: Set up the layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descriptionField, 1, 1);
        grid.add(new Label("Priority:"), 0, 2);
        grid.add(priorityBox, 1, 2);
        grid.add(new Label("Workload:"), 0, 3);
        grid.add(workloadBox, 1, 3);
        grid.add(new Label("Due Date:"), 0, 4);
        grid.add(dueDatePicker, 1, 4);

        // Step 4: Add layout to the dialog
        addTask.getDialogPane().setContent(grid);
        addTask.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Step 5: Handle OK button click
        addTask.setResultConverter(button -> {
            if (button == ButtonType.OK) {
                String title = titleField.getText().trim();
                String description = descriptionField.getText();
                String priority = priorityBox.getValue();
                String workload = workloadBox.getValue();
                LocalDate dueDate = dueDatePicker.getValue();

                // Validate fields
                if (title.isEmpty()) {
                    showAlert("Error: ", "Title is required.");
                    return null;
                }
                if (priority == null) {
                    showAlert("Error: ", "Priority is required.");
                    return null;
                }
                if (workload == null) {
                    showAlert("Error: ", "Workload is required.");
                    return null;
                }
                if (dueDate == null) {
                    showAlert("Error: ", "Due date is required.");
                    return null;
                }

                // Return the task object with correct data types
                return new Task(title, description, Priority.valueOf(priority.toUpperCase()),
                        Workload.valueOf(workload.toUpperCase()), false, dueDate);
            }
            return null;
        });

        // Step 6: Display the dialog and handle the result
        addTask.showAndWait().ifPresent(task -> {
            System.out.println("Task added: " + task);
            taskListView.getItems().add(task);
            // Show success alert
            showAlert("Success", "Task added successfully!");
        });
    }

    public static void deleteTask() {

    }

    public static void editTask() {
    }

    public static void filterTasks() {
        // Filter task placeholder
        Dialog<Void> filterDialog = new Dialog<>();
        filterDialog.setTitle("Filter Tasks");
        filterDialog.setHeaderText("Filter Tasks by:");
    }

}
