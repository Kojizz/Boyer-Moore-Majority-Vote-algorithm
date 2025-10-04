package cli;
import algorithms.BoyerMooreMajorityVote;
import metrics.PerformanceTracker;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;


public class BenchmarkRunner {

    private static final int[] DEFAULT_SIZES = {100, 1000, 10000, 100000};
    private static final int WARMUP_ITERATIONS = 5;
    private static final int BENCHMARK_ITERATIONS = 10;

    enum InputType {
        RANDOM_WITH_MAJORITY("Random array with majority element"),
        ALL_SAME("All elements identical (best case)"),
        NO_MAJORITY("No majority element exists"),
        NEARLY_MAJORITY("Element appears exactly n/2 times"),
        SORTED_WITH_MAJORITY("Sorted array with majority"),
        ALTERNATING("Alternating pattern (worst case for candidate changes)");

        private final String description;

        InputType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public static void main(String[] args) {
        BenchmarkRunner runner = new BenchmarkRunner();

        if (args.length > 0 && args[0].equals("--interactive")) {
            runner.runInteractiveMode();
        } else if (args.length > 0 && args[0].equals("--full")) {
            runner.runFullBenchmarkSuite();
        } else {
            runner.runQuickBenchmark();
        }
    }


    public void runQuickBenchmark() {
        System.out.println("=== Boyer-Moore Majority Vote - Quick Benchmark ===\n");

        for (int size : DEFAULT_SIZES) {
            System.out.println("Testing with n = " + size);
            benchmarkInputType(size, InputType.RANDOM_WITH_MAJORITY);
            System.out.println();
        }

        System.out.println("Quick benchmark complete!");
        System.out.println("Run with --full for comprehensive benchmarks");
        System.out.println("Run with --interactive for custom testing");
    }

    public void runFullBenchmarkSuite() {
        System.out.println("=== Boyer-Moore Majority Vote - Full Benchmark Suite ===\n");

        PerformanceTracker aggregateTracker = new PerformanceTracker();

        for (InputType type : InputType.values()) {
            System.out.println("\n>>> Testing: " + type.getDescription() + " <<<");

            for (int size : DEFAULT_SIZES) {
                System.out.println("\nArray size: " + size);

                long totalTime = 0;
                long totalComparisons = 0;
                long totalAccesses = 0;


                for (int i = 0; i < WARMUP_ITERATIONS; i++) {
                    int[] arr = generateInput(size, type);
                    BoyerMooreMajorityVote algo = new BoyerMooreMajorityVote();
                    algo.findMajorityElement(arr);
                }

                for (int i = 0; i < BENCHMARK_ITERATIONS; i++) {
                    int[] arr = generateInput(size, type);
                    BoyerMooreMajorityVote algo = new BoyerMooreMajorityVote();
                    algo.findMajorityElement(arr);

                    PerformanceTracker tracker = algo.getTracker();
                    totalTime += tracker.getExecutionTime();
                    totalComparisons += tracker.getComparisons();
                    totalAccesses += tracker.getArrayAccesses();
                }

                double avgTime = totalTime / (double) BENCHMARK_ITERATIONS;
                double avgComparisons = totalComparisons / (double) BENCHMARK_ITERATIONS;
                double avgAccesses = totalAccesses / (double) BENCHMARK_ITERATIONS;

                System.out.printf("  Avg Time: %.2f μs\n", avgTime / 1000.0);
                System.out.printf("  Avg Comparisons: %.0f\n", avgComparisons);
                System.out.printf("  Avg Array Accesses: %.0f\n", avgAccesses);
                System.out.printf("  Comparisons/n: %.2f\n", avgComparisons / size);
                System.out.printf("  Accesses/n: %.2f\n", avgAccesses / size);

                aggregateTracker.saveSnapshot(size);
            }
        }


        try {
            aggregateTracker.exportToCSV("docs/performance-plots/benchmark_results.csv");
            System.out.println("\n✓ Results exported to docs/performance-plots/benchmark_results.csv");
        } catch (IOException e) {
            System.err.println("Error exporting results: " + e.getMessage());
        }

        System.out.println("\nFull benchmark suite complete!");
    }


    public void runInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Boyer-Moore Majority Vote - Interactive Mode ===\n");

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Test with custom array");
            System.out.println("2. Benchmark custom size");
            System.out.println("3. Compare input distributions");
            System.out.println("4. Exit");
            System.out.print("\nChoice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    testCustomArray(scanner);
                    break;
                case 2:
                    benchmarkCustomSize(scanner);
                    break;
                case 3:
                    compareDistributions(scanner);
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private void testCustomArray(Scanner scanner) {
        System.out.print("Enter array size: ");
        int size = scanner.nextInt();
        scanner.nextLine();

        System.out.println("Input types:");
        InputType[] types = InputType.values();
        for (int i = 0; i < types.length; i++) {
            System.out.printf("%d. %s\n", i + 1, types[i].getDescription());
        }
        System.out.print("Choose type: ");
        int typeChoice = scanner.nextInt() - 1;
        scanner.nextLine();

        if (typeChoice < 0 || typeChoice >= types.length) {
            System.out.println("Invalid type!");
            return;
        }

        int[] arr = generateInput(size, types[typeChoice]);

        System.out.println("\nArray preview (first 20 elements):");
        for (int i = 0; i < Math.min(20, arr.length); i++) {
            System.out.print(arr[i] + " ");
        }
        if (arr.length > 20) System.out.print("...");
        System.out.println("\n");

        BoyerMooreMajorityVote algo = new BoyerMooreMajorityVote();
        var result = algo.findMajority(arr);

        if (result.isPresent()) {
            System.out.println("Result: " + result.get());
        } else {
            System.out.println("No majority element found");
        }

        algo.printStatistics();
    }

    private void benchmarkCustomSize(Scanner scanner) {
        System.out.print("Enter array size: ");
        int size = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\nBenchmarking size " + size + "...\n");

        for (InputType type : InputType.values()) {
            System.out.println(type.getDescription() + ":");
            benchmarkInputType(size, type);
            System.out.println();
        }
    }

    private void compareDistributions(Scanner scanner) {
        System.out.print("Enter array size: ");
        int size = scanner.nextInt();
        scanner.nextLine();

        System.out.println("\n=== Comparing Input Distributions ===\n");
        System.out.printf("%-40s %15s %15s %15s\n",
                "Distribution", "Time (μs)", "Comparisons", "Accesses");
        System.out.println("-".repeat(85));

        for (InputType type : InputType.values()) {
            int[] arr = generateInput(size, type);
            BoyerMooreMajorityVote algo = new BoyerMooreMajorityVote();
            algo.findMajorityElement(arr);

            PerformanceTracker tracker = algo.getTracker();
            System.out.printf("%-40s %15.2f %15d %15d\n",
                    type.getDescription(),
                    tracker.getExecutionTime() / 1000.0,
                    tracker.getComparisons(),
                    tracker.getArrayAccesses());
        }
    }

    private void benchmarkInputType(int size, InputType type) {
        int[] arr = generateInput(size, type);
        BoyerMooreMajorityVote algo = new BoyerMooreMajorityVote();

        var result = algo.findMajorityElement(arr);
        PerformanceTracker tracker = algo.getTracker();

        System.out.printf("  Input: %s\n", type.getDescription());
        System.out.printf("  Result: %s\n",
                result.isPresent() ? "Majority = " + result.get() : "No majority");
        System.out.printf("  Time: %.2f μs\n", tracker.getExecutionTimeMs() * 1000);
        System.out.printf("  Comparisons: %d (%.2f per element)\n",
                tracker.getComparisons(), tracker.getComparisons() / (double) size);
        System.out.printf("  Array Accesses: %d (%.2f per element)\n",
                tracker.getArrayAccesses(), tracker.getArrayAccesses() / (double) size);
    }


    private int[] generateInput(int size, InputType type) {
        Random rand = new Random(42);
        int[] arr = new int[size];

        switch (type) {
            case RANDOM_WITH_MAJORITY:
                int majorityElement = rand.nextInt(100);
                int majorityCount = size / 2 + 1;

                for (int i = 0; i < majorityCount; i++) {
                    arr[i] = majorityElement;
                }
                for (int i = majorityCount; i < size; i++) {
                    arr[i] = rand.nextInt(100);
                    if (arr[i] == majorityElement) arr[i] = (majorityElement + 1) % 100;
                }


                for (int i = size - 1; i > 0; i--) {
                    int j = rand.nextInt(i + 1);
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
                break;

            case ALL_SAME:
                int value = rand.nextInt(100);
                Arrays.fill(arr, value);
                break;

            case NO_MAJORITY:
                for (int i = 0; i < size; i++) {
                    arr[i] = i % Math.max(3, size / 3);
                }
                break;



            case NEARLY_MAJORITY:
                int nearlyMajor = rand.nextInt(100);
                for (int i = 0; i < size / 2; i++) {
                    arr[i] = nearlyMajor;
                }
                for (int i = size / 2; i < size; i++) {
                    arr[i] = rand.nextInt(100);
                    if (arr[i] == nearlyMajor) arr[i] = (nearlyMajor + 1) % 100;
                }
                break;


            case SORTED_WITH_MAJORITY:
                int sortedMajor = rand.nextInt(100);
                for (int i = 0; i < size / 2 + 1; i++) {
                    arr[i] = sortedMajor;
                }
                for (int i = size / 2 + 1; i < size; i++) {
                    arr[i] = i;
                }
                break;

            case ALTERNATING:
                int elem1 = 1;
                int elem2 = 2;
                for (int i = 0; i < size / 2 + 1; i++) {
                    arr[i * 2 % size] = elem1;
                }
                for (int i = 0; i < size / 2; i++) {
                    arr[(i * 2 + 1) % size] = elem2;
                }
                break;
        }
        return arr;

    }

}