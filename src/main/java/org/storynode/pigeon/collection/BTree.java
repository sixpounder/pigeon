package org.storynode.pigeon.collection;

import java.util.ArrayList;
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
public class BTree<K extends Comparable<K>, V> {
  private final int t; // minimum degree
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

  public BTree(int t) {
    this(t, null);
  }

  public BTree(int t, Map<K, V> initialValues) {
    if (t < 2) throw new IllegalArgumentException("t must be >= 2");
    this.t = t;
    this.root = new Node<>(true, t);
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

  public void add(K key, V value) {
    Node<K, V> r = root;
    // If key already exists, replace value
    if (search(key).isSome()) {
      insertReplace(r, key, value);
      return;
    }
    if (r.n == 2 * t - 1) {
      Node<K, V> s = new Node<>(false, t);
      root = s;
      s.children[0] = r;
      splitChild(s, 0);
      insertNonFull(s, key, value);
    } else {
      insertNonFull(r, key, value);
    }
  }

  // Replace value for existing key (helper)
  private boolean insertReplace(Node<K, V> x, K key, V value) {
    int i = x.findIndex(key);
    if (i < x.n && x.keys[i].key.compareTo(key) == 0) {
      x.keys[i].value = value;
      return true;
    }
    if (x.leaf) return false;
    return insertReplace(x.children[i], key, value);
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
      if (x.children[i].n == 2 * t - 1) {
        splitChild(x, i);
        if (x.keys[i].key.compareTo(key) < 0) i++;
      }
      insertNonFull(x.children[i], key, value);
    }
  }

  private void splitChild(@NotNull Node<K, V> parent, int index) {
    Node<K, V> y = parent.children[index];
    Node<K, V> z = new Node<>(y.leaf, t);
    z.n = t - 1;

    // move last t-1 keys of y to z
    if (t - 1 >= 0) System.arraycopy(y.keys, t, z.keys, 0, t - 1);
    if (!y.leaf) {
      if (t >= 0) System.arraycopy(y.children, t, z.children, 0, t);
    }
    y.n = t - 1;

    // shift parent's children and keys to make space
    for (int j = parent.n; j >= index + 1; j--) parent.children[j + 1] = parent.children[j];
    parent.children[index + 1] = z;

    for (int j = parent.n - 1; j >= index; j--) parent.keys[j + 1] = parent.keys[j];
    parent.keys[index] = y.keys[t - 1];
    parent.n++;
    // clear moved slot (optional)
    y.keys[t - 1] = null;
  }

  // In-order traversal (sorted by key)
  public List<Entry<K, V>> traverse() {
    List<Entry<K, V>> list = new ArrayList<>();
    traverse(root, list);
    return list;
  }

  private void traverse(@NotNull Node<K, V> x, List<Entry<K, V>> out) {
    int i;
    for (i = 0; i < x.n; i++) {
      if (!x.leaf) traverse(x.children[i], out);
      out.add(x.keys[i]);
    }
    if (!x.leaf) traverse(x.children[i], out);
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
}
