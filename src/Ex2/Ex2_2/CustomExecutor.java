package Ex2.Ex2_2;

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

    public static Object get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    public static Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

    public int getMaximumPoolSize() {
        return tPool.getMaximumPoolSize();
    }

    public int getCorePoolSize() {
        return corePoolSize;
    }
}
