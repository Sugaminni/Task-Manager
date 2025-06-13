package TaskManager.UI;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

import TaskManager.model.Task;
import TaskManager.service.FolderManager;
import TaskManager.service.TaskManager;
import TaskManager.service.TaskUtility;
import TaskManager.service.FolderUtility;


public class FolderUI {

    private final List<Task> tasks;
    private final FolderManager folderManager;
    private final Scanner sc;
    private final TaskManager taskManager;

    public FolderUI(FolderManager folderManager, Scanner sc, List<Task> tasks, TaskManager taskManager) {
        this.folderManager = folderManager;
        this.sc = sc;
        this.tasks = tasks;
        this.taskManager = taskManager;
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
        if(!ensureFolderSelected())
            return;

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
        System.out.println("Task added to folder successfully.");
    }

    // Method to allow user to view tasks in current folder
    public void viewTasksInCurrentFolderUI() {
        int i = 1;
        Set<Integer> taskIds = folderManager.getTaskIdsInCurrentFolder();
        List<Task> tasksInFolder = FolderUtility.filterTasksById(taskIds, tasks);
        if (taskIds.isEmpty()) { // Checks if task IDs are empty
            System.out.println("No tasks found in this folder.");
            return;
        }
        for (Task task : tasks) { // Loops through the tasks and print out the task list
            if (taskIds.contains(task.getTaskID())) {
                System.out.println(i + ". " + task.briefString());
                i++;
            }
        }
    }

    public void removeTaskFromCurrentFolderUI() {
        Set<Integer> taskIds = folderManager.getTaskIdsInCurrentFolder(); // Gets task IDs from current folder
        List<Task> tasksInFolder = FolderUtility.filterTasksById(taskIds, tasks); // Filters tasks by task IDs in current folder

        if (tasksInFolder.isEmpty()) {
            System.out.println("No valid were found in this folder.");
            return;
        }

        // Display filtered tasks
        int i = 1;
        for (Task task : tasksInFolder) {
            System.out.println(i + ". " + task.briefString());
            i++;
        }

        System.out.println("Which task would you like to remove from the current folder?");
        int userChoice = TaskUtility.readIntSafely(sc);

        if (userChoice < 1 || userChoice > tasksInFolder.size()) { // Checks if the user input is valid
            System.out.println("That task does not exist. Please try again.");
            return;
        }

        Task selectedTask = tasksInFolder.get(userChoice - 1); // Uses task index to check for task ID
        int realTaskID = selectedTask.getTaskID(); // Gets task ID
        folderManager.removeTaskFromCurrentFolder(folderManager.getCurrentFolder(), realTaskID); // Uses task ID to remove task from folder
        System.out.println("Task removed from folder successfully.");
    }

    // Method to ensure a folder is selected before proceeding
    private boolean ensureFolderSelected(){
        if(folderManager.getCurrentFolder() == null){
            System.out.println("No folder is currently selected.");
            return false;
        }
        return true;
    }

    //Method to delete a folder
    public void deleteFolderUI() {
        displayFoldersUI(); // Lists all folders
        System.out.println("Which folder would you like to delete?");
        String folderInput = sc.nextLine().trim();
        if (!folderManager.folderExists(folderInput)) { // Checks if folder exists
            System.out.println("That folder does not exist.");
            return;
        }
        System.out.println("Are you sure you want to delete folder " + folderInput + "? (Y/N) ");
        String confirmation = sc.nextLine().trim(); // Gets user confirmation
        if (confirmation.equalsIgnoreCase("Y") || confirmation.equalsIgnoreCase("YES")) { // Checks if user confirms deletion}
            if (folderInput.equals(folderManager.getCurrentFolder())) { // Checks if the current folder is being deleted
                folderManager.clearCurrentFolder(); // Clears the current folder
            }
            folderManager.deleteFolder(folderInput);
            System.out.println("Folder " + folderInput + " deleted successfully.");
        }
        else {
            System.out.println("Folder deletion cancelled.");
        }
    }

    // Method to rename a folder
    public void renameFolderUI() {
        System.out.println("Which folder would you like to rename?");
        String userInput = sc.nextLine().trim();
        if (!folderManager.folderExists(userInput)) {
            System.out.println("That folder does not exist.");
            return;
        }
        System.out.println("What would you like to rename the folder to?");
        String newFolderName = sc.nextLine().trim();
        if (newFolderName.isBlank()) {
            System.out.println("No name was provided. Renaming cancelled.");
            return;
        }
        if (newFolderName.equals(userInput)) {
            System.out.println("That is the same name. Are you sure you want to rename it anyway? (Y/N)");
            String confirm = sc.nextLine().trim();
            if (!confirm.equalsIgnoreCase("Y")) {
                System.out.println("Rename cancelled.");
                return;
            }
            System.out.println("Folder kept as " + userInput + ".");
            return;
        }
        if (folderManager.folderExists(newFolderName)) {
            System.out.println("A folder with that name already exists.");
            return;
        }
        folderManager.renameFolder(userInput, newFolderName);
        System.out.println("Folder renamed from '" + userInput + "' to '" + newFolderName + "'.");
    }

    // Frontend Method to duplicate folders
    public void duplicateFolderUI() {
        displayFoldersUI(); // Lists all folders
        System.out.println("Which folder would you like to duplicate?");
        String folderInput = sc.nextLine().trim();
        if (!folderManager.folderExists(folderInput)) { // Checks if folder exists
            System.out.println("That folder does not exist.");
            return;
        }
        System.out.println("What would you like to name the duplicate folder?");
        String newFolderName = sc.nextLine().trim();
        if (newFolderName.isBlank() || folderManager.folderExists(newFolderName)) { // Checks if name is blank or folder exists
            System.out.println("Invalid folder name. Duplication cancelled.");
            return;
        }
        folderManager.duplicateFolder(folderInput, newFolderName);
        System.out.println("Folder duplicated successfully.");
    }

    public void handleFolderMenu() {
        int folderOption;

        do {
            System.out.println("\n--- Folder Menu ---");
            System.out.println("1. Create Folder");
            System.out.println("2. List Folders");
            System.out.println("3. Set Current Folder");
            System.out.println("4. Add Task to Current Folder");
            System.out.println("5. Show Folder Task Counts");
            System.out.println("6. View Tasks in a Folder");
            System.out.println("7. Duplicate a Folder");
            System.out.println("8. Delete a Folder");
            System.out.println("9. Rename a Folder");
            System.out.println("10. Remove a Task from a Folder");
            System.out.println("11. Clear All Tasks from Folder");
            System.out.println("12. Back to Main Menu");

            folderOption = TaskUtility.readIntSafely(sc);

            switch (folderOption) {
                case 1 -> createFolderUI();
                case 2 -> displayFoldersUI();
                case 3 -> setCurrentFolderUI();
                case 4 -> addTaskToCurrentFolderUI();
                case 5 -> FolderUtility.showFolderTaskCounts(folderManager, this);
                case 6 -> viewTasksInCurrentFolderUI();
                case 7 -> duplicateFolderUI();
                case 8 -> deleteFolderUI();
                case 9 -> renameFolderUI();
                case 10 -> removeTaskFromCurrentFolderUI();
                case 11 -> System.out.println("Returning to main menu...");
                default -> System.out.println("Invalid option. Try again.");
            }
        } while (folderOption != 11);
    }
}
