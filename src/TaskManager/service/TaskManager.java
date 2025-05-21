package TaskManager.service;

import TaskManager.model.Task;
import TaskManager.model.Priority;
import TaskManager.model.Workload;

import TaskManager.comparator.CompletionStatusComparator;
import TaskManager.comparator.DueDateComparator;
import TaskManager.comparator.PriorityComparator;
import TaskManager.comparator.WorkloadComparator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Consumer;


public class TaskManager {

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    private ArrayList<Task> tasks;
    private ArrayList<Task> copiedTasks = new ArrayList<>();
    private ArrayList<Task> importedTasks = new ArrayList<>();
    private Scanner sc = new Scanner(System.in);

    // Creates a stack that stores deep copies of tasks list
    Stack<List<Task>> taskHistory = new Stack<>();
    // Creates a stack used to redo undone actions
    Stack<List<Task>> redoStack = new Stack<>();
    // Creates an instance of TaskService
    private final TaskService taskService = new TaskService();

    // Method to add tasks
    public void addTask() {
        taskHistory.push(TaskUtility.createTaskSnapshot(tasks));
        System.out.println("Enter task title: ");
        String taskName = sc.nextLine();
        List<Task> similarTasks = TaskUtility.findSimilarTasks(tasks, taskName); // Checks for similar tasks and prompts user to confirm if they want to add task
        if (!similarTasks.isEmpty()) {
            int i = 1;
            for (Task t : similarTasks) {
                System.out.println(i + ". " + t.briefString());
                i++;
            }
            System.out.println("There is a similar task with the title: " + taskName + ". Would you like to add this task? (y/n)");
            String userInput = sc.nextLine();
            if (!userInput.equalsIgnoreCase("y")) {
                System.out.println("Task not added.");
                return;
            }
        }


        System.out.println("Enter task description: ");
        String taskDescription = sc.nextLine();

        // Validate Priority / catches if input is invalid
        System.out.println("Enter task priority (HIGH, MEDIUM, LOW): ");
        String taskPriority = sc.nextLine();
        Priority priority = TaskUtility.validatePriority(taskPriority);

        // Validates Workload / catches if input is invalid
        System.out.println("Enter task workload (HIGH, MEDIUM, LOW): ");
        String taskWorkload = sc.nextLine();
        Workload workload = TaskUtility.validateWorkload(taskWorkload);

        // Validate and parse date / catches if input is invalid
        System.out.println("Enter task due date (YYYY-MM-DD): ");
        String dueDate = sc.nextLine();
        LocalDate date = TaskUtility.validateDate(dueDate);

        tasks.add(new Task(taskName, taskDescription, priority, workload, false, date));
        System.out.println("Task added successfully!");
    }

    //Method for GUI to delete a task without confirmation
    public boolean deleteSingleTask(Task task) {
        for (Task t : tasks) {
            if (t.getTaskID() == task.getTaskID()) {
                System.out.println("Deleting task: " + t.getTitle() + " (ID: " + t.getTaskID() + ")");
                tasks.remove(t);
                return true;
            }
        }
        System.out.println("Task not found: " + task.getTitle() + " (ID: " + task.getTaskID() + ")");
        return false;
    }


    // Method to delete tasks
    public void deleteTask() {
        taskHistory.push(TaskUtility.createTaskSnapshot(tasks));
        System.out.println("Would you like to delete a single task or multiple tasks?\n1. Single Task\n2. Multiple Tasks");
        int userChoice = TaskUtility.readIntSafely(sc);
        int counter;
        Iterator<Task> iterator;
        switch (userChoice) {

            case 1: // Selects a single task to delete
                if (displayTasksOrNotifyEmpty(tasks)) { // Checks if there are tasks available first
                    return;
                }
                System.out.println("Which task would you like to delete (By Number): ");
                int selectedTaskIndex = TaskUtility.readIntSafely(sc); // Sends user input to helper method for task deletion
                counter = 0;
                boolean deleted = false;
                iterator = tasks.iterator();

                // Loops iterations to delete the correct task/prints not found if task doesn't exist
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    counter++;
                    if (counter == selectedTaskIndex) {
                        deleted = true;
                        iterator.remove();
                        System.out.println("Successfully deleted task number: " + selectedTaskIndex);
                    }
                }
                if (!deleted) {
                    System.out.println("Task " + selectedTaskIndex + " not found. No tasks were deleted.");
                }
                break;

            case 2: // Allows user to select multiple tasks to delete
                final int deletionThreshold = 5;
                String actionWord = "delete";
                String actionMessage = "deleted";
                if (displayTasksOrNotifyEmpty(tasks)) {
                    return;
                }
                System.out.println("Please enter the task numbers for the tasks you would like to delete seperated by commas: ");
                String userInput = sc.nextLine();

                Set<Integer> taskNumbersSet = TaskUtility.parseTaskNumber(userInput, tasks.size()); // Calls parseTaskNumber to validate user input

                // Deletes tasks only if valid task numbers are input by user
                if (taskNumbersSet.isEmpty()) {
                    System.out.println("No valid task numbers provided, deletion cancelled.");
                    break;
                }

                if (taskNumbersSet.size() > deletionThreshold) {
                    // gives a preview of the tasks
                    boolean confirmation = TaskUtility.confirmBatchAction(taskNumbersSet, deletionThreshold, actionWord, sc);
                    if (!confirmation) {
                        System.out.println("Deletion cancelled.");
                        break;
                    }
                }

                // Iterates over tasks for deletion
                int markedCount = TaskUtility.iterationAction(tasks, taskNumbersSet, true, actionMessage);
                break;
        }
    }


    // Method to edit tasks
    public void handleTaskEdit() {
        Task selected = TaskUtility.selectTaskFromList(sc, tasks);
        if (selected != null) {
            selectedTaskToEdit(selected);
        }
    }

    // Method to view Tasks
    public void displayTask() {
        if (TaskUtility.displayTasksOrNotifyEmpty(tasks) == 0) return; // Checks for tasks before printing how to view
        System.out.println("""
                How would you like to view your tasks?
                1. View normally
                2. View sorted (By Due Date, Completion, Priority, Workload)
                """);
        int viewChoice = TaskUtility.readIntSafely(sc);
        switch (viewChoice) {
            case 1:
                TaskUtility.viewNormalTask(tasks, sc);
                break;

            case 2: // Method to sort tasks by different categories
                viewSortedTasks();
                break;

            default:
                System.out.println("Invalid view choice. Please select a valid choice");
                break;
        }
    }

    // Method for displayTasksOrNotifyEmpty to print(Less duplicates)
    public boolean displayTasksOrNotifyEmpty(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) { // If Task arraylist is empty, returns true
            System.out.println("You have no tasks to view.");
            return true;
        } else { // If there is tasks available then .isEmpty returns false
            System.out.println("Tasks you have: ");
            int i = 1;
            for (Task task : tasks) {
                System.out.println(i + ". " + task.briefString());
                i++;
            }
        }
        return false;
    }

    public void handleSearch() { // Prompts user for keyword then passes onto searchTask
        System.out.println("Enter a keyword to search for: ");
        String query = sc.nextLine();
        searchTasksByKeyword(query);
    }

    // Method for Searching(Searches for Tasks containing user specified word)
    public void searchTasksByKeyword(String query) {
        boolean found = false;
        for (Task task : tasks) { // Checks if keyword is in title or description
            if (task.getTitle().toLowerCase().contains(query.toLowerCase())
                    || (task.getDescription().toLowerCase().contains(query.toLowerCase()))) {
                System.out.println(task.briefString());
                found = true; // Marks true if keyword is found otherwise, false
            }
        }
        if (!found) {
            System.out.println("There is no such task or description found.");
        }
    }

    public void handleFilter() { // Prompts user for what to filter by then passes to filterTask method
        System.out.println("""
                 What would you like to filter tasks by:\s
                 1. Completed tasks
                 2. Priority(High/Med/Low)\s
                 3. Due date
                \s""");
        int filterChoice = TaskUtility.readIntSafely(sc);
        applyTaskFilter(filterChoice);
    }

    public void applyTaskFilter(int filter) {
        boolean found = false;
        switch (filter) {
            case 1: // Filters tasks by completed tasks
                for (Task task : tasks) {
                    if (task.isComplete()) {
                        System.out.println(task.briefString());
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No completed tasks found.");
                }
                break;

            case 2: // Filters tasks by priority user inputs
                Priority priority = null;
                System.out.println("Which priority would you like to filter by(HIGH/MEDIUM/LOW): ");
                String priorityInput = sc.nextLine();
                try {
                    priority = Priority.valueOf(priorityInput.toUpperCase().trim());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid priority. Please use HIGH, MEDIUM, or LOW");
                    return;
                }
                for (Task task : tasks) {
                    if (task.getPriority().equals(priority)) { // Compare priorities
                        System.out.println(task.briefString());
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No priority found with priority: " + priorityInput);
                }
                break;

            case 3: // Filters tasks by Workload
                Workload workload = null;
                System.out.println("What workload would you like to filter by(HIGH/MEDIUM/LOW): ");
                String workloadInput = sc.nextLine();

                try {
                    workload = Workload.valueOf(workloadInput.toUpperCase().trim());
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid workload. Please use HIGH, MEDIUM, or LOW");
                    return;
                }
                for (Task task : tasks) {
                    if (task.getWorkload().equals(workload)) { // Compare workloads
                        System.out.println(task.briefString());
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No workload found with workload: " + workloadInput);
                }
                break;

            case 4: // Filters tasks by Due date
                System.out.println("Enter a due date to filter by (MM/DD/YYYY): ");
                String userDueDate = sc.nextLine();

                LocalDate date;
                try { // Exception handler for date parse
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    date = LocalDate.parse(userDueDate, formatter);
                } catch (DateTimeParseException e) { // Makes sure user cannot put a date in the past
                    System.out.println("Invalid due date format");
                    break;
                }

                for (Task task : tasks) { // Compares due dates with before dates & user entered date then prints out tasks
                    if (task.getDueDate().isBefore(date) || task.getDueDate().isEqual(date)) {
                        System.out.println(task.briefString());
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No tasks due on or before: " + userDueDate);
                }
                break;
        }
    }

    public void viewSortedTasks() {
        TaskUtility.refreshCopiedTasks(copiedTasks, tasks); // Adds all tasks from origin task list to copied list/avoids permanently changing up sort
        System.out.println("""
                How would you like to view your tasks?
                 1. By Due Date\s
                 2. By Completion Status\s
                 3. By Priority\s
                 4. By Workload\s""");
        int viewType = TaskUtility.readIntSafely(sc);
        switch (viewType) {
            // Uses a list.sort to take in copiedTasks(from origin) to sort by user input
            case 1: // Sorts by due date
                copiedTasks.sort(new DueDateComparator());
                break;

            case 2: // Sorts by completion
                copiedTasks.sort(new CompletionStatusComparator());
                break;

            case 3: // Sorts by priority
                copiedTasks.sort(new PriorityComparator());
                break;

            case 4: // Sorts by workload
                copiedTasks.sort(new WorkloadComparator());
                break;

            default:
                System.out.println("Invalid option");
                return;
        }
        if (displayTasksOrNotifyEmpty(copiedTasks)) return;
        boolean validTaskOption = false;
        while (!validTaskOption) {
            System.out.println("Tasks sorted");
            try { // Asks user which task in sorted task to see in full
                System.out.println("Which task would you like to see in full?");
                int selectedTaskIndex = TaskUtility.readIntSafely(sc);
                System.out.println(copiedTasks.get(selectedTaskIndex - 1));
                validTaskOption = true;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid option");
                displayTasksOrNotifyEmpty(copiedTasks);
            }
        }
    }

    // Method to edit parts of selected task
    public void selectedTaskToEdit(Task selectedTask) {
        taskHistory.push(TaskUtility.createTaskSnapshot(tasks));
        while (true) {
            System.out.println("1. Title\n2. Description\n3. Priority\n4. Work Load\n5. Due Date\n6. Completion Status\n7. Choose another task\n8. Return to main menu\n9. Undo last change\n10. Redo last change");
            int userOptionForEdit = TaskUtility.readIntSafely(sc); // Clears input buffer

            switch (userOptionForEdit) { // Switch case to edit user selected task part
                case 1: // Changes Title
                    System.out.println("What would you like to change the title to: ");
                    selectedTask.setTitle(sc.nextLine());
                    System.out.println("Title updated!");
                    break;

                case 2: // Changes Description
                    System.out.println("What would you like to change the description to: ");
                    selectedTask.setDescription(sc.nextLine());
                    System.out.println("Description updated!");
                    break;

                case 3: // Changes Priority
                    System.out.println("What would you like to change the priority to: ");
                    String taskPriority = sc.nextLine();
                    try { // Sets Priority for selected task
                        selectedTask.setPriority(Priority.valueOf(taskPriority.toUpperCase().trim()));
                        System.out.println("Priority updated!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Please enter a valid priority");
                    }
                    break;

                case 4: // Changes Workload
                    System.out.println("What would you like to change the workload to: ");
                    String taskWorkload = sc.nextLine();
                    try { // Sets Workload for selected task
                        selectedTask.setWorkload(Workload.valueOf(taskWorkload.toUpperCase().trim()));
                        System.out.println("Workload updated!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Please enter a valid workload");
                    }
                    break;

                case 5: // Changes Due Date
                    System.out.println("What would you like to change the date to(MM/DD/YYYY): ");
                    String replacementDate = sc.nextLine();
                    try { // Exception handler for date parse
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate newDate = LocalDate.parse(replacementDate, formatter);

                        // Makes sure user can't update it to past dates
                        if (newDate.isBefore(LocalDate.now())) {
                            System.out.println("Date cannot be in the past!");
                        } else {
                            selectedTask.setDueDate(newDate);
                            System.out.println("Date updated!");
                        }

                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid due date format");
                    }
                    break;

                case 6: // Changes completion
                    System.out.println("Change Completion Status?");
                    // Flipper to flip task from pending to done / vice versa
                    selectedTask.setComplete(!selectedTask.isComplete());
                    // Confirms with user if task is complete/incomplete
                    System.out.println("Task marked as " + (selectedTask.isComplete() ? "\u001B[32mComplete ✓\u001B[0m" : "\u001B[33mIncomplete ⏳\u001B[0m"));
                    break;

                case 7: // Back to choosing a task to edit
                    if (displayTasksOrNotifyEmpty(tasks)) break;
                    continue;

                case 8: // Back to Main Menu
                    return;

                case 9: // Calls for the edit to be undone
                    undoLastAction();
                    System.out.println("Undo successful.");
                    break;

                case 10: // Calls for a redo of undone task
                    redoLastAction();
                    System.out.println("redo successful.");
                    break;

                default:
                    System.out.println("Invalid option");
            }
        }
    }

    //  Method to undo any actions done by the user (Add,Edit,Delete)
    public void undoLastAction() {
        if (taskHistory.isEmpty()) { // Checks to see if there is any tasks, if none then there is nothing to undo
            System.out.println("There is nothing to undo.");
        } else {
            redoStack.push(TaskUtility.createTaskSnapshot(tasks)); // Pushes a deep copy of tasks to redoStack in case undo action needs to be restored
            List<Task> poppedSnapshot = taskHistory.pop(); // Undoes the change and reverts back to last stored snapshot
            tasks.clear(); // Clears the task list
            tasks.addAll(poppedSnapshot); // Undoes task to previous state
            System.out.println("Undo successful.");
        }
    }

    // Method to restore any undone changes by the user
    public void redoLastAction() {
        if (redoStack.isEmpty()) { // Checks to see if there is any redoable actions, if none then there is nothing to redo
            System.out.println("There is nothing to redo.");
        } else {
            taskHistory.push(TaskUtility.createTaskSnapshot(tasks)); // Saves current state before redo
            List<Task> poppedSnapshot = redoStack.pop(); // Redoes any change that was undone
            tasks.clear(); // Clears the list
            tasks.addAll(poppedSnapshot); // Restores tasks to redone state
            System.out.println("Redo successful.");
        }
    }

    // Method to mark Tasks as complete
    public void markTasksAsComplete() {
        String actionMessage = "change the completion status of ";
        String actionWords = "marked as complete";
        Consumer<Task> action = task -> task.setComplete(true);
        final int markingThreshold = 5;
        if (displayTasksOrNotifyEmpty(tasks)) {
            return;
        }
        System.out.println("Please enter the task numbers for the tasks you would like to mark as complete seperated by commas: ");
        String userInput = sc.nextLine();
        Set<Integer> taskNumbersSet = TaskUtility.parseTaskNumber(userInput, tasks.size()); // Calls parseTaskNumber to validate user input

        // Marks tasks only if valid task numbers are input by user
        if (taskNumbersSet.isEmpty()) {
            System.out.println("No valid task numbers provided, marking cancelled.");
        }

        if (taskNumbersSet.size() > markingThreshold) {
            // Gives a preview of the tasks
            boolean confirmation = TaskUtility.confirmBatchAction(taskNumbersSet, markingThreshold, actionMessage, sc);
            if (!confirmation) {
                System.out.println("Change of completion status cancelled.");
                return;
            }
        }

        // Iterates over tasks for marking
        int markedCount = TaskUtility.iterationAction(tasks, taskNumbersSet, false, actionWords);
    }

    //Method to prompt the user for a file name
    public String promptForFileName() {
        System.out.println("What would you like to name the file? ");
        String userFileName = sc.nextLine().trim();
        // Checks if the input is empty
        if (userFileName.isBlank()) {
            System.out.println("No name was provided, using default name: tasks.csv");
            return "tasks.csv";
        }
        // Returns file name with .csv extension
        return TaskUtility.ensureCSV(userFileName);
    }

    // Method to export tasks to a CSV file
    public void exportTasksToCSV(String fileName) {
        // Checks if the task list is empty
        if (tasks.isEmpty()) {
            System.out.println("No tasks to export.");
            return;
        }
        // Calls the method exportTasksToCSVWithName to handle export
        String file = taskService.exportTasksToCSV(fileName, tasks);
        System.out.println(file);
    }

    public void exportTasks() {
        // Calls the method to prompt for file name and export tasks to CSV
        String fileName = promptForFileName();
        exportTasksToCSV(fileName);
    }

    // Method to import tasks from an outside CSV file
    public void importTasks() {
        String fileName = promptForFileName();
        String createdFile = taskService.importTasksFromCSV(fileName, importedTasks, tasks, sc); // uses the temp task list
        System.out.println(createdFile);
        displayTasksOrNotifyEmpty(tasks);
    }
}
