package TaskManager.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import TaskManager.model.Task;
import TaskManager.service.FolderManager;
import TaskManager.service.TaskUtility;

public class FolderUI {

    private List<Task> tasks;
    private FolderManager folderManager;
    private Scanner sc;

    public FolderUI(FolderManager folderManager, Scanner sc, List<Task> tasks) {
        this.folderManager = folderManager;
        this.sc = sc;
        this.tasks = tasks;
    }

    // Method that prompts the user to create a folder (Naming)
    public void createFolderUI() {
        System.out.println("What would you like to name the folder?: ");
        String folderName = sc.nextLine();
        // Checks if the input is empty
        if (folderName.isBlank()) {
            System.out.println("No name was provided, using default name: Tasks");
            folderName = "Tasks"; // Default folder name
        }
        // Checks if folder already exists
        if (folderManager.folderExists(folderName)) {
            System.out.println("A folder with that name already exists.");
            return;
        }
        // Creates the folder
        folderManager.createFolder(folderName);
        System.out.println("Folder " + folderName + " created successfully.");
    }

    public void displayFoldersUI() {
        Set<String> folderNames = folderManager.listFolders(); // Gets all folders
        String current = folderManager.getCurrentFolder(); // Gets current folder name
        if (folderNames.isEmpty()) {
            System.out.println("No folders found.");
            return;
        }

        int i = 1; // Counter
        for (String folderName : folderNames) { // Lists all folders
            if (folderName.equals(current)) { // Checks if current folder is the same as the folder name
                System.out.println(i + ". " + folderName + " (Current)"); // Prints out current folder
            } else { // Prints out all other folders
                System.out.println(i + ". " + folderName);
            }
            i++; // Increments counter
        }
    }

    // Method to set a different folder as current folder
    public void setCurrentFolderUI() {
        System.out.println("Which folder would you like to switch to?");
        String folderInput = sc.nextLine();
        folderManager.setCurrentFolder(folderInput);
    }

    // Method to add task to the current folder
    public void addTaskToCurrentFolderUI() {
        int i = 1;
        if (folderManager.getCurrentFolder() == null) { // If folder does not exist, tells user
            System.out.println("No folder is currently selected.");
            return;
        }
        for (Task task : tasks) { // Loops through the tasks and print out the task list
            System.out.println(i + ". " + task.briefString());
            i++;
        }
        System.out.println("Which task would you like to add to the current folder?");
        int userChoice = TaskUtility.readIntSafely(sc);
        if (userChoice < 1 || userChoice > tasks.size()) {
            System.out.println("That task does not exist. Please try again.");
            return;
        }
        Task selectedTask = tasks.get(userChoice - 1); // Uses task index to check for task ID
        int realTaskID = selectedTask.getTaskID(); // Gets task ID
        folderManager.addTaskToFolder(folderManager.getCurrentFolder(), realTaskID); // Uses task ID to add task to folder
    }

    // Method to allow user to view tasks in current folder
    public void viewTasksInCurrentFolderUI() {
        int i = 1;
        if (folderManager.getCurrentFolder() == null) { // If folder does not exist, tells user
            System.out.println("No folder is currently selected.");
            return;
        }
        Set<Integer> taskIds = folderManager.getTaskIdsInCurrentFolder(); // Gets all task IDs in current folder
        if (taskIds.isEmpty()) { // Checks if task IDs are empty
            System.out.println("No tasks found in the current folder.");
        }
        for (Task task : tasks) { // Loops through the tasks and print out the task list
            if (taskIds.contains(task.getTaskID())) {
                System.out.println(i + ". " + task.briefString());
                i++;
            }
        }
        if (i == 1) { // Prints if there are no task matches
            System.out.println("No valid tasks were found in this folder.");
        }
    }

    public void removeTaskFromCurrentFolderUI() {
        List<Task> tasksInFolder = new ArrayList<>(); // List to store tasks in folder
        int i = 1;
        if (folderManager.getCurrentFolder() == null) { // If folder does not exist, tells user
            System.out.println("No folder is currently selected.");
            return;
        }
        Set<Integer> taskIds = folderManager.getTaskIdsInCurrentFolder(); // Gets all task IDs in current folder
        if (taskIds.isEmpty()) { // Checks if task IDs are empty
            System.out.println("No tasks found in the current folder.");
        }
        for (Task task : tasks) {
            if (taskIds.contains(task.getTaskID())) {
                tasksInFolder.add(task); // Adds task to list if it is in the folder
                System.out.println(i + ". " + task.briefString());
                i++;
            }
        }
        if (i == 1) { // Prints if there are no task matches
            System.out.println("No valid tasks were found in this folder.");
        }
        System.out.println("Which task would you like to remove from the current folder?");
        int userChoice = TaskUtility.readIntSafely(sc);
        if (userChoice < 1 || userChoice > tasksInFolder.size()) {
            System.out.println("That task does not exist. Please try again.");
            return;
        }
        Task selectedTask = tasksInFolder.get(userChoice - 1); // Uses task index to check for task ID
        int realTaskID = selectedTask.getTaskID(); // Gets task ID
        folderManager.removeTaskFromCurrentFolder(folderManager.getCurrentFolder(), realTaskID); // Uses task ID to remove task from folder
        System.out.println("Task removed from folder successfully.");
    }
}
