package TaskManager;
import java.util.Comparator;

//Comparator to compare priorities
public class WorkloadComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return t2.getWorkload().ordinal() - t1.getWorkload().ordinal(); //Returns comparison in descending order
    }
}
