package TaskManager.service;

import TaskManager.model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class FolderUtility {

    // Helper method to filter tasks by ids
    public static List<Task> filterTasksById(Set<Integer> taskIds, List<Task> allTasks) {
        if (taskIds == null || allTasks == null) return Collections.emptyList();

        return allTasks.stream()
                .filter(task -> taskIds.contains(task.getTaskID()))
                .collect(Collectors.toList());
    }

    // Helper method to display folder task counts summary
    public static void showFolderTaskCounts(FolderManager folderManager, TaskManager taskManager) {
        for (String folderName : folderManager.listFolders()) { // Loops through each folder
            Set<Integer> ids = folderManager.getTaskIdsInFolder(folderName); // Gets the set of task IDs
            int validCount = 0;
            for (int id : ids) {
                Task task = taskManager.getTaskById(id); // Checks if the task still exists
                if (task != null) {
                    validCount++; // Counts the valid tasks
                }
            }
            System.out.println(folderName + ": " + validCount + " task(s)"); // Shows the count of the amount of tasks in the folder
        }
    }
}
