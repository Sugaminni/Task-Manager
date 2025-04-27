import java.util.Comparator;

//Comparator to compare priorities
public class PriorityComparator implements Comparator<Task> {

    @Override
    public int compare(Task t1, Task t2) {
        return Integer.compare(t1.getPriority(), (t2.getPriority()));
    }
}

