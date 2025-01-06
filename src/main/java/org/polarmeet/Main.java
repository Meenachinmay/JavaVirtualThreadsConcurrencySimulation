package org.polarmeet;

import java.util.concurrent.*;
import java.util.Random;

public class Main {
    // ANSI escape codes for cursor control and colors
    private static final String CLEAR_LINE = "\033[2K";
    private static final String MOVE_UP = "\033[1A";
    private static final String[] COLORS = {
            "\033[0;32m", // Green
            "\033[0;33m", // Yellow
            "\033[0;34m"  // Blue
    };
    private static final String RESET_COLOR = "\033[0m";

    public static void main(String[] args) {
        // Create a thread factory for virtual threads
        ThreadFactory virtualThreadFactory = Thread.ofVirtual().factory();

        // Create an executor service with virtual threads
        try (var executor = Executors.newThreadPerTaskExecutor(virtualThreadFactory)) {
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

        for (int i = 0; i <= totalSteps; i++) {
            try {
                // Simulate varying workload
                Thread.sleep(random.nextInt(200, 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }

            // Calculate progress
            int percentage = (i * 100) / totalSteps;
            int progressBarLength = (i * 20) / totalSteps;

            // Build progress bar string
            String progressBar = "[" + "=".repeat(progressBarLength) +
                    " ".repeat(20 - progressBarLength) + "]";

            // Synchronize console output
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