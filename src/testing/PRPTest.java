package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;

import testing.PRP.Builder;

/**
 *
 * @author Pelle Evensen
 *
 */
class PRPTest {
	private static void testPRP_singleRun(long size, long seed) {
		PRP p = PRP.Builder.size(size).seed(seed).build();
		BitSet seen = new BitSet();
		Iterator<Long> pIt = p.iterator();

		for (int i = 0; i < size; i++) {
			@SuppressWarnings("boxing")
			long v = pIt.next();
			assertTrue(v >= 0);
			assertTrue(v < size);
			int iv = (int) v;
			assertFalse(seen.get(iv));
			seen.set(iv);
		}
		assertFalse(pIt.hasNext());
	}

	@SuppressWarnings("boxing")
	private static void testPRP_multiRuns(long size, long seed, int runs) {
		PRP p = PRP.Builder.size(size).seed(seed).runs(runs).build();
		Map<Long, Integer> hist = new HashMap<>();
		Iterator<Long> pIt = p.iterator();

		for (int i = 0; i < size * runs; i++) {
			long v = pIt.next();
			assertTrue(v >= 0);
			assertTrue(v < size);
			hist.merge(v, 1, (prev, one) -> prev + one);
		}
		for (long i = 0; i < size; i++) {
			assertEquals(runs, hist.get(i));
		}
		assertFalse(pIt.hasNext());
	}

	private static int grow(int x, double factor) {
		return Math.max(x + 1, (int) (x * factor));
	}

	@SuppressWarnings("static-method")
	@Test
	void testPRP() {
		SplittableRandom rng = new SplittableRandom(1);
		for (int size = 0; size < 20000; size = grow(size, 1.31)) {
			for (int j = 0; j < 10; j++) {
				testPRP_singleRun(size, rng.nextLong());
				for(int runs = 1; runs < 3; runs++) {
					testPRP_multiRuns(size, rng.nextLong(), runs);
				}
			}
		}
	}

	@SuppressWarnings("static-method")
	@Test
	void testInvalidSizes() {
		assertThrows(IllegalArgumentException.class, () -> Builder.size(-1));
		assertThrows(IllegalArgumentException.class, () -> Builder.size(1).runs(-1));
		assertThrows(IllegalStateException.class, () -> Builder.size(1L << 31).runs(1L << 31).build());
		PRP perm = Builder.size(1L << 30).runs(1L << 31).build();
		assertTrue(perm.iterator().hasNext());
	}
}
