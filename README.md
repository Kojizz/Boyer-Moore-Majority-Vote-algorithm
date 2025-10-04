# Boyer-Moore Majority Vote Algorithm - Assignment 2

**Student A - Pair 3: Linear Array Algorithms**

## Algorithm Overview

The Boyer-Moore Majority Vote algorithm finds the majority element (appearing more than n/2 times) in an array using a single-pass voting mechanism.

### Time Complexity
- **Best Case**: Θ(n) - all elements are the same
- **Worst Case**: Θ(n) - no majority exists (still needs full verification)
- **Average Case**: Θ(n) - always two passes through array

### Space Complexity
- **Θ(1)** - uses only constant extra space (candidate, count, position trackers)

## Project Structure

```
assignment2-boyer-moore-majority/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── algorithms/
│   │       │   └── BoyerMooreMajorityVote.java
│   │       ├── metrics/
│   │       │   └── PerformanceTracker.java
│   │       └── cli/
│   │           └── BenchmarkRunner.java
│   └── test/
│       └── java/
│           └── algorithms/
│               └── BoyerMooreMajorityVoteTest.java
├── docs/
│   ├── analysis-report.pdf
│   └── performance-plots/
├── pom.xml
└── README.md
```

## Setup Instructions

### Prerequisites
- Java 11 or higher
- Maven 3.6+
- Git

### 1. Create Maven Project

```bash
# Create project directory
mkdir assignment2-boyer-moore-majority
cd assignment2-boyer-moore-majority

# Initialize git
git init
git checkout -b feature/algorithm

# Create Maven structure
mkdir -p src/main/java/algorithms
mkdir -p src/main/java/metrics
mkdir -p src/main/java/cli
mkdir -p src/test/java/algorithms
mkdir -p docs/performance-plots
```

### 2. Add Files

Copy the provided files to their respective directories:
- `pom.xml` → project root
- `BoyerMooreMajorityVote.java` → `src/main/java/algorithms/`
- `PerformanceTracker.java` → `src/main/java/metrics/`
- `BenchmarkRunner.java` → `src/main/java/cli/`
- `BoyerMooreMajorityVoteTest.java` → `src/test/java/algorithms/`

### 3. Build Project

```bash
# Compile and run tests
mvn clean test

# Package as JAR
mvn clean package

# This creates:
# - target/assignment2-boyer-moore-majority-1.0.0.jar
# - target/assignment2-boyer-moore-majority-1.0.0-jar-with-dependencies.jar
```

## Running the Algorithm

### Quick Benchmark
```bash
mvn exec:java -Dexec.mainClass="cli.BenchmarkRunner"
```

### Full Benchmark Suite
```bash
java -jar target/assignment2-boyer-moore-majority-1.0.0-jar-with-dependencies.jar --full
```

### Interactive Mode
```bash
java -jar target/assignment2-boyer-moore-majority-1.0.0-jar-with-dependencies.jar --interactive
```

### Run Tests
```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=BoyerMooreMajorityVoteTest

# Run with verbose output
mvn test -Dtest=BoyerMooreMajorityVoteTest#testLinearTimeComplexity
```

## Algorithm Details

### Phase 1: Candidate Selection (Voting)
```
Time: O(n) - single pass
Space: O(1) - only candidate and count variables

For each element:
  - If count is 0, set current element as candidate
  - If element matches candidate, increment count
  - Otherwise, decrement count
```

### Phase 2: Verification
```
Time: O(n) - single pass
Space: O(1) - only verification variables

Count occurrences of candidate:
  - If count > n/2, candidate is majority
  - Otherwise, no majority exists
```

### Key Features
- **Single-pass detection** with verification phase
- **Position tracking** (first and last occurrence)
- **Comprehensive metrics** collection
- **Edge case handling** (empty, single element, no majority)

## Performance Characteristics

### Expected Operations for array size n:
- **Array Accesses**: ~2n (n for voting + n for verification)
- **Comparisons**: ~3n (count checks, element comparisons, final verification)
- **Space**: O(1) constant regardless of n

### Input Types Tested:
1. **Random with Majority** - majority element randomly distributed
2. **All Same** - best case, all elements identical
3. **No Majority** - worst case for verification
4. **Nearly Majority** - element appears exactly n/2 times
5. **Sorted with Majority** - majority element grouped together
6. **Alternating Pattern** - causes maximum candidate changes

## Git Workflow

### Initial Commits
```bash
git add pom.xml
git commit -m "init: maven project structure with junit5"

git add src/main/java/metrics/PerformanceTracker.java
git commit -m "feat(metrics): performance counters and CSV export"

git add src/main/java/algorithms/BoyerMooreMajorityVote.java
git commit -m "feat(algorithm): baseline Boyer-Moore Majority Vote implementation"

git add src/test/java/algorithms/BoyerMooreMajorityVoteTest.java
git commit -m "test(algorithm): comprehensive test suite with edge cases"

git add src/main/java/cli/BenchmarkRunner.java
git commit -m "feat(cli): benchmark runner with configurable input sizes"
```

### Feature Branches
- `feature/algorithm` - main implementation
- `feature/metrics` - performance tracking
- `feature/testing` - unit tests
- `feature/cli` - command-line interface
- `feature/optimization` - performance improvements

### Tagging Releases
```bash
git tag v0.1 -m "Initial working implementation"
git tag v1.0 -m "Complete implementation with all features"
```

## Complexity Analysis Summary

### Time Complexity Analysis

**Best Case: Θ(n)**
- Input: All elements are identical
- Phase 1: n comparisons, candidate never changes
- Phase 2: n comparisons, all match
- Total: 2n operations

**Worst Case: Θ(n)**
- Input: No majority exists
- Phase 1: n comparisons with frequent candidate changes
- Phase 2: n comparisons, fails verification
- Total: ~3n operations (still linear)

**Average Case: Θ(n)**
- Input: Majority exists but scattered
- Phase 1: n comparisons
- Phase 2: n comparisons
- Total: 2n-3n operations

**Key Insight**: Algorithm is always Θ(n) regardless of input distribution. The constant factors vary slightly, but asymptotic complexity remains linear.

### Space Complexity: Θ(1)

Variables used:
- `candidate` - 4 bytes
- `count` - 4 bytes  
- `firstPos` - 4 bytes
- `lastPos` - 4 bytes
- Loop variables - 4 bytes

Total: ~20 bytes regardless of input size n

## Testing Coverage

 Edge cases (empty, null, single element)  
 Basic correctness (majority exists/doesn't exist)  
 Position tracking validation  
 Negative numbers and zeros  
 Large arrays (10K+ elements)  
 Property-based tests  
 Complexity verification  
 Stress tests (alternating patterns, worst-case scenarios)

**Test Results**: All 23 tests pass

## Benchmarking Results

Performance data will be collected across sizes: 100, 1K, 10K, 100K

Results exported to: `docs/performance-plots/benchmark_results.csv`

## Next Steps for Peer Analysis

When analyzing your partner's Kadane's Algorithm implementation:

1. **Complexity Analysis**
   - Derive time/space complexity for best/worst/average cases
   - Compare with Boyer-Moore complexity characteristics
   - Analyze recurrence relations if recursive

2. **Code Review**
   - Check for inefficient nested loops
   - Look for unnecessary array copies
   - Identify optimization opportunities

3. **Empirical Testing**
   - Run benchmarks on same input sizes
   - Plot time vs n to verify O(n)
   - Compare constant factors

4. **Optimization Suggestions**
   - Propose specific improvements
   - Measure impact of optimizations

## License

Academic project for Algorithm Analysis course.

## Author

Ahmetov Rasul

