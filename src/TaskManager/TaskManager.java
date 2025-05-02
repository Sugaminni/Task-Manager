package TaskManager;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class TaskManager {
    ArrayList<Task> tasks = new ArrayList<>();
    ArrayList<Task> copiedTasks = new ArrayList<>();
    Scanner sc = new Scanner(System.in);
    int taskNum;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    //Method to add tasks
    public void addTask() {
        System.out.println("Enter task title: ");
        String taskName = sc.nextLine();
        System.out.println("Enter task description : ");
        String taskDescription = sc.nextLine();
        System.out.println("Enter task priority (High/Med/Low): ");
        String taskPriority = sc.nextLine();
        System.out.println("Enter task due date (MM/DD/YYYY): ");
        String dueDate = sc.nextLine();
        try { //Exception handler for date parse
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            LocalDate date = LocalDate.parse(dueDate, formatter);

            //Makes sure user cannot put a date in the past
            if (date.isBefore(LocalDate.now())) {
                System.out.println("Date cannot be in the past!");
            } else {
                tasks.add(new Task(taskName, taskDescription, taskPriority, false, date));
                System.out.println("Task added");
            }

        } catch (DateTimeParseException e) {
            System.out.println("Invalid due date format");
        }
    }

    //Method to delete tasks
    public void deleteTask() {
        if (ifTaskEmpty(tasks)) {
            return;
        }
        System.out.println("Which task would you like to delete(By Number): ");
        taskNum = readIntSafely();
        int counter = 0;
        boolean deleted = false;
        Iterator<Task> iterator = tasks.iterator();

        //Loops iterations to delete the correct task/prints not found if task doesn't exist
        Task task = null;
        while (iterator.hasNext()) {
            task = iterator.next();
            counter++;
            if (counter == taskNum) {
                deleted = true;
                iterator.remove();
                System.out.println("Task " + taskNum + " deleted");
            }
        }
        if (!deleted) {
            System.out.println("Task " + taskNum + " not found");

        }
    }

    //Method to edit tasks
    public void editTask() {
        if (ifTaskEmpty(tasks)) return;

        Task selected = selectTaskToEdit();
        if (selected != null) {
            selectedTaskToEdit(selected);
        }
    }

    //Method to view Tasks
    public void viewTask() {
        if (ifTaskEmpty(tasks)) { //Checks for tasks before printing how to view
            return;
        }
        System.out.println("""
                How would you like to view your tasks?
                1. View normally
                2. View sorted (By Due Date, Completion, Priority)""");
        int viewChoice = sc.nextInt();
        switch (viewChoice) {
            case 1:
                viewNormalTasks();
                break;

            case 2: //Method to sort tasks by different categories
                viewSortedTasks();
                break;
        }
    }

    //Method for ifTaskEmpty to print(Less duplicates)
    public boolean ifTaskEmpty(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) { //If Task arraylist is empty, returns true
            System.out.println("You have no tasks to view.");
            return true;
        } else { //If there is tasks available then .isEmpty returns false
            System.out.println("Tasks you have: ");
            int i = 1;
            for (Task task : tasks) {
                System.out.println(i + ". " + task.briefString());
                i++;
            }
        }
        return false;
    }

    public void handleSearch() { //Prompts user for keyword then passes onto searchTask
        System.out.println("Enter a keyword to search for: ");
        String query = sc.nextLine();
        searchTasks(query);
    }

    //Method for Searching(Searches for Tasks containing user specified word)
    public void searchTasks(String query) {
        boolean found = false;
        for (Task task : tasks) { //Checks if keyword is in title or description
            if (task.getTitle().toLowerCase().contains(query.toLowerCase())
                    || (task.getDescription().toLowerCase().contains(query.toLowerCase()))) {
                System.out.println(task.briefString());
                found = true; //Marks true if keyword is found otherwise, false
            }
        }
        if (!found) {
            System.out.println("There is no such task or description found.");
        }
    }

    public void handleFilter() { //Prompts user for what to filter by then passes to filterTask method
        System.out.println("""
                 What would you like to filter tasks by:\s
                 1. Completed tasks
                 2. Priority(High/Med/Low)\s
                 3. Due date
                \s""");
        int filterChoice = sc.nextInt();
        filterTasks(filterChoice);
    }

    public void filterTasks(int filter) {
        boolean found = false;
        switch (filter) {
            case 1: //Filters tasks by completed tasks
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

            case 2: //Filters tasks by priority user inputs
                found = false;
                System.out.println("Which priority would you like to filter by(High/Med/Low): ");
                String userInput = sc.nextLine().toLowerCase().trim(); //Converts input to lower case then trims to take away spaces

                for (Task task : tasks) {
                    if (task.getPriority().equalsIgnoreCase(userInput)) { //Compares priority with user inputted priority
                        System.out.println(task.briefString());
                        found = true;
                    }
                }
                if (!found) {
                    System.out.println("No priority found with priority: " + userInput);
                }
                break;

            case 3: //Filters tasks by Due date
                found = false;
                System.out.println("Enter a due date to filter by (MM/DD/YYYY): ");
                String userDueDate = sc.nextLine();

                    LocalDate date;
                    try { //Exception handler for date parse
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        date = LocalDate.parse(userDueDate, formatter);
                    }   catch (DateTimeParseException e){ //Makes sure user cannot put a date in the past
                            System.out.println("Invalid due date format");
                            break;
                            }

                        for (Task task : tasks) { //Compares due dates with before dates & user entered date then prints out tasks
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

    //Method to read int safely(avoids prompt skipping)
    public int readIntSafely() {
        while (true) {
            try {
                taskNum = sc.nextInt();
                sc.nextLine();
                return taskNum;
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    public void viewNormalTasks() {
        if (ifTaskEmpty(tasks)) return;  //Shows tasks in a list from first to last
        System.out.println("Which task would you like to see in full? ");
        taskNum = readIntSafely();

        try {
            System.out.println(tasks.get(taskNum - 1));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid option");
        }
    }

    public void viewSortedTasks() {
        ArrayList<Task> copiedTasks = new ArrayList<>(tasks); //Adds all tasks from origin task list to copied list/avoids permanently changing up sort
        System.out.println("""
                How would you like to view your tasks?
                 1. By Due Date\s
                 2. By Completion Status\s
                 3. By Priority""");
        int viewType = sc.nextInt();
        switch (viewType) {
            //Uses a list.sort to take in copiedTasks(from origin) to sort by user input
            case 1:
                copiedTasks.sort(new DueDateComparator());
                break;

            case 2:
                copiedTasks.sort(new CompletionStatusComparator());
                break;

            case 3:
                copiedTasks.sort(new PriorityComparator());
                break;

            default:
                System.out.println("Invalid option");
                return;
        }
        if (ifTaskEmpty(copiedTasks)) return;
        boolean validTaskOption = false;
        while (!validTaskOption) {
            System.out.println("Tasks sorted");
            try { //Asks user which task in sorted task to see in full
                System.out.println("Which task would you like to see in full?");
                taskNum = readIntSafely();
                System.out.println(copiedTasks.get(taskNum - 1));
                validTaskOption = true;
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid option");
                ifTaskEmpty(copiedTasks);
            }
        }
    }

    //Method to select task to edit
    public Task selectTaskToEdit() {
        System.out.println("Which task would you like to edit(By Number): ");
        taskNum = readIntSafely();

        if (taskNum < 1 || taskNum > tasks.size()) {
            System.out.println("Invalid task number");
            return null;
        }
        return tasks.get(taskNum - 1); //Returns selected task
    }

    //Method to edit parts of selected task
    public void selectedTaskToEdit(Task selectedTask) {
        while (true) {
        System.out.println("1. Title\n2. Description\n3. Priority\n4. Due Date\n5. Completion Status\n6. Choose another task\n7. Return to main menu");
        int userOptionForEdit = readIntSafely(); //Clears input buffer

            switch (userOptionForEdit) { //Switch case to edit user selected task part
                case 1: //Changes Title
                    System.out.println("What would you like to change the title to: ");
                    selectedTask.setTitle(sc.nextLine());
                    System.out.println("Title updated!");
                    break;

                case 2: //Changes Description
                    System.out.println("What would you like to change the description to: ");
                    selectedTask.setDescription(sc.nextLine());
                    System.out.println("Description updated!");
                    break;

                case 3: //Changes Priority
                    System.out.println("What would you like to change the priority to: ");
                    selectedTask.setPriority(sc.nextLine());
                    System.out.println("Priority updated!");
                    break;

                case 4: //Changes Due Date
                    System.out.println("What would you like to change the date to(MM/DD/YYYY): ");
                    String replacementDate = sc.nextLine();
                    try { //Exception handler for date parse
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate newDate = LocalDate.parse(replacementDate, formatter);

                        //Makes sure user can't update it to past dates
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

                case 5: //Changes completion
                    System.out.println("Change Completion Status?");
                    //Flipper to flip task from pending to done / vice versa
                    selectedTask.setComplete(!selectedTask.isComplete());
                    //Confirms with user if task is complete/incomplete
                    System.out.println("Task marked as " + (selectedTask.isComplete() ? "\u001B[32mComplete ✓\u001B[0m" : "\u001B[33mIncomplete ⏳\u001B[0m"));
                    break;

                case 6: //Back to choosing a task to edit
                    if (ifTaskEmpty(tasks)) break;
                    continue;

                case 7: //Back to Main Menu
                    return;
                default:
                    System.out.println("Invalid option");
            }
        }
    }
}
