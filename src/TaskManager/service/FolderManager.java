package TaskManager.service;

import TaskManager.model.Task;

import java.util.*;

public class FolderManager {

    // Folder class to allow users to create folders and store tasks within

    private Map<String, Set<Integer>> folders;
    private String currentFolder = "All Tasks";

    // Constructor for FolderManager
    public FolderManager() {
        this.folders = new HashMap<>();
    }

    // Method to create a folder
    public void createFolder(String folderName) {
        folders.putIfAbsent(folderName, new HashSet<>());
    }

    // Methods to check if the folder exists
    public boolean folderExists(String folderName) {
        return folders.containsKey(folderName);
    }

    // Method to get the tasks within the folder
    public Set<Integer> getTaskIdsInCurrentFolder() {
        return folders.getOrDefault(currentFolder, new HashSet<>());
    }

    // Method to add tasks to a folder
    public void addTaskToFolder(String folderName, int taskID) {
        if (!folders.containsKey(folderName)) {
            System.out.println("Folder does not exist: " + folderName);
            return;
        }
        folders.get(folderName).add(taskID);
    }

    // Displays a list of folders
    public Set<String> listFolders () {
        return folders.keySet();
    }

    // Method to delete a folder
    public void deleteFolder (String folderName) {
        folders.remove(folderName);
        if(folderName.equals(currentFolder)) {
            currentFolder = "All Tasks"; // Sets current folder to all tasks if deleted folder was current folder
        }
    }

    // Method to set the current folder, changes to all tasks if a folder does not exist yet
    public void setCurrentFolder (String folderName) {
        if( !folders.containsKey(folderName) ) { // Checks if folder exists
            System.out.println("Folder does not exist: " + folderName);
            return;
        }
        currentFolder = folderName;
    }

    // Getter for current folder
    public String getCurrentFolder () {
        return currentFolder;
    }

    // Method to get tasks within the current folder
    public Set<Integer> getTaskIdsInFolder(String folderName) {
        return folders.getOrDefault(folderName, new HashSet<>());
    }

    // Allows user to remove tasks from all folders
    public void removeTaskFromAllFolders(int taskID) {
        for (Set<Integer> taskIDs : folders.values()) {
            taskIDs.remove(taskID);
        }
    }

}
