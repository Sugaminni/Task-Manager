import java.util.Comparator;

//Comparator to compare Due Dates
public class DueDateComparator implements Comparator <Task>{
    @Override
    public int compare(Task t1, Task t2) {
        return t2.getDueDate().compareTo(t1.getDueDate());
    }
}
