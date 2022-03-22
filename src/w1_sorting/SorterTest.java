package w1_sorting;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.SplittableRandom;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author evensen
 *
 */
public abstract class SorterTest {
	private SplittableRandom rng;
	private final IntSorter sorter;
	private final int minSize;
	private final int maxSize;

	public SorterTest(IntSorter sorter, int minSize, int maxSize) {
		this.sorter = sorter;
		this.minSize = minSize;
		this.maxSize = maxSize;
	}

	@BeforeEach
	public void setup() {
		this.rng = new SplittableRandom(0);
	}

	@Test
	public void testForwardRangeSort() {
		for (int i = this.minSize; i < this.maxSize; i++) {
			int[] forwardRange = TestUtils.getRange(0, i);
			int[] expected = forwardRange.clone();
			this.sorter.sort(forwardRange);
			assertArrayEquals(expected, forwardRange);
		}
	}

	@Test
	public void testReverseRangeSort() {
		for (int i = this.minSize; i < this.maxSize; i++) {
			int[] reversedRange = TestUtils.getReversedRange(0, i);
			int[] expected = TestUtils.reverse(reversedRange);
			this.sorter.sort(reversedRange);
			assertArrayEquals(expected, reversedRange);
		}
	}

	@Test
	public void testRandomArraySortWithReference1() {
		for (int i = this.minSize; i < this.maxSize; i++) {
			// Let our pseudo random array be constructed such that it is
			// certain that there is at least one duplicate element (assuming the length >
			// 1).
			int[] arr = TestUtils.getRandomArray(0, i / 2 + 1, i, this.rng);
			// Assume that we have a reference method that we know is correct,
			// in this case Arrays.sort().
			int[] expected = arr.clone();
			Arrays.sort(expected);

			// Now sort our random array and compare.
			this.sorter.sort(arr);
			assertArrayEquals(expected, arr);
		}
	}

	@Test
	public void testRandomArraySortWithReference2() {
		for (int i = this.minSize; i < this.maxSize; i++) {
			// Let our pseudo random array be constructed such that it is
			// highly likely that there are no duplicate elements.
			int[] arr = TestUtils.getRandomArray(0, i * 1000 + 1, i, this.rng);
			// Assume that we have a reference method that we know is correct,
			// in this case Arrays.sort().
			int[] expected = arr.clone();
			Arrays.sort(expected);

			// Now sort our random array and compare.
			this.sorter.sort(arr);
			assertArrayEquals(expected, arr);
		}
	}

	// The test below makes no assumptions about a reference method that we can use.
	// By being slightly more clever with how we construct our input we can still
	// make it into a fairly strict test.
	@Test
	public void testRandomArraySortNoReference() {
		// By using a divisor we make sure that we test both arrays with all
		// elements unique and arrays that are guaranteed to contain duplicates
		// (assuming length > 1).
		for (int divisor = 1; divisor <= 2; divisor++) {
			for (int i = this.minSize; i < this.maxSize; i++) {
				int[] arr = TestUtils.getRange(0, i);
				TestUtils.divideAll(arr, divisor);
				int[] expected = arr.clone();

				// We now shuffle our original array pseudo-randomly...
				TestUtils.shuffle(arr, this.rng);

				// Now sort our shuffled array and compare.
				this.sorter.sort(arr);
				assertArrayEquals(expected, arr);
			}
		}
	}

}
