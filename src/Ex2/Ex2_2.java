package Ex2;


import java.util.Objects;
import java.util.concurrent.*;

public class Ex2_2{
    public static class Task implements Callable,Comparable<Task>{
        private Callable task;
        private TaskType tPriority;


        /*
        private constructor for inner class use only.(To hide the implementation from the user)
         */
        private Task(Callable task,TaskType prio){
            this.task=task;
            tPriority = prio;
        }
        /*
        Factory method for Task object creation with default(TaskType.OTHER) priority.
        */
        public static Task createTask(Callable task, TaskType prio){
            Task t=null;
            if(task==null){
                System.out.println("task is null");
                return null;}
            else if(!TaskType.validatePriority(prio.getPriorityValue())){
                System.out.println("Priority need to be in range of 1-10");
                return null;
            }
            else {
                return new Task(task, prio);
            }
        }
        /*
        Factory method for Task object creation with default(TaskType.OTHER) priority.
         */
        public static Task createTask(Callable task){
            Task t = null;
            if(task==null){
                System.out.println("task is null");
                return null;
            }
            return new Task(task,TaskType.OTHER);
        }

        public Callable<Object> getTask() {
            return task;
        }

        public void setTask(Callable<Object> task) {
            this.task = task;
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
            return Integer.compare(this.gettPriority().getPriorityValue(), o.gettPriority().getPriorityValue());
        }

        @Override
        public Object call() throws Exception {
            if(this.task!=null) {
                try {
                    return task.call();
                }catch (InterruptedException | ExecutionException e){
                    throw new RuntimeException(e);
                }
            }
            else{
                System.out.println("task is null");
                return null;
            }
        }

        @Override
        public String toString() {
            return "Task{" +
                    "task=" + task +
                    ", tPriority=" + tPriority +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Task task = (Task) o;
            return tPriority == task.tPriority;
        }

        @Override
        public int hashCode() {
            return Objects.hash(tPriority);
        }

        public enum TaskType {
            COMPUTATIONAL(1){
                @Override
                public String toString(){return "Computational Task";}
            },
            IO(2){
                @Override
                public String toString(){return "IO-Bound Task";}
            },
            OTHER(3){
                @Override
                public String toString(){return "Unknown Task";}
            };
            private int typePriority;
            private TaskType(int priority){
                if (validatePriority(priority)) typePriority = priority;
                else
                    throw new IllegalArgumentException("Priority is not an integer");
            }
            public void setPriority(int priority){
                if(validatePriority(priority)) this.typePriority = priority;
                else
                    throw new IllegalArgumentException("Priority is not an integer");
            }
            public int getPriorityValue(){
                return typePriority;
            }
            public TaskType getType(){
                return this;
            }
            /**
             * priority is represented by an integer value, ranging from 1 to 10
             * @param priority
             * @return whether the priority is valid or not
             */
            private static boolean validatePriority(int priority){
                if (priority < 1 || priority >10) return false;
                return true;
            }
        }
    }

    public class CustomExecutor{

        private BlockingQueue<Runnable> taskQ;
        private int maxPrio,numOfCores,corePoolSize,maxPoolSize;
        private ThreadPoolExecutor tPool;



        public CustomExecutor(){
            numOfCores = Runtime.getRuntime().availableProcessors();
            corePoolSize = numOfCores/2;
            maxPoolSize = numOfCores-1;
            taskQ = new PriorityBlockingQueue<>();
            tPool = new ThreadPoolExecutor(corePoolSize,maxPoolSize,300,TimeUnit.MILLISECONDS,taskQ);
            maxPrio = Integer.MAX_VALUE;
        }

        public Future<Object> submit(Callable task){
            Future<Object> fut = null;
            if(task!=null){
                Task nt1 = Task.createTask(task);
                if(nt1.gettPriority().getPriorityValue()<maxPrio){
                    maxPrio=nt1.gettPriority().getPriorityValue();
                }
                fut=submit(task);
            }
            else{
                System.out.println("error occurred");
            }
            return fut;
        }
        public Future submit(Callable task, Task.TaskType prio){
            Future fut = null;
            if(task!=null){
                if(prio!=null){
                    Task nt1 = Task.createTask(task,prio);
                    if(nt1.gettPriority().getPriorityValue()<maxPrio){
                        maxPrio=nt1.gettPriority().getPriorityValue();
                    }
                    fut=submit(nt1);
                }
                else{
                    fut = submit(task);
                }
            }
            else{
                System.out.println("error occurred");
            }
            return fut;
        }
        public Future submit(Task task){
            Future fut = null;
            if(task == null){
                System.out.println("null task");
                return null;
            }
            else{
                if(taskQ.size()>maxPoolSize){
                    System.out.println("Not enough space for another Task");
                }
                else {
                    if(maxPrio>task.gettPriority().getPriorityValue()){
                        maxPrio=task.gettPriority().getPriorityValue();
                    }
                    try {
                        fut = tPool.submit(task);
                    } catch (Exception e) {
                        tPool.shutdown();
                        throw new RuntimeException(e);
                    }
                }
                return fut;
            }
        }
        public int getCurrentMax(){
            return maxPrio;
        }

        public void shutdown() {
            tPool.shutdown();
            while(!tPool.isTerminated()) {
                try {
                    tPool.awaitTermination(300,TimeUnit.MILLISECONDS);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }


        public int getMaximumPoolSize() {
            return tPool.getMaximumPoolSize();
        }
    }

}

