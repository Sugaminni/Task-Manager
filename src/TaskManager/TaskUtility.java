package TaskManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class TaskUtility {

    private TaskUtility() {}

    /*Helper class to pass what is needed through parameters in order to make TaskManager cleaner*/

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
        List<Task> snapshotList = new ArrayList<>();
        for (Task task : originalList) {
            snapshotList.add(new Task(
                    task.getTitle(),
                    task.getDescription(),
                    task.getPriority(),
                    task.getWorkload(),
                    task.isComplete(),
                    task.getDueDate()
            ));
        }
        return snapshotList;
    }
}
