package org.example;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {
    static int x = 0;

    public static void main(String[] args) {

        var startTime = System.currentTimeMillis();

//        try(var executor = Executors.newCachedThreadPool()) {
//        try(var executor = Executors.newWorkStealingPool()) {
        try(var executor = Executors.newVirtualThreadPerTaskExecutor()) {
//        try(var executor = Executors.newSingleThreadScheduledExecutor()) {
            IntStream.range(0, 100).forEach(i -> {
                executor.submit(() -> {
                    new Job("thread " + i).run();
                    return i;
                });
            });
        }
        System.out.println((System.currentTimeMillis() - startTime)/1000 + " seconds");
    }

    private static class Job implements Runnable {

        private String a;
        private int x = 0;

        public Job(String a) {
            this.a = a;
        }

        @Override
        public void run() {
            Random r = new Random();
            while (x<1000) {
                x++;
                for(int i = 0; i<10000;i++) {
                    r.nextInt();
                }
//                System.out.println(a + ": "+x);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(a + " ended");

        }
    }
}