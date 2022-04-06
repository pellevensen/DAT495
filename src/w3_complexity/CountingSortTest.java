package w3_complexity;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import java.util.SplittableRandom;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountingSortTest {
	private SplittableRandom rng;

	@BeforeEach
	void setUp() {
		this.rng = new SplittableRandom();
	}

	@SuppressWarnings("static-method")
	@Test
	void testEmpty() {
		byte[] bs = new byte[0];
		CountingSort.sort(bs);
		// If no exception is thrown: success.
	}

	@SuppressWarnings("static-method")
	@Test
	void testSingleton() {
		byte[] bs = new byte[1];
		for (int i = -128; i < 128; i++) {
			bs[0] = (byte) i;
			CountingSort.sort(bs);
			assertEquals(i, bs[0]);
		}
	}

	@SuppressWarnings("static-method")
	@Test
	void testRandom() {
		for (int len = 1; len < 10000; len *= 2) {
			byte[] bs = new byte[len];
			this.rng.nextBytes(bs);
			byte[] bsCopy = bs.clone();
			Arrays.sort(bsCopy);
			CountingSort.sort(bs);
			assertArrayEquals(bsCopy, bs);
		}
	}

	void testTimeSort(String sorterName, Consumer<byte[]> sorter) {
		for (int len = 100; len < 20000000; len *= 10) {
			long acc = 0;
			long dummy = 0;
			long bytesSorted = 0;
			byte[] bs = new byte[len];
			for (int reps = 0; reps < 20000000 / len; reps++) {
				this.rng.nextBytes(bs);
				long now = System.nanoTime();
				CountingSort.sort(bs);
				sorter.accept(bs);
				acc += System.nanoTime() - now;
				bytesSorted += len;
				dummy += bs[len / 2];
			}
			if ((dummy & 0xFF) >= 0) {
				System.out.printf("%20s length: %4.2e bytes, time used: %4.2f ns/byte, %9.2f µs/array\n", sorterName,
						(double) len, bytesSorted / (double) acc, bytesSorted * len / acc * 0.001);
			}
		}
		System.out.println("");
	}

	@Test
	void timeTestCountingSort() {
		testTimeSort("CountingSort", CountingSort::sort);
	}

	@Test
	void timeTestBuiltinSort() {
		testTimeSort("Arrays.sort", Arrays::sort);
	}
}
