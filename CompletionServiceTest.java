package com.by.practice.mdap;

/*
As you can see the total execution time between Normal Executor Service and Completion Service is the same, while the normal loop was more than 3 times slower. This is easily explained by the fact that the normal loop was single threaded and the others 2 were run with 3 threads.

The difference between  Normal Executor Service and Completion Service is visible at runtime, Normal Executor Service always waits  for the next thread to finish the job before printing the number, the client needs to wait until a big bunch of threads return all together.
Completion Service is behaving differently, it follows a producer/consumer philosophy: as soon a thread is done, it puts the result into a non blocking queue so that the consumer can take it.

I hope this simple example will help you to better clarify the completion service as it did for me.*/
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
public class CompletionServiceTest {

        private static final int waittime = 200;
        private static final int numberOfThreadsInThePool = 3;

        private final List<String> printRequests = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                        "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
                        );

        void normalLoop() {
                for (String image : printRequests) {
                        try {
                                Thread.sleep(waittime);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        System.out.print(image);
                }
        }

        void normalExecutorService() {
                ExecutorService executor = Executors.newFixedThreadPool(numberOfThreadsInThePool);
                try {
                        Set<Future<String>> printTaskFutures = new HashSet<Future<String>>();
                        for (final String printRequest : printRequests) {
                                printTaskFutures.add(executor.submit(new Printer(printRequest)));
                        }
                        for (Future<String> future : printTaskFutures) {
                                System.out.print(future.get());

                        }
                } catch (Exception e) {
                        Thread.currentThread().interrupt();
                } finally {
                        if (executor != null) {
                                executor.shutdownNow();
                        }
                }
        }

        void completionService() {
                ExecutorService executor = Executors.newFixedThreadPool(numberOfThreadsInThePool);
                CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);
                for (final String printRequest : printRequests) {
                        completionService.submit(new Printer(printRequest));
                }
                try {
                        for (int t = 0, n = printRequests.size(); t < n; t++) {
                                Future<String> f = completionService.take();
                                System.out.print(f.get());
                        }
                } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                        Thread.currentThread().interrupt();
                } finally {
                        if (executor != null) {
                                executor.shutdownNow();
                        }
                }

        }

        private class Printer implements Callable<String> {

                private final String toPrint;

                public Printer(String toPrint) {
                        this.toPrint = toPrint;
                }

                public String call() {
                        try {
                                Thread.sleep(waittime);
                        } catch (InterruptedException e) {
                                e.printStackTrace();
                        }
                        return toPrint;
                }
        }

        public static void main(String[] args) {
                System.out.println("Normal Executor Service");
                long start = System.currentTimeMillis();
                new CompletionServiceTest().normalExecutorService();
                System.out.println();
                System.out.println("Execution time : " + (System.currentTimeMillis() - start));

                System.out.println("Completion Service");
                start = System.currentTimeMillis();
                new CompletionServiceTest().completionService();
                System.out.println();
                System.out.println("Execution time : " + (System.currentTimeMillis() - start));

                System.out.println("Normal Loop");
                start = System.currentTimeMillis();
                new CompletionServiceTest().normalLoop();
                System.out.println();
                System.out.println("Execution time : " + (System.currentTimeMillis() - start));

        }
}


/*
 * The output :
 * 
 * 
 * Normal Executor Service
 * 4865447106310711038976137104163378132553196955489824610105761329674254896133105110752911167871022184410147885282715959248479622229673310496981056331010958
 * Execution time : 9410 Completion Service
 * 1234567891012345678910123456789101234567891012345678109123456789101234567891012345678910123456789101235647891012345678910123456789101234568791012435687109
 * Execution time : 9405 Normal Loop
 * 1234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910123456789101234567891012345678910
 * Execution time : 28013
 */