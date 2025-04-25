import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class TaskManager {
    ArrayList<Task> tasks = new ArrayList<>();
    Scanner sc = new Scanner(System.in);

    int taskNum;

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    //Method to add tasks
     public void addTask() {
         System.out.println("Enter task : ");
         String taskName = sc.nextLine();
         System.out.println("Enter task description : ");
         String taskDescription = sc.nextLine();
         System.out.println("Enter task priority : ");
         int taskPriority = sc.nextInt();
         sc.nextLine();
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
             taskNum = sc.nextInt();
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
             if (ifTaskEmpty(tasks))
                 return;
             boolean returnToMainMenu = false;

             outerEditLoop:
             while (true) { //Outer loop to print task user wants to edit
                 System.out.println("Which task would you like to edit(By Number): ");
                 taskNum = sc.nextInt();
                 Task selectedTask = tasks.get(taskNum - 1);
                 while (true) { //Inner loop for program to keep prompting a selection
                     int userOptionForEdit;//Menu to display which part of task to edit
                     System.out.println("1. Title\n2. Description\n3. Priority\n4. Due Date\n5. Completion Status\n6. Choose another task\n7. Return to main menu");
                     userOptionForEdit = sc.nextInt();
                     sc.nextLine(); //Clears input buffer

                     switch (userOptionForEdit) { //Switch case to edit user selected task part
                         case 1: //Changes Title
                             System.out.println("What would you like to change the title to: ");
                             selectedTask.setTitle(sc.nextLine());
                             break;

                         case 2: //Changes Description
                             System.out.println("What would you like to change the description to: ");
                             selectedTask.setDescription(sc.nextLine());
                             break;

                         case 3: //Changes Priority
                             System.out.println("What would you like to change the priority to: ");
                             selectedTask.setPriority(sc.nextInt());
                             sc.nextLine();
                             break;

                         case 4: //Changes Due Date
                             System.out.println("What would you like to change the date to(MM/DD/YYYY): ");
                             String replacementDate = sc.nextLine();
                             try { //Exception handler for date parse
                                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                                 LocalDate newDate = LocalDate.parse(replacementDate, formatter);

                                 //Makes sure user can't update it into past dates
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
                             selectedTask.setComplete(!selectedTask.isComplete);
                             //Confirms with user if task is complete/incomplete
                             System.out.println("Task marked as " + (selectedTask.isComplete() ? "\u001B[32mComplete ✓\u001B[0m" : "\u001B[33mIncomplete ⏳\u001B[0m"));
                             break;

                         case 6: //Back to choosing a task to edit
                             if (ifTaskEmpty(tasks)) break;
                             continue outerEditLoop;

                         case 7: //Back to Main Menu
                             returnToMainMenu = true;
                             break;
                         default:
                             System.out.println("Invalid option");
                     }
                     if (returnToMainMenu) break outerEditLoop; //Breaks outer loop
                 }
             }
         }

         //Method to view Tasks
         public void viewTask() {
             ifTaskEmpty(tasks);
         }

    //Method for ifTaskEmpty to print(Less duplicates)
    public boolean ifTaskEmpty(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) { //Displays tasks and asks user which they want to edit
            System.out.println("There are no tasks!");
            return true;
        } else { //Increments the Tasks with numbers, making it easier to tell which to edit
            System.out.println("Tasks you have: ");
            int i = 1;
            for (Task task : tasks) {
                System.out.println(i + ". " + task + "\n");
                i++;
            }
        } //Asks the user which task they want to edit
        return false;
    }
}
