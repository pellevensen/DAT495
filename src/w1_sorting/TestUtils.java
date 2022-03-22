package w1_sorting;

import java.util.SplittableRandom;

/**
 *
 * @author evensen
 *
 */
public enum TestUtils {
	;

	/**
	 * Returns an array with the elements {@code {lower, lower + 1, ..., upper - 1}} in ascending order.
	 *
	 * E.g. getRange(0, 2) gives the array {0, 1} as output.
	 * @pre {@code lower <= upper}
	 * @param lower The value of the lowest element.
	 * @param upper The value of the greatest element + 1.
	 */
	public static int[] getRange(int lower, int upper) {
		int[] arr = new int[upper - lower];
		for(int i = lower; i < upper; i++) {
			arr[i - lower] = i;
		}
		return arr;
	}

	/**
	 * Returns an array with the elements {upper - 1, upper - 2, ..., lower} in descending order.
	 *
	 * E.g. getRange(0, 2) gives the array {1, 0} as output.
	 * @pre {@code lower <= upper}
	 * @param lower The value of the lowest element.
	 * @param upper The value of the greatest element + 1.
	 */
	public static int[] getReversedRange(int lower, int upper) {
		return reverse(getRange(lower, upper));
	}

	/**
	 * Returns a reversed version of the input. The first element becomes the last element and so on.
	 * @pre {@code arr != null}
	 * @post {@code arr} will not be mutated.
	 * @param arr The array to create a reversed copy from.
	 * @return A reversed copy of {@code arr}.
	 */
	public static int[] reverse(int[] arr) {
		int[] reversed = new int[arr.length];
		for(int i = 0; i < arr.length; i++) {
			reversed[i] = arr[arr.length - i - 1];
		}
		return reversed;
	}

	/**
	 * Returns a pseudo-random array of length {@code size}.
	 * @pre {@code lower <= upper}
	 * @pre {@code size >= 0}
	 * @pre {@code rng != null}
	 * @param lower The lowest possible output
	 * @param upper The upper bound such that all elements in the output will be strictly less.
	 * @param length The number of elements in the output.
	 * @param rng The random number generator to use.
	 * @return An array of length {@code} with elements in the range [{@code lower}, {@code upper} - 1].
	 */
	public static int[] getRandomArray(int lower, int upper, int length, SplittableRandom rng) {
		int[] arr = new int[length];
		for(int i = 0; i < length; i++) {
			arr[i] = rng.nextInt(lower, upper);
		}
		return arr;
	}

	/**
	 * Divides every element in {@code arr} by {@code divisor}.
	 *
	 * @pre arr != null
	 * @pre divisor != 0
	 * @param arr The array to perform element-wise division on.
	 * @param divisor The divisor to use.
	 */
	public static void divideAll(int[] arr, int divisor) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] = arr[i] / divisor;
		}
	}

	/**
	 * Does a pseudo-random permutation of the input array.
	 *
	 * This is the classic Fisher-Yates shuffle:
	 * https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle
	 */
	public static void shuffle(int[] arr, SplittableRandom rng) {
		for(int i = arr.length - 1; i > 0; i--) {
			int r = rng.nextInt(i + 1);
			int tmp = arr[r];
			arr[r] = arr[i];
			arr[i] = tmp;
		}
	}
}
