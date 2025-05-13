package TaskManager;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class TaskService {

    // Calls the service method to export tasks to CSV with names
    public String exportTasksToCSV(String fileName, List<Task> taskList) {
        return exportTasksToCSVWithName(fileName, taskList);
    }

    // Service Method to check if tasks have been exported with names for CSV
    private String exportTasksToCSVWithName(String fileName, List<Task> taskList) {
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

    public String importTasksFromCSV(String fileName, List<Task> taskList) {
        return importTasksFromCSVWithName(fileName, taskList);
    }

    // Service Method to check if tasks have been imported with names for CSV
    private String importTasksFromCSVWithName(String fileName, List<Task> importedTasks) {
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

        return "Successfully imported " + importedCount + " tasks from " + fileName;
    }
}
