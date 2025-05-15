package TaskManager.view;

import TaskManager.model.Task;
import TaskManager.service.TaskManager;
import TaskManager.service.TaskService;
import TaskManager.service.TaskUtility;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.List;


public class TaskManagerGUI extends Application {

    @Override
    public void start(Stage primaryStage) {
        Label label = new Label("Welcome to Task Manager!");
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("Task Manager - GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void launchApp(String[] args) {
        launch(args);
    }
}