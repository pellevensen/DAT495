package testing;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.BitSet;
import java.util.Iterator;
import java.util.SplittableRandom;

import org.junit.jupiter.api.Test;

class PRPTest {
	private static void testPRP(long size, long seed) {
		PRP p = new PRP(size, seed);
		BitSet seen = new BitSet();
		Iterator<Long> pIt = p.iterator();

		for(int i = 0; i < size; i++) {
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

	@SuppressWarnings("static-method")
	@Test
	void testPRP() {
		SplittableRandom rng = new SplittableRandom(1);
		for(int size = 0; size < 100; size++) {
			for(int j = 0; j < 10; j++) {
				testPRP(size, rng.nextLong());
			}
		}
	}

}
