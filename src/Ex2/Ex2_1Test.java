package Ex2;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

class Ex2_1Test {
    @Test
    public void test() {
//        Ex2_1 ex2 = new Ex2_1();
//        String[] test =new String[10];
//        test = Ex2_1.createTextFiles(test.length,1,99999);
//        long startTime = System.currentTimeMillis();
//        int ch = Ex2_1.getNumOfLines(test);
//        long elapsedTime = System.currentTimeMillis() - startTime;
//        System.out.println("[NORMAL] Lines:"+ch+" Timer:"+elapsedTime+" ms");
//        startTime = System.currentTimeMillis();
//        int ch1 = ex2.getNumOfLinesThreads(test);
//        elapsedTime = System.currentTimeMillis() - startTime;
//        System.out.println("[Thread] Lines:"+ch1+" Timer:"+elapsedTime+" ms");
//        startTime = System.currentTimeMillis();
//        int ch2 = ex2.getNumOfLinesThreadPool(test);
//        elapsedTime = System.currentTimeMillis() - startTime;
//        System.out.println("[ThreadPool] Lines:"+ch2+" Timer:"+elapsedTime+" ms");
//        Ex2.Ex2_1.deleteFiles(test);
        Callable<Integer> c1 = () -> {
            return 1000*3;
        };
        Ex2_2.Task t1 = Ex2_2.Task.createTask(c1);
        CustomExecutor cs = new CustomExecutor();


    }

}