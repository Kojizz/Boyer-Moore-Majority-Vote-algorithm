package algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import java.util.Random;

class BoyerMooreMajorityVoteTest {

    private BoyerMooreMajorityVote algorithm;

    @BeforeEach
    void setUp() {
        algorithm = new BoyerMooreMajorityVote();
    }

    @Test
    @DisplayName("Empty array should return empty Optional")
    void testEmptyArray() {
        int[] arr = {};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Null array should throw IllegalArgumentException")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> {
            algorithm.findMajorityElement(null);
        });
    }

    @Test
    @DisplayName("Single element array should return that element")
    void testSingleElement() {
        int[] arr = {42};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(42, result.get());
    }

    @Test
    @DisplayName("Two identical elements should return majority")
    void testTwoIdenticalElements() {
        int[] arr = {5, 5};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(5, result.get());
    }

    @Test
    @DisplayName("Two different elements should return empty (no majority)")
    void testTwoDifferentElements() {
        int[] arr = {3, 7};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Simple majority case - all same elements")
    void testAllSameElements() {
        int[] arr = {1, 1, 1, 1, 1};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(1, result.get());
    }

    @Test
    @DisplayName("Majority element at the beginning")
    void testMajorityAtBeginning() {
        int[] arr = {4, 4, 4, 2, 1};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(4, result.get());
    }

    @Test
    @DisplayName("Majority element at the end")
    void testMajorityAtEnd() {
        int[] arr = {1, 2, 3, 3, 3};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(3, result.get());
    }

    @Test
    @DisplayName("Majority element scattered throughout")
    void testMajorityScattered() {
        int[] arr = {2, 1, 2, 3, 2, 4, 2};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(2, result.get());
    }

    @Test
    @DisplayName("No majority element exists")
    void testNoMajority() {
        int[] arr = {1, 2, 3, 4, 5};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Element appears exactly n/2 times (not majority)")
    void testExactlyHalf() {
        int[] arr = {1, 1, 2, 2};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Element appears n/2 + 1 times (is majority)")
    void testJustOverHalf() {
        int[] arr = {1, 1, 1, 2, 2};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(1, result.get());
    }

    @Test
    @DisplayName("Array with negative numbers")
    void testNegativeNumbers() {
        int[] arr = {-3, -3, -3, 1, 2};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(-3, result.get());
    }

    @Test
    @DisplayName("Array with zeros")
    void testWithZeros() {
        int[] arr = {0, 0, 0, 1, 2};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(0, result.get());
    }

    @Test
    @DisplayName("Mixed positive and negative with majority")
    void testMixedNumbers() {
        int[] arr = {-1, 2, -1, 3, -1, 4, -1};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(-1, result.get());
    }

    @Test
    @DisplayName("Position tracking - first and last positions correct")
    void testPositionTracking() {
        int[] arr = {1, 2, 3, 3, 3, 2, 3};
        Optional<BoyerMooreMajorityVote.MajorityResult> result = algorithm.findMajority(arr);
        assertTrue(result.isPresent());
        assertEquals(3, result.get().getElement());
        assertEquals(2, result.get().getFirstPosition());
        assertEquals(6, result.get().getLastPosition());
        assertEquals(4, result.get().getCount());
    }

    @Test
    @DisplayName("Position tracking - all elements same")
    void testPositionTrackingAllSame() {
        int[] arr = {7, 7, 7, 7};
        Optional<BoyerMooreMajorityVote.MajorityResult> result = algorithm.findMajority(arr);
        assertTrue(result.isPresent());
        assertEquals(0, result.get().getFirstPosition());
        assertEquals(3, result.get().getLastPosition());
        assertEquals(4, result.get().getCount());
    }

    @Test
    @DisplayName("Large array with clear majority")
    void testLargeArrayWithMajority() {
        int[] arr = new int[10001];
        for (int i = 0; i < 5001; i++) {
            arr[i] = 1;
        }
        for (int i = 5001; i < 10001; i++) {
            arr[i] = i % 100;
        }

        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(1, result.get());
    }

    @Test
    @DisplayName("Large array without majority")
    void testLargeArrayWithoutMajority() {
        int[] arr = new int[10000];
        for (int i = 0; i < 10000; i++) {
            arr[i] = i % 100;
        }

        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Property: If all elements same, that element is majority")
    void testPropertyAllSame() {
        Random rand = new Random(42);
        for (int size = 1; size <= 100; size++) {
            int value = rand.nextInt(1000);
            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                arr[i] = value;
            }

            Optional<Integer> result = algorithm.findMajorityElement(arr);
            assertTrue(result.isPresent(), "Failed for size " + size);
            assertEquals(value, result.get(), "Failed for size " + size);
        }
    }

    @Test
    @DisplayName("Property: Majority element count > n/2")
    void testPropertyMajorityDefinition() {
        int[] arr = {1, 1, 1, 2, 2};
        Optional<BoyerMooreMajorityVote.MajorityResult> result = algorithm.findMajority(arr);
        assertTrue(result.isPresent());
        assertTrue(result.get().getCount() > arr.length / 2);
    }

    @Test
    @DisplayName("Verify linear time complexity O(n)")
    void testLinearTimeComplexity() {
        int[] sizes = {100, 200, 400, 800};
        long[] operations = new long[sizes.length];

        for (int i = 0; i < sizes.length; i++) {
            int[] arr = new int[sizes[i]];
            for (int j = 0; j < sizes[i]; j++) {
                arr[j] = (j < sizes[i] / 2 + 1) ? 1 : j;
            }

            BoyerMooreMajorityVote algo = new BoyerMooreMajorityVote();
            algo.findMajorityElement(arr);
            operations[i] = algo.getTracker().getComparisons() +
                    algo.getTracker().getArrayAccesses();
        }

        for (int i = 0; i < sizes.length - 1; i++) {
            double opRatio = (double) operations[i + 1] / operations[i];
            double sizeRatio = (double) sizes[i + 1] / sizes[i];
            assertTrue(Math.abs(opRatio - sizeRatio) < 0.5,
                    "Operations not scaling linearly at size " + sizes[i]);
        }
    }

    @Test
    @DisplayName("Verify constant space complexity O(1)")
    void testConstantSpaceComplexity() {
        BoyerMooreMajorityVote algo = new BoyerMooreMajorityVote();

        int[] smallArray = new int[100];
        int[] largeArray = new int[10000];

        for (int i = 0; i < 100; i++) smallArray[i] = 1;
        for (int i = 0; i < 10000; i++) largeArray[i] = 1;

        algo.findMajorityElement(smallArray);
        long smallMemory = algo.getTracker().getMemoryAllocations();

        algo.findMajorityElement(largeArray);
        long largeMemory = algo.getTracker().getMemoryAllocations();

        assertEquals(smallMemory, largeMemory, "Space complexity is not constant");
    }

    @Test
    @DisplayName("Stress test - alternating pattern")
    void testAlternatingPattern() {
        int[] arr = new int[1001];
        for (int i = 0; i < 1001; i++) {
            arr[i] = (i < 501) ? 1 : (i % 2);
        }

        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(1, result.get());
    }

    @Test
    @DisplayName("Stress test - worst case candidate changes")
    void testWorstCaseCandidateChanges() {
        int[] arr = {1, 2, 1, 2, 1, 2, 1};
        Optional<Integer> result = algorithm.findMajorityElement(arr);
        assertTrue(result.isPresent());
        assertEquals(1, result.get());
    }
}