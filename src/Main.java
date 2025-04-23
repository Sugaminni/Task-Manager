import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Task> Tasks = new ArrayList<Task>();

        int option;

        //Do while loop that takes in user input for menu
        do {
            System.out.println("What would you like to do?");
            System.out.println("1. Add Task");
            System.out.println("2. Delete Task");
            System.out.println("3. Edit Task");
            System.out.println("4. View all Tasks");
            System.out.println("5. Exit");
            option = sc.nextInt();
            sc.nextLine();

            //Switch case for options in the menu
            switch (option) {
                case 1:
                    System.out.println("Enter task : ");
                    String taskName = sc.nextLine();
                    System.out.println("Enter task description : ");
                    String taskDescription = sc.nextLine();
                    System.out.println("Enter task priority : ");
                    int taskPriority = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter task due date (MM/DD/YYYY): ");
                    String dueDate = sc.nextLine();
                    try { //Throws exception for incorrect date format
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        LocalDate date = LocalDate.parse(dueDate, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid due date format");
                    }
                    break;

                case 2:
                    System.out.println("Which task do you want to delete? : ");
                    int taskNum = sc.nextInt();
                    break;

                case 3:
                    System.out.println("Which task do you want to edit? : ");
                    int taskNum2 = sc.nextInt();
                    break;

                case 4:
                    for (Task task : Tasks) {
                        System.out.println();
                    }
            }

        }while (option != 5) ;


        for (Task task : Tasks) {
            Task myTask = new Task("Finish Website", "Work on website", 1, true);

        }
    }
}