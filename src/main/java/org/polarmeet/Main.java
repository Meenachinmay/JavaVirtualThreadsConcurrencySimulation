package org.polarmeet;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

public class Main {
    // ANSI escape codes for cursor control and colors
    private static final String CLEAR_LINE = "\033[2K";
    private static final String MOVE_UP = "\033[1A";
    private static final String[] COLORS = {
            "\033[0;32m", // Green
            "\033[0;33m", // Yellow
            "\033[0;34m", // Blue
    };
    private static final String RESET_COLOR = "\033[0m";


    public static void main(String[] args) {
        // Create a thread factory
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().factory();

        // Create an executor service with virtual threads
        try ( var executor = Executors.newCachedThreadPool(virtualThreadFactory) ) {
           // Launch three concurrent tasks
           Future<?> task1 = executor.submit(() -> simulateTask("Service 1", 0));
            Future<?> task2 = executor.submit(() -> simulateTask("Service 2", 1));
            Future<?> task3 = executor.submit(() -> simulateTask("Service 3", 2));

            // Wait for all tasks to complete
            try {
                task1.get();
                task2.get();
                task3.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // Move cursor down and print completion message
        System.out.println("\nAll tasks completed successfully!");
    }

    private static void simulateTask(String taskName, int colorIndex) {
        Random random = new Random();
        int totalSteps = 20;

        // Synchronize console output to prevent garbled progress bars
        synchronized (System.out) {
            System.out.println(COLORS[colorIndex] + taskName + " starting..." + RESET_COLOR);
        }

        for (int i = 0; i <= totalSteps; i ++) {
            try {
                // Simulate varying workload
                Thread.sleep(random.nextInt(200,500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            // Calculate progress
           int percentage = ( i * 100) / totalSteps;
            int progressBarLength = ( i * 20) / totalSteps;

            // Build progress bar
            String progressBar = "[" + "=".repeat(progressBarLength) +
                    " ".repeat(20 - progressBarLength) + "]";

            // Synchronize terminal output
            synchronized (System.out) {
                // Move cursor up and clear line for smooth updates
                if (i > 0) {
                    System.out.print(MOVE_UP);
                    System.out.print(CLEAR_LINE);
                }

                // Print colored progress bar
                System.out.printf("%s%s %s %d%%%s%n",
                        COLORS[colorIndex], taskName, progressBar, percentage, RESET_COLOR);
            }
        }
    }
}



// As you can see that we are running 3 tasks concurrently with virtual thread. and here all the tasks are running smoothly.
// In Java when you use virtual thread, it does not make your code more speedy but it make your code more robust for large load.
// never think that virtual threads are for speed, no they are not. but they are for concurrency which mean distributed your load in a better
// to the CPU, so your app or server can perform better without breaking.
// Thats it for this lecture. I will see you in the next one.

// And i want to tell you that i have already uploaded my course on udemy which is all about building a highly scaleable distributed system using
// gRPC + Kotlin / Java + Redis and Spring web flux
// the code we are writing in that course is able to handle 100000 live real time gRPC streams from client.
// Hope you will like it.























