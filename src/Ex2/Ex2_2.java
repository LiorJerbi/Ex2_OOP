package Ex2;


import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

public class Ex2_2{

    }
    class Task implements Callable,Comparable<Task> {
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


    class CustomExecutor{

    private PriorityBlockingQueue taskQ;
    private int maxPrio,numOfCores,corePoolSize,maxPoolSize;
    private ThreadPoolExecutor tPool;
    public static Future future;



    public CustomExecutor(){
        numOfCores = Runtime.getRuntime().availableProcessors();
        corePoolSize = numOfCores/2;
        maxPoolSize = numOfCores-1;
        this.taskQ = new PriorityBlockingQueue<Task>(maxPoolSize,Task.getComp());
        this.tPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize, 300, TimeUnit.MILLISECONDS,taskQ);
        maxPrio = Integer.MAX_VALUE;
    }

    public <T> Future<T> submit(Callable task){
        if(task!=null){
            Task nt1 = Task.createTask(task);
            if(nt1.gettPriority().getPriorityValue()<maxPrio){
                maxPrio=nt1.gettPriority().getPriorityValue();
            }
            future=submit(task);
        }
        else{
            System.out.println("error occurred");
        }
        return future;
    }
    public <T> Future<T> submit(Callable task, TaskType prio){
        if(task!=null){
            if(prio!=null){
                Task nt1 = Task.createTask(task,prio);
                assert nt1 != null;
                if(nt1.gettPriority().getPriorityValue()<maxPrio){
                    maxPrio=nt1.gettPriority().getPriorityValue();
                }
                future =submit(nt1);
            }
            else{
                future = submit(task);
            }
        }
        else{
            System.out.println("error occurred");
        }
        return future;
    }
    public <T> Future<T> submit(Task task){
        if(task == null){
            System.out.println("null task");
            return null;
        }
        else{
            if(taskQ.size()>=maxPoolSize){
                System.out.println("Not enough space for another Task");
            }
            else {
                if(maxPrio>task.gettPriority().getPriorityValue()){
                    maxPrio=task.gettPriority().getPriorityValue();
                }
                try {
                    future = tPool.submit(task);
                } catch (Exception e) {
                    gracefullyTerminate();
                    throw new RuntimeException(e);
                }
            }
            return future;
        }
    }
    public int getCurrentMax(){
        return maxPrio;
    }

    public void gracefullyTerminate() {
        tPool.shutdown();
        while(!tPool.isTerminated()) {
            try {
                tPool.awaitTermination(300,TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <V> Object get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    public static <V> Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

    public int getMaximumPoolSize() {
        return tPool.getMaximumPoolSize();
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }

    public List<Runnable> shutdownNow() {
        return tPool.shutdownNow();
    }

    public boolean isShutdown() {
        return tPool.isShutdown();
    }

    public boolean isTerminating() {
        return tPool.isTerminating();
    }

    public boolean isTerminated() {
        return tPool.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return tPool.awaitTermination(timeout, unit);
    }

    public long getKeepAliveTime(TimeUnit unit) {
        return tPool.getKeepAliveTime(unit);
    }

    public int getPoolSize() {
        return tPool.getPoolSize();
    }

    public int getActiveCount() {
        return tPool.getActiveCount();
    }

    public int getLargestPoolSize() {
        return tPool.getLargestPoolSize();
    }

    public long getTaskCount() {
        return tPool.getTaskCount();
    }

    public long getCompletedTaskCount() {
        return tPool.getCompletedTaskCount();
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return tPool.invokeAny(tasks);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return tPool.invokeAny(tasks, timeout, unit);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return tPool.invokeAll(tasks);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return tPool.invokeAll(tasks, timeout, unit);
    }
}

    enum TaskType {
    COMPUTATIONAL(1) {
        @Override
        public String toString() {
            return "Computational Task";
        }
    },
    IO(2) {
        @Override
        public String toString() {
            return "IO-Bound Task";
        }
    },
    OTHER(3) {
        @Override
        public String toString() {
            return "Unknown Task";
        }
    };
    private int typePriority;

    private TaskType(int priority) {
        if (validatePriority(priority)) typePriority = priority;
        else
            throw new IllegalArgumentException("Priority is not an integer");
    }

    public void setPriority(int priority) {
        if (validatePriority(priority)) this.typePriority = priority;
        else
            throw new IllegalArgumentException("Priority is not an integer");
    }

    public int getPriorityValue() {
        return typePriority;
    }

    public TaskType getType() {
        return this;
    }

    /**
     * priority is represented by an integer value, ranging from 1 to 10
     *
     * @param priority
     * @return whether the priority is valid or not
     */
    private static boolean validatePriority(int priority) {
        if (priority < 1 || priority > 10) return false;
        return true;
    }
}


