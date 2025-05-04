package TaskManager;
import java.util.Comparator;

//Comparator to compare priorities
public class PriorityComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return t2.getPriority().ordinal() - t1.getPriority().ordinal(); //Returns comparison in descending order
    }
}

