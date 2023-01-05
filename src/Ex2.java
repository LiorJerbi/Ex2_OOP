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
