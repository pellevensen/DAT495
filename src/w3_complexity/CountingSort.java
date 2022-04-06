package w3_complexity;

import java.util.Arrays;

public class CountingSort {
	// Non-comparison based linear time sort.
	// The complexity is O(n + k) where n is the input size
	// and k is the number of possible distinct values.
	public static void sort(byte[] bs) {
		int[] count = new int[256]; // O(1)
		for(byte b : bs) {  // O(n)
			// A byte in java has value -128 <= b < 128.
			// Adjust to 0 <= b < 256
			count[b + 128]++; // O(1)
		}

		int p = 0; // O(1)
		// The number of steps in the outermost loop is not a function
		// of the input size, hence constant.
		for(int i = 0; i < 256; i++) { // (O(1))
			// Readjust from [0, 256) -> [-128, 128).
			byte b = (byte) (i - 128); // O(1)

			Arrays.fill(bs, p, p + count[i], b); // O(count[i])
			p += count[i]; // O(1)

//			Equivalent snippet, also in O(count[i]):
//			for(int j = 0; j < count[i]; j++) { // (O(count[i]))
//				bs[p++] = b; // O(1)
//			}

		}
	}
}
