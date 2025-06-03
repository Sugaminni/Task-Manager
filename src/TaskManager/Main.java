package TaskManager;

import TaskManager.service.FolderManager;
import TaskManager.service.TaskManager;
import TaskManager.service.TaskUtility;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int option;
        TaskManager manager = new TaskManager();
        FolderManager folderManager = new FolderManager();

        // CLI Mode: Command Line Interface Loop
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
            System.out.println("10. Import Tasks");
            System.out.println("11. Export Tasks");
            System.out.println("12. Folders");
            System.out.println("13. Export Tasks in Markdown");
            System.out.println("14. Exit");

            option = TaskUtility.readIntSafely(sc); // Read user input for menu option, and validate it is a valid option (1-12)

            // Switch case for menu options
            switch (option) {
                case 1: manager.addTask(); break;
                case 2: manager.deleteTask(); break;
                case 3: manager.handleTaskEdit(); break;
                case 4: manager.displayTask(); break;
                case 5: manager.handleSearch(); break;
                case 6: manager.handleFilter(); break;
                case 7: manager.undoLastAction(); break;
                case 8: manager.redoLastAction(); break;
                case 9: manager.markTasksAsComplete(); break;
                case 10: manager.importTasks(); break;
                case 11: manager.exportTasks(); break;
                case 12: manager.handleFolderMenu(); break;
                case 13: TaskUtility.handleMarkdownExport(manager, folderManager, sc); break;
                default:
                    break;
            }
        } while (option != 13);
    }
}


