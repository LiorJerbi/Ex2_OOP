package Ex2.Ex2_2;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public class CustomExecutor{

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
