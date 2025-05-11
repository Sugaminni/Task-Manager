package TaskManager;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Task> tasks = new ArrayList<>();
        int option, taskNum;
        TaskManager manager = new TaskManager();

        //Do while loop that takes in user input for menu
        do {
            System.out.println("What would you like to do?");
            System.out.println("1. Add Task(s)");
            System.out.println("2. Delete Task(s)");
            System.out.println("3. Edit Task(s)");
            System.out.println("4. View Task(s)");
            System.out.println("5. Search Task(s)");
            System.out.println("6. Filter Task(s)");
            System.out.println("7. Undo last action");
            System.out.println("8. Redo last action");
            System.out.println("9. Mark Task(s) completion status");
            System.out.println("10. Export Tasks");
            System.out.println("11. Exit");
            option = sc.nextInt();
            sc.nextLine();

            //Switch case for options in menu
            switch (option) {
                case 1: //asks user to add task
                    manager.addTask();
                    break;

                case 2: //asks the user for a number to delete a task
                    manager.deleteTask();
                    break;

                case 3: //to edit the tasks
                    manager.handleTaskEdit();
                    break;

                case 4: //Displays tasks
                    manager.displayTask();
                        break;

                case 5: //Searches for task based on keyword
                    manager.handleSearch();
                    break;

                case 6:
                    manager.handleFilter();
                    break;

                case 7:
                    manager.undoLastAction();
                    break;

                case 8:
                    manager.redoLastAction();
                    break;

                case 9:
                    manager.markTasksAsComplete();
                    break;

                case 10:
                    manager.exportTasks();
                    break;

                default:
                    System.out.println("Invalid option");
                    break;
            }
        }
        while (option != 11);
    }
}
