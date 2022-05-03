package w5_midpoint_review;

import java.util.Objects;

public class NaiveTreeMap<K extends Comparable<K>, V> {
	private Node<K, V> root;

	private static class Node<K extends Comparable<K>, V> {
		private Node<K, V> left;
		private Node<K, V> right;
		private K key;
		private V value;

		public Node(K key, V value) {
			this.key = Objects.requireNonNull(key);
			this.value = value;
		}

		/*
		 * Unneeded since the surrounding class manipulates nodes directly.
		public K getKey() {
			return this.key;
		}

		public V getValue() {
			return this.value;
		}

		public Node<K, V> getLeft() {
			return this.left;
		}

		public Node<K, V> getRight() {
			return this.right;
		}
		*/

		public int size() {
//			int sum = 1;
//			if(this.left != null) {
//				sum += this.left.size();
//			}
//			if(this.right != null) {
//				sum += this.right.size();
//			}
//			return sum;

			return (this.left != null ? this.left.size() : 0) + 1 + (this.right != null ? this.right.size() : 0);
		}

		public void put(@SuppressWarnings("hiding") K key, @SuppressWarnings("hiding") V value) {
			int cmp = this.key.compareTo(key);
			if (cmp == 0) {
				this.value = value;
			} else if (cmp < 0) {
				if (this.right == null) {
					this.right = new Node<>(key, value);
				} else {
					this.right.put(key, value);
				}
			} else {
				if (this.left == null) {
					this.left = new Node<>(key, value);
				} else {
					this.left.put(key, value);
				}
			}
		}

		public Node<K, V> findMax() {
			if (this.right != null) {
				return this.right.findMax();
			}
			return this;
		}

		public V get(@SuppressWarnings("hiding") K key) {
			int cmp = this.key.compareTo(key);
			if (cmp == 0) {
				return this.value;
			} else if (cmp < 0) {
				return this.right != null ? this.right.get(key) : null;
			} else {
				return this.left != null ? this.left.get(key) : null;
			}
		}

		private String toString(Node<K, V> n, int level) {
			if (n == null) {
				return "null".indent(2 * level);
			}
			String leftString = ("L->" + toString(n.left, level)).indent(2 * level + 1);
			String rightString = ("R->" + toString(n.right, level)).indent(2 * level + 1);

			return "[" + n.key + ":" + n.value + "\n" + leftString + ""
					+ rightString + "]".indent(2 * level);
		}

		@Override
		public String toString() {
			return toString(this, 1);
		}
	}

	public int size() {
		return this.root != null ? this.root.size() : 0;
	}

	public boolean isEmpty() {
		return this.root == null;
	}

	public V get(K key) {
		return this.root == null ? null : this.root.get(key);
	}

	public void put(K key, V value) {
		if (this.root == null) {
			this.root = new Node<>(key, value);
		} else {
			this.root.put(key, value);
		}
	}

	private Node<K, V> remove(Node<K, V> subTreeRoot, K key) {
		// Base Case: Empty subtree.
		if (subTreeRoot == null) {
	 		return null;
		}

		// Otherwise, recurse down.
		int cmp = subTreeRoot.key.compareTo(key);
		if (cmp > 0) {
			subTreeRoot.left = remove(subTreeRoot.left, key);
		} else if (cmp < 0) {
			subTreeRoot.right = remove(subTreeRoot.right, key);
		} else {
			// Key matches => this is the node to delete.
			// Handle the simple cases, one or no child.
			if (subTreeRoot.left == null) {
				return subTreeRoot.right;
			}
			if (subTreeRoot.right == null) {
				return subTreeRoot.left;
			}

			// Subtree has two children. Get the predecessor
			// (greatest in the left subtree).
			Node<K, V> predecessor = subTreeRoot.left.findMax();
			subTreeRoot.key = predecessor.key;
			subTreeRoot.value = predecessor.value;

			// Delete the predecessor.
			subTreeRoot.left = remove(subTreeRoot.left, subTreeRoot.key);
		}

		return subTreeRoot;
	}

	public void remove(K key) {
		this.root = remove(this.root, key);
	}

	@Override
	public String toString() {
		return "NaiveTreeMap [root=\n" + this.root + "]";
	}

}
