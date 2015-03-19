import java.util.Set;

public class BSTMap<K extends Comparable<K>,V> implements Map61B<K, V>{
	private int size = 0; 
	private BST root;

	/** Removes all of the mappings from this map. */
	@Override
	public void clear() {
		size = 0;
		root = null;
		return;
	}

	/* Returns true if this map contains a mapping for the specified key. */
	@Override
	public boolean containsKey(K key) {
		if (root == null || key == null) return false;
		if (root.get(key) == null) return false;
		return true;
	}

	/* Returns the value to which the specified key is mapped, or null if this
	* map contains no mapping for the key. 
	*/
	@Override
	public V get(K key) {
		if (key == null || root == null) return null;
		return root.get(key).val;
	}

	/* Returns the number of key-value mappings in this map. */
	@Override
	public int size() {
		return size;
	}

	/* Associates the specified value with the specified key in this map. */
	@Override
	public void put(K key, V value) {
		if (!containsKey(key)) size++;
		if (root == null) {
			root = new BST(key, value);
		}
		root = root.insert(root, key, value);
		
		return;
	}

	/* Removes the mapping for the specified key from this map if present.
	* Not required for HW6. */
	@Override
	public V remove(K key) {
		return null;
	}

	/* Removes the entry for the specified key only if it is currently mapped to
	* the specified value. Not required for HW6a. */
	@Override
	public V remove(K key, V value) {
		return null;
	}

	/* Returns a Set view of the keys contained in this map. Not required for HW6. */
	@Override
	public Set<K> keySet() {
		return null;
	}

	public void printInOrder() {
		String s = root.print();
		System.out.println("[" + s + "]");
		return;
	}

	private class BST {
		private K key;
		private V val;
		private BST left;
		private BST right;

		public BST(K k, V v) {
			val = v;
			key = k;
		}

		public BST get(K k) {
			if (k == null) {
				return null;
			}
			if (k.equals(key)) {
				return this;
			}
			if (k.compareTo(key) < 0 && left != null) {
				return left.get(k);
			}
			if (right != null) {
				return right.get(k);
			}
			return null;
		}

		public String print() {
			String s = "";
			if (left != null) {
				s = s + left.print();
			}
			s = s +  ", " + key.toString();
			if (right != null) {
				s = s + right.print();
			}
			return s;
		}

		public BST insert(BST t, K k, V v) {
			if (t == null) {
				return new BST(k, v);
			}
			if (k == null || v == null) {
				return t;
			}
			if (k.compareTo(t.key) < 0) {
				t.left = insert(t.left, k, v);
			} else if (k.compareTo(t.key) == 0) {
				t.val = v;
			}else {
				t.right = insert(t.right, k, v);
			}
			return t;
		}

	}
}