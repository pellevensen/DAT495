package w5_midpoint_review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.junit.jupiter.api.Test;

import testing.PRP;

class NaiveTreeMapTest {
	private static final int MAX_SPAN = 31;

	@SuppressWarnings({ "static-method" })
	@Test
	void test() {
		for (int span = 31; span <= MAX_SPAN; span = Math.max(span + 1, (int) (span * 1.05))) {
			NaiveTreeMap<Long, Long> bst = new NaiveTreeMap<>();

			for(int pIdx = 0; pIdx < 1; pIdx++) {
				// Insert {0, ..., span - 1} in pseudo-random order.
				PRP insertionPermutation = new PRP(span, pIdx);
				for (Long v : insertionPermutation) {
					bst.put(v, v);
				}
				for (Long v : insertionPermutation) {
					assertEquals(v, bst.get(v));
				}

				// Remove {0, ..., span - 1} in pseudo-random order.
				PRP removalPermutation = new PRP(span, -pIdx - 1);
				Iterator<Long> pIt = removalPermutation.iterator();
				Long prev = pIt.next();
				while(pIt.hasNext()) {
					bst.remove(prev);
					// Check that the item was removed and...
					assertNull(bst.get(prev));

					// that the next item, which is guaranteed to not have been
					// removed yet, is still in the map.
					prev = pIt.next();
					assertEquals(prev, bst.get(prev));
				}

				// Remove last element.
				bst.remove(prev);

				// And the map should now be empty!
				assertTrue(bst.isEmpty());
			}
		}
	}
}
