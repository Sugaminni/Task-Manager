import java.time.LocalDate;

public class Task {
    String title ;
    String description ;
    int priority ;
    boolean isComplete ;
    LocalDate dueDate = LocalDate.now();

    //Constructor for Task class
    public Task(String title, String description, int priority, boolean isComplete) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.isComplete = isComplete;
        this.dueDate = LocalDate.now();
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
    public int getPriority() {
        return priority;
    }
    public void setPriority(int priority) {
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

    //Override for java printing
    @Override
    public String toString() {
        return  title + "\t" + description + "\t" + priority + "\t" + dueDate.toString() + (isComplete ? " Done" : " Pending");
    }

}