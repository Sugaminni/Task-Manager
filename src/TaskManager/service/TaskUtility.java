package TaskManager.service;
import java.time.LocalDate;
import java.util.*;
import TaskManager.model.Priority;
import TaskManager.model.Task;
import TaskManager.model.Workload;

public final class TaskUtility {

    private TaskUtility() {
    }

    /*Helper class to pass what is needed through parameters to make TaskManager cleaner
    Make most of the classes in here static as they're standalone operations without needing instance variables*/

    //Helper method for selecting tasks from a list
    public static Task selectTaskFromList(Scanner sc, List<Task> taskList) {
        if (displayTasksOrNotifyEmpty(taskList) == 0) {
            return null;
        }
        System.out.println("Select a task from the list(by number): ");
        int selection = readIntSafely(sc);
        if (selection >= 1 && selection <= taskList.size()) {
            return taskList.get(selection - 1);
        } else {
            System.out.println("Invalid selection, please try again.");
            return null;
        }
    }

    //Helper Method for displayTasksOrNotifyEmpty to print(Less duplicates)
    public static int displayTasksOrNotifyEmpty(List<Task> tasks) {
        if (tasks.isEmpty()) { // Returns the number of tasks displayed (0 if none)
            System.out.println("You have no tasks to view.");
            return tasks.size();
        } else { //If there is tasks available then returns number of tasks
            System.out.println("Tasks you have: ");
            int i = 1;
            for (Task task : tasks) {
                System.out.println(i + ". " + task.briefString());
                i++;
            }
        }
        return tasks.size();
    }

    //Helper method to view list unsorted
    public static void viewNormalTask(List<Task> tasks, Scanner sc) {
        if (displayTasksOrNotifyEmpty(tasks) == 0) return;
        System.out.println("Which task would you like to view? ");
        int selection = readIntSafely(sc);
        try {
            System.out.println(tasks.get(selection - 1));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid selection, please try again.");
        }
    }

    //Helper Method to read int safely(avoids prompt skipping)
    public static int readIntSafely(Scanner sc) {
        while (true) {
            try {
                int input = sc.nextInt();
                sc.nextLine(); //Clears newline
                return input;
            } catch (Exception e) {
                sc.nextLine(); //Clears invalid input
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    //Helper method to refresh copied tasks, so original list remains unchanged
    public static void refreshCopiedTasks(List<Task> copiedList, List<Task> originalList) {
        copiedList.clear();
        copiedList.addAll(originalList);
    }

    //Helper method that creates a snapshot (deep copy) of the current task list. Used for storing previous states of tasks for undo feature.
    //Creates new Task objects (not referencing original ones)
    public static List<Task> createTaskSnapshot(List<Task> originalList) {
        List<Task> snapshotList = new ArrayList<>(); //Creates a new list for the snapshot
        for (Task task : originalList) { //Takes information from original list
            snapshotList.add(new Task(
                    task.getTitle(),
                    task.getDescription(),
                    task.getPriority(),
                    task.getWorkload(),
                    task.isComplete(),
                    task.getDueDate(),
                    task.getTaskID()
            ));
        }
        return snapshotList; //Returns the deep copy of the list
    }

    // Helper method to parse and validate a comma-separated list of task numbers
    // Used for both batch delete and batch mark as complete operations
    // Returns a Set of unique, valid task numbers to avoid duplication
    public static Set<Integer> parseTaskNumber(String userInput, int totalTasks) {
        // Initialize a set to store unique, valid task numbers
        Set<Integer> taskNumbersSet = new HashSet<>();
        int number;
        // Split the input string into an array of numbers
        String[] taskNumbers = userInput.split(",");

        for (String taskNumber : taskNumbers) {
            taskNumber = taskNumber.trim(); // Remove leading and trailing spaces

            // Check for empty or invalid task numbers
            if (taskNumber.isEmpty()) {
                System.out.println("Empty or invalid task number, skipping.");
                continue;
            }

            try {
                number = Integer.parseInt(taskNumber); // Attempt to parse the integer
                if (number <= 0) {
                    System.out.println("Task number must be a positive integer.");
                } else if (number > totalTasks) {
                    System.out.println("Task number " + number + " does not exist.");
                } else {
                    taskNumbersSet.add(number); // Adds valid number to the set
                }
            } catch (NumberFormatException e) {
                System.out.println(taskNumber + " is an invalid task number.");
            }
        }
        return taskNumbersSet; // Return the set of valid task numbers
    }

    // Helper method to build a preview of task numbers from a set
    // This method takes a set of task numbers and a preview limit as parameters
    // It returns a formatted string showing up to the specified number of task numbers, followed by "..." if more tasks are present
    public static String buildTaskPreview(Set<Integer> taskNumbersSet, int previewLimit) {
        // Initialize a StringBuilder to construct the preview string
        StringBuilder preview = new StringBuilder();
        int previewCount = 0;

        // Loop through the task numbers in the set to build the preview
        for (int num : taskNumbersSet) {
            preview.append(num).append(", "); // Append the current task number and a comma
            previewCount++;

            // If the preview limit is reached, add "..." to indicate more tasks and exit the loop
            if (previewCount >= previewLimit) {
                preview.append("...");
                break;
            }
        }

        // Remove the trailing comma and space if present to clean up the output
        if (preview.length() > 2) {
            preview.setLength(preview.length() - 2);
        }

        // Return the final preview string
        return preview.toString();
    }

    // Helper method to confirm batch actions (delete/mark as complete)
    // This method prompts the user for confirmation when the number of tasks exceeds a given threshold
    // It dynamically generates a preview of the task numbers and displays a message based on the action name provided
    // Returns true if the user confirms, false if the user cancels
    public static boolean confirmBatchAction(Set<Integer> taskNumbersSet, int threshold, String action, Scanner sc) {
        // Generate a preview of the task numbers to be displayed in the confirmation message
        String preview = buildTaskPreview(taskNumbersSet, threshold);

        // Display the confirmation prompt with the action name and task preview
        System.out.println("You are about to " + action + " " + taskNumbersSet.size() + " tasks: [" + preview + "]. Are you sure? (Y/N)");

        // Read the user's response and trim any extra spaces
        String answer = sc.nextLine().trim();

        // If the user does not confirm with "Y" or "y", print a cancellation message and return false
        if (!answer.equalsIgnoreCase("Y")) {
            System.out.println(action + " cancelled.");
            return false;
        }

        // Return true if the user confirms the action
        return true;
    }

    // Helper method for iterating over tasks and performing batch actions (delete/mark as complete)
    // The 'isDelete' flag determines whether to delete the task or mark it as complete
    // Returns the number of tasks successfully processed
    public static int iterationAction(List<Task> tasks, Set<Integer> taskNumbersSet, boolean isDelete, String actionWord) {
        int totalTasks = taskNumbersSet.size();
        int updateInterval = Math.min(100, Math.max(1, totalTasks / 10));
        int counter = 0;
        int actionCounter = 0;
        int skipCounter = 0;

        Iterator<Task> iterator = tasks.iterator();

        // Iterates over the list of tasks
        while (iterator.hasNext()) {
            Task task = iterator.next();
            counter++;  // Increments the task counter to match task numbering

            // Checks if the current counter matches a task number from the set
            if (taskNumbersSet.contains(counter)) {
                // If marking as complete, check if the task is already complete
                if (!isDelete && task.isComplete()) {
                    System.out.println("Task " + counter + " is already complete, skipping.");
                    skipCounter++;  // Increment skipped tasks counter
                    continue;
                }

                // Perform the delete or mark as complete action based on the 'isDelete' flag
                if (isDelete) {
                    iterator.remove();  // Deletes the task from the list
                    System.out.println("Task " + counter + " deleted.");
                } else {
                    task.setComplete(true);  // Marks the task as complete
                    System.out.println("Task " + counter + " marked as complete.");
                }

                actionCounter++;

                // Progress feedback
                if (actionCounter % updateInterval == 0 || actionCounter == totalTasks) {
                    System.out.printf("Processed %d out of %d tasks (%.1f%%)\n",
                            actionCounter, totalTasks, (actionCounter * 100.0) / totalTasks);
                }
            }
        }

        // Final detailed summary message for user feedback
        System.out.println("Batch operation completed:");
        System.out.println("- Total tasks selected: " + totalTasks);
        System.out.println("- Successfully processed: " + actionCounter);
        System.out.println("- Skipped (already completed): " + skipCounter);

        return actionCounter;
    }


    //Ensures the file type is .csv
    public static String ensureCSV(String fileName) {
        return fileName.endsWith(".csv") ? fileName : fileName + ".csv";
    }

    // Validates Priority
    public static Priority validatePriority(String input) {
        try {
            return Priority.valueOf(input.toUpperCase().trim());
        } catch (Exception e) { // Defaults the priority to Low if priority doesn't exist
            System.out.println("Invalid priority: " + input + ". Defaulting to LOW priority.");
            return Priority.LOW;
        }
    }

    // Validates Workload
    public static Workload validateWorkload(String input) {
        try {
            return Workload.valueOf(input.toUpperCase().trim());
        } catch (Exception e) { // Defaults the workload to Low if workload doesn't exist
            System.out.println("Invalid workload: " + input + ". Defaulting to LOW workload.");
            return Workload.LOW;
        }
    }

    // Validates Due Date
    public static LocalDate validateDate(String input) {
        try {
            LocalDate parsedDate = LocalDate.parse(input);
            // Checks if the date is in the past
            if (parsedDate.isBefore(LocalDate.now())) {
                System.out.println("Warning: The date " + parsedDate + " is in the past. Defaulting to today's date.");
                return LocalDate.now();
            }
            return parsedDate;  // Returns the valid, non-past date
        } catch (Exception e) {
            // Handles invalid date format
            System.out.println("Invalid date format: " + input + ". Defaulting to today's date.");
            return LocalDate.now();
        }
    }

    // Validates completion status
    public static boolean validateCompletionStatus(String input) {
        if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false")) {
            System.out.println("Invalid completion status: " + input + ". Defaulting to false.");
        }
        return Boolean.parseBoolean(input.trim());
    }

    // Utility Method to find tasks with titles similar to the new task title
    public static List<Task> findSimilarTasks(List<Task> taskList, String newTaskTitle) {
        List<Task> similarTasks = new ArrayList<>(); // Creates a list to store similar tasks
        for(Task task : taskList) { // Iterates through through each existing task
            String existingTaskTitle = task.getTitle().toLowerCase(); // Converts the task title to lowercase
            String newTitle = newTaskTitle.toLowerCase(); // Converts the new task title to lowercase
            if(existingTaskTitle.contains(newTitle) || newTitle.contains(existingTaskTitle)) { // Checks if the new task title contains the existing task title or vice versa
                similarTasks.add(task); // Adds the task to the list if it is similar to an existing task title
            }
        }
        return similarTasks; // Returns the list of similar tasks
    }
}
