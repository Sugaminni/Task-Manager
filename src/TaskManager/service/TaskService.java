package TaskManager.service;

import TaskManager.model.Task;
import TaskManager.model.Priority;
import TaskManager.model.Workload;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TaskService {

    // Calls the service method to export tasks to CSV with names
    public String exportTasksToCSV(String fileName, List<Task> taskList) {
        return exportTasksToCSVWithName(fileName, taskList);
    }

    // Service Method to check if tasks have been exported with names for CSV
    private String exportTasksToCSVWithName(String fileName, List<Task> taskList) {
        // Uses a default name if the file name is empty
        if (fileName.isEmpty()) {
            fileName = "Tasks";
        }

        // Generates the timestamp for the file name
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");
        String timestamp = now.format(formatter);

        // Gets task count for file name
        int taskCount = taskList.size();
        fileName = fileName + "_" + timestamp + "_" + taskCount + ".csv";

        // Checks if the file name ends with .csv and adds it if not
        fileName = TaskUtility.ensureCSV(fileName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Task ID, Task, Description, Priority, Work Load, Due Date, Status\n");
            writer.newLine(); // Adds a new line after the header to separate the data from the header
            for (Task task : taskList) {
                String line = String.format("%d,%s,%s,%s,%s,%s,%b",
                        task.getTaskID(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getPriority(),
                        task.getWorkload(),
                        task.getDueDate(),
                        task.isComplete());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            return "Error exporting tasks: " + e.getMessage();
        }
        return "Tasks successfully exported to " + fileName;
    }

    public String importTasksFromCSV(String fileName, List<Task> importedTasks, List<Task> mainTasks, Scanner sc) {
        return importTasksFromCSVWithName(fileName, importedTasks, mainTasks, sc);
    }

    // Service Method to check if tasks have been imported with names for CSV
    private String importTasksFromCSVWithName(String fileName, List<Task> importedTasks, List<Task> mainTasks, Scanner sc) {
        // Checks if the file name ends with .csv and displays an error if it isn't
        fileName = TaskUtility.ensureCSV(fileName);

        int importedCount = 0; // Keeps track of how many tasks were imported
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) { // Reads the first line to skip the header
                String[] tasks = line.split(","); // Splits the line into an array of strings
                if (tasks.length == 7) {
                    try {
                        int taskID = Integer.parseInt(tasks[0].trim());
                        String title = tasks[1].trim();
                        String description = tasks[2].trim();
                        Priority priority = TaskUtility.validatePriority(tasks[3].trim());
                        Workload workload = TaskUtility.validateWorkload(tasks[4].trim());
                        LocalDate dueDate = TaskUtility.validateDate(tasks[5].trim());
                        boolean isComplete = TaskUtility.validateCompletionStatus(tasks[6].trim());

                        // Create the task object and add it to the list
                        Task task = new Task(title, description, priority, workload, isComplete, dueDate, taskID);
                        task.setTaskID(taskID); // Keeps the original ID from the file
                        importedTasks.add(task);
                        importedCount++;

                    } catch (Exception e) {
                        System.out.println("Error importing tasks: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid data format in line: " + line);
                }
            }
        } catch (IOException e) {
            return "Error importing tasks: " + e.getMessage();
        }

        // Throws error message if no valid tasks are found in the file
        if (importedCount == 0) {
            return "No valid tasks found in the file. Import cancelled.";
        }

        // Shows a preview of the tasks to be imported
        System.out.println("Preview of tasks to be imported:");
        int i = 1;
        for (Task task : importedTasks) {
            System.out.println(i + ". " + task.briefString());
            i++;
        }

        // Asks the user to confirm the task merge
        System.out.println("Are you sure you want to merge " + importedCount + " tasks into your main list of tasks? (Y/N)");
        String confirmation = sc.nextLine().trim();
        if (!confirmation.equalsIgnoreCase("Y") && !confirmation.equalsIgnoreCase("YES")) {
            return "Merge cancelled. No tasks were added.";

        }

        // Merges the tasks when confirmed by the user
        int numOfTasks = importedTasks.size();
        mainTasks.addAll(importedTasks);
        importedTasks.clear();
        return "Successfully imported " + importedCount + " tasks from " + fileName + " and merged them with existing tasks.";
    }

    // Method to export tasks to Markdown
    public static void exportTasksToMarkdown(List<Task> tasks, String fileName) {
        if (fileName == null || fileName.isBlank()) { // Uses a default name if the file name is empty
            fileName = "Tasks";
        }
        // Checks if the file name ends with .md and adds it if not
        if (!fileName.toLowerCase().endsWith(".md")) {
            fileName += ".md";
        }

        // Directs the download path to the user's Downloads folder
        Path downloadPath = Paths.get(System.getProperty("user.home"), "Downloads", fileName);

        // Checks if the file name ends with .md and adds it if not
        try (BufferedWriter writer = Files.newBufferedWriter(downloadPath)) {
            for (Task task : tasks) { // Writes each task to the file in Markdown format
                writer.write(formatTaskAsMarkdown(task)); // Formats the task as Markdown
                writer.newLine();
                writer.newLine();
            }
            System.out.println("Tasks exported to: " + downloadPath.toAbsolutePath());
        } catch (IOException e) { // Prints an error message if there is an issue writing to the file
            System.out.println("Error exporting tasks to Markdown: " + e.getMessage());
        }
    }

    // Method to format a task as Markdown
    private static String formatTaskAsMarkdown(Task task) {
        return String.format(
                "## Task: %s\n\n" +
                        "- **ID**: %d\n" +
                        "- **Description**: %s\n" +
                        "- **Priority**: %s\n" +
                        "- **Workload**: %s\n" +
                        "- **Due Date**: %s\n" +
                        "- **Completed**: %s",
                task.getTitle(),
                task.getTaskID(),
                task.getDescription(),
                task.getPriority(),
                task.getWorkload(),
                task.getDueDate(),
                task.isComplete() ? "[x]" : "[ ]"
        );
    }
}
