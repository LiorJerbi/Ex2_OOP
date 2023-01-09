package Ex2.Ex2_2;

import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class Task implements Callable,Comparable<Task> {
    private Callable task;
    private TaskType tPriority;
    private static Comparator<Task> comp = new Comparator<Task>() {
        @Override
        public int compare(Task o1, Task o2) {
            return o1.gettPriority().getPriorityValue()-o2.gettPriority().getPriorityValue();
        }
    };

    public static Comparator<Task> getComp() {
        return comp;
    }

    /*
        private constructor for inner class use only.(To hide the implementation from the user)
         */
    private Task(Callable task, TaskType prio) {
        this.task = task;
        tPriority = prio;
    }

    /*
    Factory method for Task object creation with the given priority.
    */
    public static Task createTask(Callable task, TaskType prio) {
        if (task == null) {
            System.out.println("task is null");
            return null;
        } else if (prio.getPriorityValue()>10||prio.getPriorityValue()<1) {
            System.out.println("Priority need to be in range of 1-10");
            return null;
        } else {
            return new Task(task, prio);
        }
    }

    /*
    Factory method for Task object creation with default(TaskType.OTHER) priority.
     */
    public static Task createTask(Callable task) {
        if (task == null) {
            System.out.println("task is null");
            return null;
        }
        return new Task(task, TaskType.OTHER);
    }
    public TaskType gettPriority() {
        return tPriority;
    }

    public void settPriority(TaskType tPriority) {
        this.tPriority = tPriority;
    }

    /*
    Compare method that compares Task priority values.
     */
    @Override
    public int compareTo(Task o) {
        return Integer.compare(tPriority.getPriorityValue(), o.gettPriority().getPriorityValue());
    }

    @Override
    public Object call() throws Exception {
            try {
                return task.call();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public String toString() {
        return "Task{" +
                "task=" + task +
                ", tPriority=" + tPriority +
                '}';
    }

    public boolean equals(Task o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return tPriority == o.tPriority;
    }

    @Override
    public int hashCode() {
        return Objects.hash(tPriority);
    }


}





