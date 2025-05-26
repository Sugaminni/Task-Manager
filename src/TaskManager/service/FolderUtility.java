package TaskManager.service;

import TaskManager.model.Task;

import java.util.*;
import java.util.stream.Collectors;

public class FolderUtility {

    public static List<Task> filterTasksById(Set<Integer> taskIds, List<Task> allTasks) {
        if (taskIds == null || allTasks == null) return Collections.emptyList();

        return allTasks.stream()
                .filter(task -> taskIds.contains(task.getTaskID()))
                .collect(Collectors.toList());
    }
}
