package testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
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
	private static void testPRPSingleRun(final long size, final long seed) {
		final var p = PRP.Builder.size(size).seed(seed).build();
		final var seen = new BitSet();
		final var pIt = p.iterator();

		for (var i = 0; i < size; i++) {
			@SuppressWarnings("boxing")
			final long v = pIt.next();
			assertTrue(v >= 0);
			assertTrue(v < size);
			final var iv = (int) v;
			assertFalse(seen.get(iv));
			seen.set(iv);
		}

		assertFalse(pIt.hasNext());
		assertEquals(size, seen.cardinality());
		assertEquals(-1, seen.nextSetBit((int) size));
	}

	@SuppressWarnings("boxing")
	private static void testPRPMultiRuns(final int size, final long seed, final int runs) {
		final var p = PRP.Builder.size(size).seed(seed).runs(runs).build();
		final Map<Long, Integer> hist = new HashMap<>();
		final var pIt = p.iterator();

		for (var i = 0; i < size * runs; i++) {
			final long v = pIt.next();
			assertTrue(v >= 0);
			assertTrue(v < size);
			hist.merge(v, 1, (prev, one) -> prev + one);
		}
		for (var i = 0L; i < size; i++) {
			assertEquals(runs, hist.get(i));
		}
		assertFalse(pIt.hasNext());
	}

	private static int grow(final int x, final double factor) {
		return Math.max(x + 1, (int) (x * factor));
	}

	@SuppressWarnings("static-method")
	@Test
	void testPRP() {
		final var rng = new SplittableRandom(1);
		for (var size = 0; size < 20000; size = grow(size, 1.31)) {
			for (var j = 0; j < 10; j++) {
				testPRPSingleRun(size, rng.nextLong());
				for (var runs = 2; runs < 4; runs++) {
					testPRPMultiRuns(size, rng.nextLong(), runs);
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
		final var perm = Builder.size(1L << 30).runs(1L << 31).build();
		assertTrue(perm.iterator().hasNext());
	}

	@Test
	void testInReverseSingleRun() {
		for (var size = 0; size < 20000; size = grow(size, 1.31)) {
			final Map<Long, Integer> seen = new HashMap<>();
			final var permutation = PRP.Builder.size(size).build();
			var i = 0;
			for (final long v : permutation) {
				seen.put(v, i);
				i++;
			}
			for (final long v : permutation.inReverse()) {
				i--;
				assertEquals(i, seen.get(v));
			}
		}
	}

	@Test
	void testInReverseMultiRuns() {
		for (var size = 0; size < 20000; size = grow(size, 1.31)) {
			final List<Map<Long, Integer>> seen = new ArrayList<>();
			for (var runs = 2; runs < 10; runs++) {
				for (var r = 0; r < runs; r++) {
					seen.add(new HashMap<>());
				}
				final var permutation = PRP.Builder.size(size).runs(runs).build();
				var i = 0;
				for (final long v : permutation) {
					int mapIdx;
					for (mapIdx = 0; mapIdx < runs; mapIdx++) {
						if (!seen.get(mapIdx).containsKey(v)) {
							break;
						}
					}
					seen.get(mapIdx).put(v, i);
					i++;
				}
				for (final long v : permutation.inReverse()) {
					int mapIdx;
					for (mapIdx = runs - 1; mapIdx >= 0; mapIdx--) {
						if (seen.get(mapIdx).containsKey(v)) {
							break;
						}
					}
					i--;
					assertEquals(i, seen.get(mapIdx).get(v));
					seen.get(mapIdx).remove(v);
				}
			}
		}
	}

}
