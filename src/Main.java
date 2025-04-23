import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ArrayList<Task> Tasks = new ArrayList<>();
        int option;

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
                    System.out.println("Enter task : ");
                    String taskName = sc.nextLine();
                    System.out.println("Enter task description : ");
                    String taskDescription = sc.nextLine();
                    System.out.println("Enter task priority : ");
                    int taskPriority = sc.nextInt();
                    sc.nextLine();
                    System.out.println("Enter task due date (MM/DD/YYYY): ");
                    String dueDate = sc.nextLine();
                    LocalDate date = null;
                    try { //Throws exception for incorrect date format
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                        date = LocalDate.parse(dueDate, formatter);
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid due date format");
                    }
                    if (date != null) { //If date is not null then program adds tasks into an arraylist
                        Tasks.add(new Task(taskName, taskDescription, taskPriority, false, date));
                        System.out.println("Task added");
                    }
                    break;

                case 2: //Case 2 asks the user for a number to delete a task
                    if (Tasks.isEmpty()) { //Displays tasks and asks user which they want to delete
                        System.out.println("There are no tasks!");
                        break;
                    } else {
                        System.out.println("Tasks you have: ");
                        int i = 1;
                        for (Task task : Tasks) {
                            System.out.println(i + ". " + task.title + ": " + task.description);
                            i++;
                        }
                    }
                    System.out.println("Which task would you like to delete(By Number): ");
                    int taskNum = sc.nextInt();
                    int counter = 0;
                    boolean deleted = false;
                    Iterator<Task> iterator = Tasks.iterator();

                    //Loops iterations to delete the correct task/prints not found if task doesn't exist
                    while (iterator.hasNext()) {
                        Task task = iterator.next();
                        counter++;
                        if (counter == taskNum) {
                            deleted = true;
                            iterator.remove();
                            System.out.println("Task " + taskNum + " deleted");
                        }
                    }
                    if (!deleted) {
                        System.out.println("Task" + taskNum + " not found");
                    }
                    break;


                case 3:
                    System.out.println("Which task do you want to edit? : ");
                    int taskNum2 = sc.nextInt();
                    break;

                case 4:
                    for (Task task : Tasks) {
                        System.out.println(task);
                    }
            }

        } while (option != 5);
    }
}