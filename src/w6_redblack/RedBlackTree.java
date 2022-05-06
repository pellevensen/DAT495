package w6_redblack;

// Sedgewicks's implementation with minor cosmetic edits by Pelle Evensen from
// http://citeseerx.ist.psu.edu/viewdoc/download;jsessionid=2F505A71076FE0A09C9FC73244262CFC?doi=10.1.1.139.282&rep=rep1&type=pdf
public class RedBlackTree<K extends Comparable<K>, V> {
	private static final boolean RED = true;
	private static final boolean BLACK = false;
	private Node<K,V> root;

	private static class Node<K extends Comparable<K>, V> {
		private final K key;
		private V val;
		private Node<K,V> left, right;
		private boolean color;

		public Node(K key, V val) {
			this.key = key;
			this.val = val;
			this.color = RED;
		}
	}

	public V search(K key) {
		Node<K,V> x = this.root;
		while (x != null) {
			int cmp = key.compareTo(x.key);
			if (cmp == 0) {
				return x.val;
			} else if (cmp < 0) {
				x = x.left;
			} else if (cmp > 0) {
				x = x.right;
			}
		}
		return null;
	}

	public void insert(K key, V value) {
		this.root = insert(this.root, key, value);
		this.root.color = BLACK;
	}

	// Left rotate (right link of h)
	private Node<K,V> rotateLeft(Node<K,V> h) {
		Node<K,V> x = h.right;
		h.right = x.left;
		x.left = h;
		x.color = h.color;
		h.color = RED;
		return x;
	}

	// Right rotate (right link of h)
	private Node<K,V> rotateRight(Node<K,V> h) {
		Node<K,V> x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		h.color = RED;
		return x;
	}

	// Flipping colors to split a 4-node
	private void flipColors(Node<K,V> h) {
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}

	private boolean isRed(Node<K,V> node) {
		return node != null && node.color == RED;
	}

	private Node<K,V> insert(Node<K,V> h, K key, V value) {
		if (h == null) {
			return new Node<>(key, value);
		}
		if (isRed(h.left) && isRed(h.right)) {
			flipColors(h);
		}
		int cmp = key.compareTo(h.key);
		if (cmp == 0) {
			h.val = value;
		} else if (cmp < 0) {
			h.left = insert(h.left, key, value);
		} else {
			h.right = insert(h.right, key, value);
		}
		Node<K,V> h2 = h;
		if (isRed(h2.right) && !isRed(h2.left)) {
			h2 = rotateLeft(h);
		}
		if (isRed(h2.left) && isRed(h2.left.left)) {
			h2 = rotateRight(h);
		}
		return h2;
	}

}
