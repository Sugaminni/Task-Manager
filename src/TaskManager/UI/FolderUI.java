package TaskManager.UI;

import java.util.Scanner;
import java.util.Set;
import TaskManager.service.FolderManager;

public class FolderUI {

    private FolderManager folderManager;
    private Scanner sc;

    public FolderUI(FolderManager folderManager, Scanner sc) {
        this.folderManager = folderManager;
        this.sc = sc;
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
        if(folderNames.isEmpty()) {
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

    public void addTaskToCurrentFolderUI(){

    }
}
