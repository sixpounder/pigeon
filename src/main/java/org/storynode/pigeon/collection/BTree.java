package org.storynode.pigeon.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.storynode.pigeon.option.Option;

/**
 * Generic B-Tree implementation (mutable, in-memory).
 *
 * <p>This class implements a B-Tree mapping keys to values with the usual B-Tree invariants:
 *
 * <ul>
 *   <li>Each node (except the root) contains between {@code t-1} and {@code 2t-1} keys.
 *   <li>All leaves have the same depth.
 *   <li>Keys within a node are stored in sorted order and child pointers separate the key ranges
 *       like a search tree.
 * </ul>
 *
 * <p>Features provided:
 *
 * <ul>
 *   <li>Search by key: {@link #search} — returns the associated value or {@code null} if absent.
 *   <li>Insertion: {@link #add} — inserts a key/value pair; if the key already exists its value is
 *       replaced.
 *   <li>Ordered traversal: {@link #traverse()} — returns entries in ascending key order.
 *   <li>Debug printing: {@link #printTree()} — prints node keys by level.
 * </ul>
 *
 * <p>Complexities (standard B-Tree guarantees with minimum degree {@code t}):
 *
 * <ul>
 *   <li>Search: O(t * log_t(n)) comparisons (node scan + tree height).
 *   <li>Insert: O(t * log_t(n)) amortized (splits cost proportional to t).
 * </ul>
 *
 * <p>Usage notes:
 *
 * <ul>
 *   <li>Construct with a minimum degree {@code t >= 2}. Larger {@code t} reduces tree height but
 *       increases per-node work and memory per node.
 *   <li>Keys must implement {@link java.lang.Comparable} (type parameter {@code K extends
 *       Comparable<K>}).
 *   <li>This implementation focuses on clarity and correctness for search/insert/traverse; deletion
 *       is intentionally omitted and must be implemented separately if required.
 *   <li>Not thread-safe — external synchronization is required for concurrent access.
 * </ul>
 *
 * <p>Example:
 *
 * <pre>
 *   BTree&lt;Integer,String&gt; bt = new BTree&lt;&gt;(3); // minimum degree 3
 *   bt.insert(10, "ten");
 *   String v = bt.search(10); // "ten"
 * </pre>
 *
 * @param <K> key type; must be {@link Comparable}
 * @param <V> value type
 */
public class BTree<K extends Comparable<K>, V> implements Iterable<BTree.Node<K, V>> {
  private final int minDegree; // minimum degree
  private Node<K, V> root;

  public static class Entry<K extends Comparable<K>, V> {
    K key;
    V value;

    Entry(K k, V v) {
      key = k;
      value = v;
    }
  }

  public static class Node<K extends Comparable<K>, V> {
    int n; // number of keys
    Entry<K, V>[] keys; // length 2t-1
    Node<K, V>[] children; // length 2t
    boolean leaf;

    @SuppressWarnings("unchecked")
    private Node(boolean leaf, int degree) {
      this.leaf = leaf;
      this.keys = (Entry<K, V>[]) new BTree.Entry[2 * degree - 1];
      this.children = new Node[2 * degree];
      this.n = 0;
    }

    // find index of first key >= target
    int findIndex(K target) {
      int i = 0;
      while (i < n && keys[i].key.compareTo(target) < 0) i++;
      return i;
    }
  }

  /** Creates a new {@link BTree} */
  public BTree() {
    this(2, null);
  }

  /**
   * Creates a new {@link BTree} with a minimum degree
   *
   * @param minDegree - the minimum degree
   */
  public BTree(int minDegree) {
    this(minDegree, null);
  }

  /**
   * Creates a new {@link BTree} with a minimum degree and some initial items in it
   *
   * @param minDegree The minimum degree
   * @param initialValues Initial values to insert into the tree
   */
  public BTree(int minDegree, Map<K, V> initialValues) {
    if (minDegree < 2) throw new IllegalArgumentException("minimum degree must be >= 2");
    this.minDegree = minDegree;
    this.root = new Node<>(true, minDegree);
    if (initialValues != null) {
      initialValues.forEach(this::add);
    }
  }

  public @NotNull Option<V> search(K key) {
    return Option.of(search(root, key));
  }

  private @Nullable V search(@NotNull Node<K, V> x, K key) {
    int i = x.findIndex(key);

    if (i < x.n && x.keys[i].key.compareTo(key) == 0) {
      return x.keys[i].value;
    }

    if (x.leaf) {
      return null;
    }

    return search(x.children[i], key);
  }

  /**
   * Adds a node to the tree
   *
   * @param key - The item key
   * @param value The item value
   */
  public void add(K key, V value) {
    Node<K, V> r = root;
    // If key already exists, replace value
    if (search(key).isSome()) {
      insertReplace(r, key, value);
      return;
    }
    if (r.n == 2 * minDegree - 1) {
      Node<K, V> s = new Node<>(false, minDegree);
      root = s;
      s.children[0] = r;
      splitChild(s, 0);
      insertNonFull(s, key, value);
    } else {
      insertNonFull(r, key, value);
    }
  }

  // Replace value for existing key (helper)
  private boolean insertReplace(@NotNull Node<K, V> node, K key, V value) {
    int i = node.findIndex(key);
    if (i < node.n && node.keys[i].key.compareTo(key) == 0) {
      node.keys[i].value = value;
      return true;
    }
    if (node.leaf) return false;
    return insertReplace(node.children[i], key, value);
  }

  private void insertNonFull(@NotNull Node<K, V> x, K key, V value) {
    int i = x.n - 1;
    if (x.leaf) {
      // shift and insert
      while (i >= 0 && x.keys[i].key.compareTo(key) > 0) {
        x.keys[i + 1] = x.keys[i];
        i--;
      }
      x.keys[i + 1] = new Entry<>(key, value);
      x.n++;
    } else {
      while (i >= 0 && x.keys[i].key.compareTo(key) > 0) i--;
      i++;
      if (x.children[i].n == 2 * minDegree - 1) {
        splitChild(x, i);
        if (x.keys[i].key.compareTo(key) < 0) i++;
      }
      insertNonFull(x.children[i], key, value);
    }
  }

  private void splitChild(@NotNull Node<K, V> parent, int index) {
    Node<K, V> y = parent.children[index];
    Node<K, V> z = new Node<>(y.leaf, minDegree);
    // z will have t-1 keys
    z.n = minDegree - 1;

    // move keys y.keys[t .. 2t-2] -> z.keys[0 .. t-2]
    System.arraycopy(y.keys, minDegree, z.keys, 0, minDegree - 1);

    // move children y.children[t .. 2t-1] -> z.children[0 .. t-1]
    if (!y.leaf) {
      System.arraycopy(y.children, minDegree, z.children, 0, minDegree);
    }

    // reduce y
    y.n = minDegree - 1;

    // make room in parent for new child
    for (int j = parent.n; j >= index + 1; j--) parent.children[j + 1] = parent.children[j];
    parent.children[index + 1] = z;

    // make room for median key
    for (int j = parent.n - 1; j >= index; j--) parent.keys[j + 1] = parent.keys[j];

    // lift median from y to parent
    parent.keys[index] = y.keys[minDegree - 1];
    parent.n++;

    // optional: clear moved slots from y
    for (int j = minDegree - 1; j < 2 * minDegree - 1; j++) y.keys[j] = null;
    if (!y.leaf) {
      for (int j = minDegree; j < 2 * minDegree; j++) y.children[j] = null;
    }
  }

  // In-order traversal (sorted by key)
  public List<Entry<K, V>> traverse() {
    List<Entry<K, V>> list = new ArrayList<>();
    traverse(root, list);
    return list;
  }

  public boolean isEmpty() {
    for (Node<K, V> node : this) {
      if (node.n > 0) {
        return false;
      }
    }

    return true;
  }

  private void traverse(@NotNull Node<K, V> x, List<Entry<K, V>> out) {
    int i;
    for (i = 0; i < x.n; i++) {
      if (!x.leaf) traverse(x.children[i], out);
      out.add(x.keys[i]);
    }
    if (!x.leaf) traverse(x.children[i], out);
  }

  /**
   * Collects they keys in the tree and returns them
   *
   * @return All the keys in the tree
   */
  public java.util.Set<K> keySet() {
    java.util.Set<K> set = new java.util.HashSet<>();
    for (Node<K, V> node : this) {
      for (int i = 0; i < node.n; i++) {
        K key = node.keys[i].key;
        set.add(key);
      }
    }
    return set;
  }

  @Override
  public @NotNull Iterator<Node<K, V>> iterator() {
    return new NodeIterator<>(root);
  }

  // Utility to print keys at each node (debug)
  public void printTree() {
    printNode(root, 0);
  }

  private void printNode(@NotNull Node<K, V> x, int level) {
    System.out.print("Level " + level + " [");
    for (int i = 0; i < x.n; i++) {
      System.out.print(x.keys[i].key);
      if (i < x.n - 1) System.out.print(", ");
    }
    System.out.println("]");
    if (!x.leaf) {
      for (int i = 0; i <= x.n; i++) printNode(x.children[i], level + 1);
    }
  }

  private static class NodeIterator<K extends Comparable<K>, V>
      implements java.util.Iterator<Node<K, V>> {
    private final java.util.Deque<Node<K, V>> stack = new java.util.ArrayDeque<>();

    NodeIterator(Node<K, V> start) {
      if (start != null) stack.push(start);
    }

    @Override
    public boolean hasNext() {
      return !stack.isEmpty();
    }

    @Override
    public Node<K, V> next() {
      Node<K, V> node = stack.poll();
      if (node == null) throw new java.util.NoSuchElementException();
      // push children in reverse order so leftmost child is returned first
      if (!node.leaf) {
        for (int i = node.n; i >= 0; i--) {
          Node<K, V> child = node.children[i];
          if (child != null) stack.push(child);
        }
      }
      return node;
    }
  }
}
