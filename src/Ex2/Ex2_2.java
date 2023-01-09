package Ex2;


import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

// given the orders to implement class inside the class, the Ex2_2 is an empty class
public class Ex2_2{

    }
    class Task implements Callable,Comparable<Task> {
        private Callable task;
        private TaskType tPriority;
        //The Task comparator in charge of ordering tasks by tasktype in the queue
        private static Comparator<Task> comp = new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.gettPriority().getPriorityValue() - o2.gettPriority().getPriorityValue();
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
            } else if (prio.getPriorityValue() > 10 || prio.getPriorityValue() < 1) {
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

        /*
        Compare method that compares Task priority values.
         */
        @Override
        public int compareTo(Task o) {
            return Integer.compare(tPriority.getPriorityValue(), o.gettPriority().getPriorityValue());
        }

        /*
        Uses the super calL() method for an asynchronous task which returns a generic value
         */
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
    //Represents new type of ThreadPool that supports tasks priority queue
    class CustomExecutor{

    private PriorityBlockingQueue taskQ;                    //a task priority queue
    private int maxPrio,numOfCores,corePoolSize,maxPoolSize;
    private ThreadPoolExecutor tPool;                       //the executor type
    public static Future future;


/*
an empty constructor of the class that implements the executor bounds for each user's processors limitations
 */
    public CustomExecutor(){
        numOfCores = Runtime.getRuntime().availableProcessors();
        corePoolSize = numOfCores/2;
        maxPoolSize = numOfCores-1;
        this.taskQ = new PriorityBlockingQueue<Task>(maxPoolSize,Task.getComp());
        this.tPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize, 300, TimeUnit.MILLISECONDS,taskQ);
        maxPrio = Integer.MAX_VALUE;
    }
    //submit an asynchronic operation without task priority as parameter
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
    // submit an asynchronic operation with task priority as parameter
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
    //method to submit a task to the priority queue
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
    //returns the highest priority that has been inserted to the queue
    public int getCurrentMax(){
        return maxPrio;
    }
    /* that method stops the CustomExecutor activity:
     done by blocking any other tasks from been submitted to the queue
     and finish all the remaining task in the line
     */
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
    //return value from future variable, waits if needed
    public static <V> Object get() throws InterruptedException, ExecutionException {
        return future.get();
    }
    //return the value from future variable, waits until timeout to do so
    public static <V> Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

    public int getMaximumPoolSize() {
        return tPool.getMaximumPoolSize();
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }
    //shuts down the executor without considering the undone tasks
    //return list of all undone tasks
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
    //how much time to wait until stops
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


