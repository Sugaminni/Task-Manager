import java.time.LocalDate;

public class Task {
    String title = "";
    String description = "";
    int priority = 1;
    boolean isComplete = true;
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

    //Getter and setter for Description
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }


}