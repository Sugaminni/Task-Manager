import java.time.LocalDate;

public class Task {
    String title ;
    String description ;
    String priority ;
    boolean isComplete ;
    LocalDate dueDate ;
    private int taskID = 1;

    //Constructor for Task class
    public Task(String title, String description, String priority, boolean isComplete , LocalDate dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isComplete = isComplete;
        this.dueDate = dueDate;
        this.taskID++;
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
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }

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

    //Override for displaying full Task description
    @Override
    public String toString() {
        return  "Task: " + title + "\nDescription: " + description
                + "\nPriority: " + priority + "\nDue Date: "
                + dueDate.toString() + "\nStatus: " +
                (isComplete ? "\u001B[32mComplete ✓\u001B[0m" : dueDate.isBefore(LocalDate.now()) ? "\u001B[31mOVERDUE!!!\u001B[0m" : "\u001B[33mIncomplete ⏳\u001B[0m" +
                        "\nTaskID: " + taskID); //Checks if the due date has passed or not.
    }

    //Displays brief Task before going to full Task summary
    public String briefString() {
        return "ID: " + taskID + " | Task: " + title + " [Due: " + dueDate + "]";
    }

    public int getTaskID() {
        return taskID;
    }

    public void setTaskID(int taskID) {
        this.taskID = taskID;
    }
}