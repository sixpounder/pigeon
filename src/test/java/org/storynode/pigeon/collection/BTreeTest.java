package org.storynode.pigeon.collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.storynode.pigeon.assertion.Assertions;
import org.storynode.pigeon.option.Option;

class BTreeTest {
  private BTree<Integer, String> bt;

  @BeforeEach
  void setUp() {
    bt = new BTree<>(3, Map.of(10, "ten", 20, "twenty", 5, "five"));
  }

  @Test
  void defaultConstructor() {
    BTree<Integer, String> bt = new BTree<>();
    assertThat(bt.isEmpty()).isTrue();
  }

  @Test
  void isEmpty() {
    assertThat(bt.isEmpty()).isFalse();
  }

  @Test
  void testInsertAndSearch() {
    Assertions.assertThat(bt.search(10)).isSome().returns("ten", Option::unwrap);
    Assertions.assertThat(bt.search(20)).isSome().returns("twenty", Option::unwrap);
    Assertions.assertThat(bt.search(5)).isSome().returns("five", Option::unwrap);
    Assertions.assertThat(bt.search(99)).isNone();
  }

  @Test
  void testReplaceValueOnInsert() {
    bt.add(15, "fifteen");
    Assertions.assertThat(bt.search(15)).isSome().returns("fifteen", Option::unwrap);
    bt.add(15, "15-updated");
    Assertions.assertThat(bt.search(15)).isSome().returns("15-updated", Option::unwrap);
  }

  @Test
  void testInOrderTraversalIsSorted() {
    int[] keys = {10, 20, 5, 6, 12, 30, 7, 17};
    for (int k : keys) bt.add(k, "v" + k);

    List<?> entries = bt.traverse(); // entries are BTree.Entry objects
    assertThat(keys.length).isEqualTo(entries.size());

    // Extract keys in order and verify sorted ascending
    int prev = Integer.MIN_VALUE;
    for (Object o : entries) {
      // reflection-safe extraction in case Entry is non-public
      Integer key;
      try {
        var keyField = o.getClass().getDeclaredField("key");
        keyField.setAccessible(true);
        key = (Integer) keyField.get(o);
      } catch (ReflectiveOperationException e) {
        fail("Failed to access entry.key via reflection: " + e.getMessage());
        return;
      }
      assertTrue(key >= prev, "traverse should produce non-decreasing keys");
      prev = key;
    }
  }

  @Test
  void testNodeSplittingBehavior() {
    // Insert enough keys to force splits in a t=3 tree
    int[] keys = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 20};
    for (int k : keys) bt.add(k, "v" + k);

    // All keys should be searchable after many inserts
    for (int k : keys) {
      assertThat(bt.search(k)).returns("v" + k, Option::unwrap);
    }

    // Basic structural sanity: traversal size equals number inserted
    assertThat(keys.length).isEqualTo(bt.traverse().size());
  }

  @Test
  void noDuplicates() {
    // Basic sanity again: each key should be present only once across
    // the tree
    BTree<Integer, Integer> tree =
        new BTree<>(3, Map.of(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8, 9, 9, 10, 10));

    Set<Integer> keys = tree.keySet();
    assertThat(keys).hasSize(tree.traverse().size());
    assertThat(keys).isEqualTo(Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
  }

  @Test
  void testEmptyTreeBehavior() {
    BTree<Integer, String> empty = new BTree<>(3);
    Assertions.assertThat(empty.search(1)).isNone();
    assertThat(empty.traverse()).isEmpty();
  }

  @Test
  void printTree() {
    BTree<Integer, Integer> tree = new BTree<>(3, Map.of(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7));
    assertThatNoException().isThrownBy(tree::printTree);
  }
}
