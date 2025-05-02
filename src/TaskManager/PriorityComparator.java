package TaskManager;
import java.util.Comparator;

//Comparator to compare priorities
public class PriorityComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return Integer.compare(priorityValue(t2.getPriority()), priorityValue(t1.getPriority()));
    }

    //Method for translating priority Strings to integers
    public int priorityValue(String priority) {
        return switch (priority.toLowerCase().trim()) {
            case "high" -> 3;
            case "med" -> 2;
            case "low" -> 1;
            default -> 0;
        };
    }
}

