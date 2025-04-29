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
             if (ifTaskEmpty(tasks))
                 return;
             boolean returnToMainMenu = false;

             outerEditLoop: //For Continue statement to continue outer while loop
             while (true) { //Outer loop to print task user wants to edit
                 System.out.println("Which task would you like to edit(By Number): ");
                 taskNum = readIntSafely();
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
             System.out.println("How would you like to view your tasks?\n"
                     + "1. View normally\n" +
                     "2. View sorted (By Due Date, Completion, Priority)");
             int viewchoice = sc.nextInt();
             switch (viewchoice){
                 case 1: {
                     if (ifTaskEmpty(tasks)) break;
                     System.out.println("Which task would you like to see? ");
                     taskNum = readIntSafely();
                     System.out.println(tasks.get(taskNum - 1));
                     break;
                 }

                 case 2: //Method to sort tasks by different categories
                     copiedTasks.clear(); //Clears copiedTasks to avoid duplicate printing
                     copiedTasks.addAll(tasks); //Adds all tasks from origin task list to copied list/avoids permanently changing up sort
                     System.out.println("""
                         How would you like to view your tasks?
                          1. By Due Date\s
                          2. By Completion Status\s
                          3. By Priority""");
                 int viewType = sc.nextInt();
                 switch (viewType){
                     //Uses a list.sort to take in copiedTasks(from origin) to sort by user input
                     case 1: copiedTasks.sort(new DueDateComparator());
                     System.out.println("Tasks sorted by Due Date");
                     ifTaskEmpty(copiedTasks);
                     break;

                     case 2: copiedTasks.sort(new CompletionStatusComparator());
                     System.out.println("Tasks sorted by Completion Status");
                     ifTaskEmpty(copiedTasks);
                     break;

                     case 3: copiedTasks.sort(new PriorityComparator());
                     System.out.println("Tasks sorted by Priority");
                     ifTaskEmpty(copiedTasks);
                     break;
                 }
             }

         }

    //Method for ifTaskEmpty to print(Less duplicates)
    public boolean ifTaskEmpty(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) { //If Task arraylist is empty, returns true
            System.out.println("There are no tasks!");
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

    //Method to read int safely(avoids prompt skipping)
    public int readIntSafely() {
        while (true) {
            try {
                taskNum = sc.nextInt();
                sc.nextLine();
                return taskNum;
            }
            catch (Exception e) {
                sc.nextLine();
                System.out.println("Please enter a valid integer.");
            }
        }
    }
}
