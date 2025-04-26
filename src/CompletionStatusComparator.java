import java.util.Comparator;

//Comparator to compare completion status
public class CompletionStatusComparator implements Comparator <Task>{

    @Override
    public int compare(Task t1, Task t2) {
        return Boolean.compare(t2.isComplete(), t1.isComplete());
    }
}
