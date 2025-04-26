import java.util.Comparator;

//Comparator to compare priorities
public class PriorityComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return Integer.compare(t2.getPriority(), (t1.getPriority()));
    }
}

