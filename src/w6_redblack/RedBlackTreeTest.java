package w6_redblack;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.jupiter.api.Test;

import testing.PRP;

class RedBlackTreeTest {
	private static final int TEST_SIZE = 1000;

	@SuppressWarnings("static-method")
	@Test
	void test() {
		PRP prpKeys = PRP.Builder.size(TEST_SIZE).seed(20220506123701L).runs(2).build();
		PRP prpValues = PRP.Builder.size(TEST_SIZE).seed(20220506123722L).runs(2).build();
		RedBlackTree<Long, Long> tree = new RedBlackTree<>();
		Map<Long,Long> reference = new HashMap<>();

		Iterator<Long> valueIterator = prpValues.iterator();
		for(Long key : prpKeys) {
			Long value = valueIterator.next();
			reference.put(key, value);
			tree.insert(key, value);
		}

		for (Long key : prpKeys) {
			assertEquals(reference.get(key), tree.search(key));
		}
	}

}
