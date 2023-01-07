import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

public class Ex2 {
    public class Ex2_1{
        public static String[] createTextFiles(int n, int seed, int bound){
            String[] files = new String[n];
            Random rand = new Random(seed);
            for (int i = 1; i <= n; i++) {
                File newFile=null;
                try {
                    newFile= new File("file_" + i + ".txt");
                    if(!(newFile.createNewFile())){
                        System.out.println("File already exists");
                    }
                }catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                    deleteFiles(files);
                }
                int lines = rand.nextInt(bound);
//                System.out.print(x+", ");
                try {
                    FileWriter myWriter = new FileWriter(newFile);
                    for(int j=0;j<lines;j++){
                        myWriter.write("Hello World from yael and lior\n");
                    }
                    myWriter.close();
                }catch (IOException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                    deleteFiles(files);
                }
                System.out.println(newFile.getName()+" created with "+lines+" lines");
                files[i-1]=newFile.getName();
            }
            return files;

        }
        public static int getNumOfLines(String[] fileNames){
            int lineCounter=0;
            File tempFile=null;
            Scanner fileReader=null;
            for (String fileName : fileNames) {
                try {
                    tempFile = new File(fileName);
                    fileReader = new Scanner(tempFile);
                    while (fileReader.hasNextLine()) {
                        lineCounter++;
                        fileReader.nextLine();
                    }
                    fileReader.close();
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                    deleteFiles(fileNames);
                }
            }
            return lineCounter;
        }
        public static int getNumOfLinesThreads(String[] fileNames){
            int lineCounter=0;
            for (String fileName : fileNames) {
                LineCountThread tempThread = new LineCountThread(fileName);
                tempThread.start();
                try {
                    tempThread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                lineCounter += tempThread.getLines();
            }
            return lineCounter;
        }
        public static int getNumOfLinesThreadPool(String[] fileNames){
            ExecutorService filesTPool = Executors.newFixedThreadPool(fileNames.length);
            int lineCounter = 0;
            List<Future<Integer>> list = new ArrayList<Future<Integer>>();
            for (String fileName : fileNames) {
                Callable<Integer> ft = new LineCountTPool(fileName);
                Future<Integer> future = filesTPool.submit(ft);
                list.add(future);
            }
            for(Future<Integer> fut:list){
                try{
                    lineCounter += (int)fut.get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            filesTPool.shutdown();
            return lineCounter;
        }


        public static void deleteFiles(String[] filenames){
            for(int i=0;i<filenames.length;i++) {
                File obj = new File("file_"+ (i+1) +".txt");
                if (obj.delete()) {
                    //System.out.println("Deleted the file: " + obj.getName());
                    filenames[i]=null;
                } else {
                    System.out.println("Failed to delete the file.");
                }
            }
            System.out.println("All files deleted\n");
        }
        @Override
        public String toString() {
            return super.toString();
        }

    }

    public class Ex2_2{

        public class Task implements Callable<Object>,Comparable<Task>{
            private Callable<Object> task;
            private TaskType tPriority;

            /*
            private constructor for inner class use only.(To hide the implementation from the user)
             */
            private Task(Callable<Object> task,TaskType prio){
                this.task=task;
                tPriority = prio;
            }

            private Task creates(Callable<Object> task,TaskType prio){
                Task t= new Task(task,prio);
                return t;
            }
            /*
            Factory method for Task object creation with default(TaskType.OTHER) priority.
            */
            public static Task createTask(Callable<Object> task,TaskType prio){
                Task t=null;
                if(task==null){
                    System.out.println("task is null");
                    return null;}
                if(!TaskType.validatePriority(prio.getPriorityValue())){
                    System.out.println("Priority need to be in range of 1-10");
                    return null;
                }
                return t.creates(task,prio);
            }
            /*
            Factory method for Task object creation with default(TaskType.OTHER) priority.
             */
            public static Task createTask(Callable<Object> task){
                Task t = null;
                if(task==null){
                    System.out.println("task is null");
                    return null;
                }
                    return t.creates(task,TaskType.OTHER);
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
                        return this.task.call();
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

        public class CustomExecutor {

            private static PriorityBlockingQueue<Task> taskPool;
            private static int maxPrio=Integer.MAX_VALUE;
            ScheduledThreadPoolExecutor stpe;
            static final int numOfCores = Runtime.getRuntime().availableProcessors();
            static final int corePoolSize = numOfCores/2;
            static final int maxPoolSize = numOfCores-1;


            public CustomExecutor(){
//                stpe = new ScheduledThreadPoolExecutor(corePoolSize);
//                stpe.setMaximumPoolSize(maxPoolSize);
//                stpe.setKeepAliveTime(300,TimeUnit.MILLISECONDS);
                taskPool  = new PriorityBlockingQueue<Task>();
            }

            public Future<Object> submit(Callable<Object> task){
                Future<Object> fut = null;
                if(task!=null){
                    Task nt1 = Task.createTask(task);
                    if(nt1.gettPriority().getPriorityValue()<maxPrio){
                        maxPrio=nt1.gettPriority().getPriorityValue();
                    }
                    fut=CustomExecutor.submit(nt1);
                }
                else{
                    System.out.println("error occurred");
                }
                return fut;
            }
            public Future<Object> submit(Callable<Object> task, Task.TaskType prio){
                Future<Object> fut = null;
                if(task!=null){
                    if(prio!=null){
                        Task nt1 = Task.createTask(task,prio);
                        if(nt1.gettPriority().getPriorityValue()<maxPrio){
                            maxPrio=nt1.gettPriority().getPriorityValue();
                        }
                        fut=CustomExecutor.submit(nt1);
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
            public static Future<Object> submit(Task task){
                Future<Object> fut = null;
                if(taskPool.size()>maxPoolSize){
                    System.out.println("Not enough space for another Task");
                }
                else {
                    taskPool.add(task);
                    try {
                        fut = (Future<Object>) task.call();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
                return fut;
            }
        }

    }

    public static void main(String[] args) {
        String[] test =new String[10000];
        test = Ex2_1.createTextFiles(test.length,1,99999);
        long startTime = System.currentTimeMillis();
        int ch = Ex2_1.getNumOfLines(test);
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("[NORMAL] Lines:"+ch+" Timer:"+elapsedTime+" ms");
        startTime = System.currentTimeMillis();
        int ch1 = Ex2_1.getNumOfLinesThreads(test);
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("[Thread] Lines:"+ch1+" Timer:"+elapsedTime+" ms");
        startTime = System.currentTimeMillis();
        int ch2 = Ex2_1.getNumOfLinesThreadPool(test);
        elapsedTime = System.currentTimeMillis() - startTime;
        System.out.println("[ThreadPool] Lines:"+ch2+" Timer:"+elapsedTime+" ms");
        Ex2_1.deleteFiles(test);

    }
}
