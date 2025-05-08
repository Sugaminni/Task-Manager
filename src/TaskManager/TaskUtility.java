package TaskManager;
import java.util.*;

public final class TaskUtility {

    private TaskUtility() {}

    /*Helper class to pass what is needed through parameters in order to make TaskManager cleaner
    Make most of the classes in here static as they're standalone operations without needing instance variables*/

    //Helper method for selecting tasks from a list
    public static Task selectTaskFromList(Scanner sc, List<Task> taskList) {
        if(displayTasksOrNotifyEmpty(taskList) == 0) {
            return null;
        }
        System.out.println("Select a task from the list(by number): ");
        int selection = readIntSafely(sc);
        if(selection >= 1 && selection <= taskList.size()) {
            return taskList.get(selection - 1);
        }
        else {
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
        if(displayTasksOrNotifyEmpty(tasks) == 0) return;
        System.out.println("Which task would you like to view? ");
        int selection = readIntSafely(sc);
        try{
            System.out.println(tasks.get(selection-1));
        }
        catch (IndexOutOfBoundsException e) {
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

    //Helper method to refresh copied tasks so original list remains unchanged
    public static void refreshCopiedTasks(List<Task> copiedList, List<Task> originalList) {
        copiedList.clear();
        copiedList.addAll(originalList);
    }

    //Helper method that creates a snapshot (deep copy) of the current task list. Used for storing previous states of tasks for undo feature.
    //Creates new Task objects(not referencing original ones)
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

    // Helper method to parse and validate a comma-separated list of task numbers.
    // Used for both batch delete and batch mark as complete operations.
    // Returns a Set of unique, valid task numbers to avoid duplication.
    public static Set<Integer> parseTaskNumber(String userInput, int totalTasks) {
        // Initialize a set to store unique, valid task numbers.
        Set<Integer> taskNumbersSet = new HashSet<>();
        int number;
        // Split the input string into an array of numbers.
        String[] taskNumbers = userInput.split(",");

        for (String taskNumber : taskNumbers) {
            taskNumber = taskNumber.trim(); // Remove leading and trailing spaces.

            // Check for empty or invalid task numbers.
            if (taskNumber.isEmpty()) {
                System.out.println("Empty or invalid task number, skipping.");
                continue;
            }

            try {
                number = Integer.parseInt(taskNumber); // Attempt to parse the integer.
                if (number <= 0) {
                    System.out.println("Task number must be a positive integer.");
                } else if (number > totalTasks) {
                    System.out.println("Task number " + number + " does not exist.");
                } else {
                    taskNumbersSet.add(number); // Add valid number to the set.
                }
            } catch (NumberFormatException e) {
                System.out.println(taskNumber + " is an invalid task number.");
            }
        }
        return taskNumbersSet; // Return the set of valid task numbers.
    }
}
