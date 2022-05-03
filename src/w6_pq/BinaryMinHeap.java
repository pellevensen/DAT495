package w6_pq;

import java.util.ArrayList;
import java.util.Comparator;

// Binary min-heaps, done elegantly (in Christian's opinion).
public class BinaryMinHeap<T> {

	// The ordering of T.
	private Comparator<T> comparator;

	// The underlying dynamic array list storing the elements of the heap.
	private ArrayList<T> items;

	public BinaryMinHeap(Comparator<T> comparator) {
		this.comparator = comparator;
		this.items = new ArrayList<>();
	}

	// If no comparator is given, we default to the natural order.
	// This assumes that T implements Comparable<? super T>.
	@SuppressWarnings("unchecked")
	public BinaryMinHeap() {
		this((Comparator<T>) Comparator.naturalOrder());
	}

	// Compares the values at indices i and j.
	// This method abstracts over the comparator.
	// The rest of the implementation compares values only using this method.
	private boolean lessThan(int i, int j) {
		return this.comparator.compare(this.items.get(i), this.items.get(j)) < 0;
	}

	// Swaps the values at indices i and j.
	private void swap(int i, int j) {
		T x = this.items.get(i);
		T y = this.items.get(j);
		this.items.set(i, y);
		this.items.set(j, x);
	}

	// Replaces an index with null if it is invalid.
	private Integer validateIndex(int i) {
		return i >= 0 && i < this.items.size() ? i : null;
	}

	// Index computation methods.
	// We return null if there is no valid result.
	// (For example, if there is no left child.)

	private Integer root() {
		return validateIndex(0);
	}

	private Integer last() {
		return validateIndex(this.items.size() - 1);
	}

	private Integer parent(int i) {
		return validateIndex(Math.floorDiv(i - 1, 2));
	}

	private Integer leftChild(int i) {
		return validateIndex(2 * i + 1);
	}

	private Integer rightChild(int i) {
		return validateIndex(2 * i + 2);
	}

	// Recursive methods for swimming up and sinking down.

	// Swim up the value at a given index.
	private void swimUp(int i) {
		// If the parent exists and has larger value, we swap and continue swimming up.
		Integer p = parent(i);
		if (p != null && lessThan(i, p)) {
			swap(i, p);
			swimUp(p);
		}
	}

	// Sink down the value at a given index.
	private void sinkDown(int i) {
		// We compute the index with smallest value among i and its children.
		int smallestIndex = i;
		Integer[] toCheck = { leftChild(i), rightChild(i) };
		for (Integer k : toCheck) {
			if (k != null && lessThan(k, smallestIndex)) {
				smallestIndex = k;
			}
		}

		// If smallestIndex is different from i, we swap and continue sinking down.
		if (smallestIndex != i) {
			swap(i, smallestIndex);
			sinkDown(smallestIndex);
		}
	}

	// Collection methods.

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public int size() {
		return this.items.size();
	}

	public void add(T x) {
		this.items.add(x);
		swimUp(last());
	}

	// Priority queue methods.

	public T getMin() {
		Integer root = root();
		if (root == null) {
			throw new IndexOutOfBoundsException("no element");
		}

		return this.items.get(root);
	}

	public T removeMin() {
		T min = getMin();
		swap(root(), last());

		// We need to cast to int here to select the correct remove method of ArrayList.
		// Without the cast, Java stupidly uses a different remove method
		// that removes the specified *element* instead of the index:
		// ArrayList<T>::remove(Object x)
		// This bad method signature resulted from backward compatibility
		// with the pre-generics version of ArrayList; it should really be:
		// ArrayList<T>::remove(T x)
		this.items.remove((int) last());

		if (root() != null) {
			sinkDown(root());
		}
		return min;
	}

}
