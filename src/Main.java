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
            System.out.println("5. Exit");
            option = sc.nextInt();
            sc.nextLine();

            //Switch case for options in the menu
            switch (option) {
                case 1: //Case 1 asks user to add task
                    manager.addTask();
                    break;

                case 2: //Case 2 asks the user for a number to delete a task
                    manager.deleteTask();
                    break;

                case 3: //Case 3 to edit the tasks
                    manager.editTask();
                    break;

                case 4: //Case 4 Displays tasks
                    manager.viewTask();
                        break;

            }
        }
        while (option != 5);
    }
}
