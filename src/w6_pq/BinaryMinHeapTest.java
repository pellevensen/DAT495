package w6_pq;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import testing.PRP;

/**
 *
 * @author Pelle Evensen
 *
 */
class BinaryMinHeapTest {
	private static final int MAX_SPAN = 5000;
	private static final int RUNS = 3;

	@SuppressWarnings({ "static-method", "boxing" })
	@Test
	void test() {
		for (int span = 1; span <= MAX_SPAN; span = Math.max(span + 1, (int) (span * 1.05))) {
			BinaryMinHeap<Integer> heap = new BinaryMinHeap<>();
			Collection<PRP> permutations = new LinkedList<>();

			for (int i = 0; i < RUNS; i++) {
				permutations.add(new PRP(span, i));
			}

			// Insert SPAN * RUNS integers, each integer
			// will be on [0, SPAN) and occur exactly RUNS times.
			for (PRP permutation : permutations) {
				for (long i : permutation) {
					heap.add((int) i);
				}
			}

			assertEquals(span * RUNS, heap.size());

			// The numbers should come out sorted,
			// each number should occur exactly RUNS times.
			for (int i = 0; i < span; i++) {
				for (int j = 0; j < RUNS; j++) {
					int min = heap.getMin();
					assertEquals(i, min);
					min = heap.removeMin();
					assertEquals(i, min);
				}
			}
			assertTrue(heap.isEmpty());
		}
	}

}
