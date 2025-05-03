package TaskManager;
import java.time.LocalDate;

public class Task {
    String title;
    String description;
    Priority priority;
    Workload workload;
    boolean isComplete;
    LocalDate dueDate;
    private static int nextTaskID = 1;
    private int taskID;

    //Constructor for Task class
    public Task(String title, String description, Priority priority, Workload workload, boolean isComplete, LocalDate dueDate)
    {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.workload = workload;
        this.isComplete = isComplete;
        this.dueDate = dueDate;
        this.taskID = nextTaskID++;
    }

    //Getter and Setter for Title
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    //Getter and Setter for Description
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    //Getter and Setter for Priority
    public Priority getPriority() {
        return priority;
    }
    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Workload getWorkload() { return workload;}
    public void setWorkload(Workload workload) {this.workload = workload;}

    //Getter and Setter for Completion
    public boolean isComplete() {
        return isComplete;
    }
    public void setComplete(boolean isComplete) {
        this.isComplete = isComplete;
    }

    //Getter and Setter for Due Dates
    public LocalDate getDueDate() {
        return dueDate;
    }
    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getTaskID() {
        return taskID;
    }
    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    //Override for displaying full Task description
    @Override
    public String toString() {
        return  "Task ID: " + taskID +"\nTask: " + title + "\nDescription: " + description
                + "\nPriority: " + priority +
                "\nWork Load: " + workload + "\nDue Date: "
                + dueDate.toString() + "\nStatus: " +
                (isComplete ? "\u001B[32mComplete ✓\u001B[0m" : dueDate.isBefore(LocalDate.now()) ? "\u001B[31mOVERDUE!!!\u001B[0m" : "\u001B[33mIncomplete ⏳\u001B[0m" +
                        "\nTaskID: " + taskID); //Checks if the due date has passed or not.
    }

    //Displays brief Task before going to full Task summary
    public String briefString() {
        return "Task ID: " + taskID + " | Task: " + title + " [Due: " + dueDate + "]";
    }
}

