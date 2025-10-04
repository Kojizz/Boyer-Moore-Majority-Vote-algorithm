package metrics;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


public class PerformanceTracker {

    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long memoryAllocations;
    private long startTime;
    private long endTime;
    private boolean timerRunning;


    private final List<MetricsSnapshot> snapshots;

    public PerformanceTracker() {
        this.snapshots = new ArrayList<>();
        reset();
    }


    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        memoryAllocations = 0;
        startTime = 0;
        endTime = 0;
        timerRunning = false;
    }


    public void startTimer() {
        startTime = System.nanoTime();
        timerRunning = true;
    }

    public void stopTimer() {
        if (timerRunning) {
            endTime = System.nanoTime();
            timerRunning = false;
        }
    }


    public long getExecutionTime() {
        if (timerRunning) {
            return System.nanoTime() - startTime;
        }
        return endTime - startTime;
    }


    public double getExecutionTimeMs() {
        return getExecutionTime() / 1_000_000.0;
    }


    public void incrementComparisons() {
        comparisons++;
    }
    public void incrementComparisons(long count) {
        comparisons += count;
    }

    public void incrementSwaps() {
        swaps++;
    }

    public void incrementSwaps(long count) {
        swaps += count;
    }

    public void incrementArrayAccesses() {
        arrayAccesses++;
    }
    public void incrementArrayAccesses(long count) {
        arrayAccesses += count;
    }

    public void incrementMemoryAllocations() {
        memoryAllocations++;
    }

    public void incrementMemoryAllocations(long count) {
        memoryAllocations += count;
    }
    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public long getArrayAccesses() {
        return arrayAccesses;
    }

    public long getMemoryAllocations() {
        return memoryAllocations;
    }


    public void saveSnapshot(int arraySize) {
        snapshots.add(new MetricsSnapshot(
                arraySize,
                comparisons,
                swaps,
                arrayAccesses,
                memoryAllocations,
                getExecutionTime()
        ));
    }

    public void exportToCSV(String filename) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("ArraySize,Comparisons,Swaps,ArrayAccesses,MemoryAllocations,ExecutionTime(ns)");
            for (MetricsSnapshot snapshot : snapshots) {
                writer.printf("%d,%d,%d,%d,%d,%d%n",
                        snapshot.arraySize,
                        snapshot.comparisons,
                        snapshot.swaps,
                        snapshot.arrayAccesses,
                        snapshot.memoryAllocations,
                        snapshot.executionTime
                );
            }
        }
    }

    public List<MetricsSnapshot> getSnapshots() {
        return new ArrayList<>(snapshots);
    }


    public void clearSnapshots() {
        snapshots.clear();
    }


    public void printMetrics() {
        System.out.println("\n=== Performance Metrics ===");
        System.out.println("Execution Time: " + getExecutionTimeMs() + " ms");
        System.out.println("Comparisons: " + comparisons);
        System.out.println("Swaps: " + swaps);
        System.out.println("Array Accesses: " + arrayAccesses);
        System.out.println("Memory Allocations: " + memoryAllocations);
    }

    public static class MetricsSnapshot {
        public final int arraySize;
        public final long comparisons;
        public final long swaps;
        public final long arrayAccesses;
        public final long memoryAllocations;
        public final long executionTime;

        public MetricsSnapshot(int arraySize, long comparisons, long swaps,
                               long arrayAccesses, long memoryAllocations, long executionTime) {
            this.arraySize = arraySize;
            this.comparisons = comparisons;
            this.swaps = swaps;
            this.arrayAccesses = arrayAccesses;
            this.memoryAllocations = memoryAllocations;
            this.executionTime = executionTime;
        }

        @Override
        public String toString() {
            return String.format("n=%d: comparisons=%d, swaps=%d, accesses=%d, time=%dns",
                    arraySize, comparisons, swaps, arrayAccesses, executionTime);
        }
    }
}