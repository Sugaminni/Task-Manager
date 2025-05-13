package TaskManager;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class TaskService {

    // Calls the service method to export tasks to CSV with names
    public String exportTasksToCSV(String fileName, List<Task> taskList) {
        return exportTasksToCSVWithName(fileName, taskList);
    }

    // Service Method to check if tasks have been exported with names for CSV
    private String exportTasksToCSVWithName(String fileName, List<Task> taskList) {
        // Checks if the file name ends with .csv and adds it if not
        if (!fileName.endsWith(".csv")) {
            fileName += ".csv";
        }

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

    public String importTasksFromCSV(String fileName, List<Task> taskList) {
        return importTasksFromCSVWithName(fileName, taskList);
    }

    // Service Method to check if tasks have been imported with names for CSV
    private String importTasksFromCSVWithName(String fileName, List<Task> importedTasks) {
        // Checks if the file name ends with .csv and displays an error if it isn't
        if (!fileName.endsWith(".csv")) {
            return "Error: File type must be .csv";
        }

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

                        // Validates Priority
                        Priority priority; // Defaults the priority to Low if priority doesn't exist
                        try {
                            priority = Priority.valueOf(tasks[3].trim());
                        } catch (Exception e) {
                            System.out.println("Invalid priority value in line: " + line + " - Defaulting to LOW priority.");
                            priority = Priority.LOW;
                        }

                        // Validates Workload
                        Workload workload; // Defaults the workload to Low if workload doesn't exist
                        try {
                            workload = Workload.valueOf(tasks[4].trim());
                        } catch (Exception e) {
                            System.out.println("Invalid workload value in line: " + line + " - Defaulting to LOW workload.");
                            workload = Workload.LOW;
                        }

                        // Validates Due Date
                        LocalDate dueDate; // Defaults to today's date if the due date is invalid or not provided
                        try {
                            dueDate = LocalDate.parse(tasks[5].trim());
                        } catch (DateTimeParseException e) {
                            System.out.println("Invalid due date format in line: " + line + " - Defaulting to today's date.");
                            dueDate = LocalDate.now();
                        }


                        boolean isComplete = Boolean.parseBoolean(tasks[6].trim());

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

        return "Successfully imported " + importedCount + " tasks from " + fileName;
    }
}
