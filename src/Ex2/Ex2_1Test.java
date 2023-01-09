package Ex2;

import Ex2.Ex2_2.CustomExecutor;
import Ex2.Ex2_2.Task;
import Ex2.Ex2_2.TaskType;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import java.util.concurrent.*;

import java.util.concurrent.Callable;

class Ex2_1Test {

        public static final Logger logger = LoggerFactory.getLogger(Ex2_1Test.class);
        @Test
        public void partialTest(){
            CustomExecutor customExecutor = new CustomExecutor();
            var task = Task.createTask(()->{
                int sum = 0;
                for (int i = 1; i <= 10; i++) {
                    sum += i;
                }
                return sum;
            }, TaskType.COMPUTATIONAL);
            var sumTask = customExecutor.submit(task);
            final int sum;
            try {
                sum = (int) sumTask.get(1, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new RuntimeException(e);
            }
            logger.info(()-> "Sum of 1 through 10 = " + sum);
            Callable<Double> callable1 = ()-> {
                return 1000 * Math.pow(1.02, 5);
            };
            Callable<String> callable2 = ()-> {
                StringBuilder sb = new StringBuilder("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                return sb.reverse().toString();
            };
            // var is used to infer the declared type automatically
            var priceTask = customExecutor.submit(()-> {
                return 1000 * Math.pow(1.02, 5);
            }, TaskType.COMPUTATIONAL);
            var reverseTask = customExecutor.submit(callable2, TaskType.IO);
            final Double totalPrice;
            final String reversed;
            try {
                totalPrice = (Double) priceTask.get();
                reversed = (String) reverseTask.get();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
            logger.info(()-> "Reversed String = " + reversed);
            logger.info(()->String.valueOf("Total Price = " + totalPrice));
            logger.info(()-> "Current maximum priority = " +
                    customExecutor.getCurrentMax());
            customExecutor.gracefullyTerminate();

        }
    @Test
    public void test() {
//        Ex2.Ex2_1 ex2 = new Ex2.Ex2_1();
//        String[] test =new String[10];
//        test = Ex2.Ex2_1.createTextFiles(test.length,1,99999);
//        long startTime = System.currentTimeMillis();
//        int ch = Ex2.Ex2_1.getNumOfLines(test);
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
//        Ex2.Ex2_1.Ex2.Ex2_1.deleteFiles(test);
        Callable<Integer> c1 = () -> {
            return 1000*3;
        };

        Task task = Task.createTask(c1);
        CustomExecutor ce = new CustomExecutor();
        ce.submit(task);


    }
    }


