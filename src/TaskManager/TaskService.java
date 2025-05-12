package TaskManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class TaskService {

    // Calls the service method to export tasks to CSV with names
    public String exportTasksToCSV(String fileName, List<Task> taskList){
        return exportTasksToCSVWithName(fileName, taskList);
    }
    // Service Method to check if tasks have been exported with names for CSV
    private String exportTasksToCSVWithName(String fileName, List<Task> taskList){
        // Checks if the file name ends with .csv and adds it if not
        if(!fileName.endsWith(".csv")) {
            fileName += ".csv";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))){
            writer.write("Task ID, Task, Description, Priority, Work Load, Due Date, Status\n");
            writer.newLine();
            for(Task task : taskList) {
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
}
