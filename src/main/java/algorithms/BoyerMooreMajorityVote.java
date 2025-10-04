package algorithms;

import metrics.PerformanceTracker;
import java.util.Optional;


public class BoyerMooreMajorityVote {

    private final PerformanceTracker tracker;


    public static class MajorityResult {
        private final int element;
        private final int count;
        private final int firstPosition;
        private final int lastPosition;

        public MajorityResult(int element, int count, int firstPos, int lastPos) {
            this.element = element;
            this.count = count;
            this.firstPosition = firstPos;
            this.lastPosition = lastPos;
        }

        public int getElement() { return element; }
        public int getCount() { return count; }
        public int getFirstPosition() { return firstPosition; }
        public int getLastPosition() { return lastPosition; }

        @Override
        public String toString() {
            return String.format("Majority: %d (count=%d, first@%d, last@%d)",
                    element, count, firstPosition, lastPosition);
        }
    }

    public BoyerMooreMajorityVote() {
        this.tracker = new PerformanceTracker();
    }

    public BoyerMooreMajorityVote(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    public Optional<MajorityResult> findMajority(int[] arr) {
        // Input validation
        if (arr == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }

        tracker.reset();
        tracker.startTimer();

        if (arr.length == 0) {
            tracker.stopTimer();
            return Optional.empty();
        }


        if (arr.length == 1) {
            tracker.incrementComparisons(0);
            tracker.incrementArrayAccesses(1);
            tracker.stopTimer();
            return Optional.of(new MajorityResult(arr[0], 1, 0, 0));
        }


        int candidate = findCandidate(arr);
        Optional<MajorityResult> result = verifyAndTrackCandidate(arr, candidate);

        tracker.stopTimer();
        return result;
    }


    private int findCandidate(int[] arr) {
        int candidate = arr[0];
        int count = 1;
        tracker.incrementArrayAccesses(1);

        for (int i = 1; i < arr.length; i++) {
            tracker.incrementArrayAccesses(1);

            if (count == 0) {
                candidate = arr[i];
                count = 1;
                tracker.incrementComparisons(1);
            } else {
                tracker.incrementComparisons(2);

                if (arr[i] == candidate) {
                    count++;
                } else {
                    count--;
                }
            }
        }

        return candidate;
    }


    private Optional<MajorityResult> verifyAndTrackCandidate(int[] arr, int candidate) {
        int count = 0;
        int firstPos = -1;
        int lastPos = -1;

        for (int i = 0; i < arr.length; i++) {
            tracker.incrementArrayAccesses(1);
            tracker.incrementComparisons(1);

            if (arr[i] == candidate) {
                count++;
                if (firstPos == -1) {
                    firstPos = i;
                }
                lastPos = i;
            }
        }

        tracker.incrementComparisons(1); // count > arr.length / 2

        if (count > arr.length / 2) {
            return Optional.of(new MajorityResult(candidate, count, firstPos, lastPos));
        }

        return Optional.empty();
    }

    public Optional<Integer> findMajorityElement(int[] arr) {
        Optional<MajorityResult> result = findMajority(arr);
        return result.map(MajorityResult::getElement);
    }


    public PerformanceTracker getTracker() {
        return tracker;
    }

    public void printStatistics() {
        System.out.println("\n=== Boyer-Moore Majority Vote Statistics ===");
        System.out.println("Execution Time: " + tracker.getExecutionTime() + " ns");
        System.out.println("Array Accesses: " + tracker.getArrayAccesses());
        System.out.println("Comparisons: " + tracker.getComparisons());
        System.out.println("Theoretical Complexity: Θ(n)");
        System.out.println("Space Complexity: Θ(1)");
    }
}