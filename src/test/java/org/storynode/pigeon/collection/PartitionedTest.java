package org.storynode.pigeon.collection;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.storynode.pigeon.result.Result;

class PartitionedTest {
  private Partitioned<Integer, Integer> partitioned;

  @BeforeEach
  void setUp() {
    partitioned = new Partitioned<>(n -> n % 2, List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
  }

  @Test
  void emptyInit() {
    assertThat(new Partitioned<>(o -> true)).isEmpty();
  }

  @Test
  void size() {
    assertThat(partitioned.size()).isEqualTo(2);
  }

  @Test
  void add() {
    partitioned.add(11);
    assertThat(partitioned.size()).isEqualTo(2);
    assertThat(partitioned.containsValue(11)).isTrue();
  }

  @Test
  void addAll() {
    boolean changed = partitioned.addAll(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
    assertThat(changed).isFalse();

    changed = partitioned.addAll(List.of(11, 12, 13));
    assertThat(changed).isTrue();
    assertThat(partitioned.size()).isEqualTo(2);
    assertThat(partitioned.containsValue(11)).isTrue();
    assertThat(partitioned.containsValue(12)).isTrue();
    assertThat(partitioned.containsValue(13)).isTrue();
  }

  @Test
  void isEmpty() {
    assertThat(partitioned.isEmpty()).isFalse();
    assertThat(Partitioned.by((o) -> true).isEmpty()).isTrue();
  }

  @Test
  void containsKey() {
    assertThat(partitioned.containsKey(0)).isTrue();
    assertThat(partitioned.containsKey(1)).isTrue();
    assertThat(partitioned.containsKey(2)).isFalse();
  }

  @Test
  void containsValue() {
    assertThat(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)).allMatch(o -> partitioned.containsValue(o));
    assertThat(List.of(11, 12, 13, 14, 15, 16, 17, 18, 19, 20))
        .noneMatch(o -> partitioned.containsValue(o));
  }

  @Test
  void tryGet() {
    assertThat(partitioned.tryGet(0))
        .as("No error and value is present")
        .returns(true, Result::isOk)
        .returns(true, r -> r.unwrap().isSome());
    assertThat(partitioned.tryGet(1))
        .as("No error and value is present")
        .returns(true, Result::isOk)
        .returns(true, r -> r.unwrap().isSome());
    assertThat(partitioned.tryGet(2))
        .as("No error, but no partition either")
        .returns(true, Result::isOk)
        .returns(true, r -> r.unwrap().isNone());
  }

  @Test
  void get() {
    assertThat(partitioned.get(0)).isNotNull();
    assertThat(partitioned.get(1)).isNotNull();
    assertThat(partitioned.get(2)).isNull();
  }

  @Test
  void tryRemove() {
    assertThat(partitioned.tryRemove(0)).returns(true, Result::isOk);
    assertThat(partitioned.tryRemove(1)).returns(true, Result::isOk);
    assertThat(partitioned.tryRemove(2)).returns(true, Result::isOk);
    assertThat(partitioned.tryRemove(null)).returns(true, Result::isOk);
  }

  @Test
  void tryRemoveValue() {
    assertThat(partitioned.tryRemoveValue(0)).returns(true, Result::isOk);
    assertThat(partitioned.tryRemoveValue(1)).returns(true, Result::isOk);
    assertThat(partitioned.tryRemoveValue(2)).returns(true, Result::isOk);
    assertThat(partitioned.tryRemoveValue(99))
        .returns(true, Result::isOk)
        .returns(true, e -> e.unwrap().isNone());
    ;
    assertThat(partitioned.tryRemoveValue(null)).returns(true, Result::isErr);
  }

  @Test
  void clear() {
    assertThat(partitioned).isNotEmpty();
    partitioned.clear();
    assertThat(partitioned).isEmpty();
  }

  @Test
  void keySet() {
    assertThat(partitioned.keySet()).isNotEmpty();
  }

  @Test
  void values() {
    assertThat(partitioned.values()).isNotEmpty();
  }

  @Test
  void entrySet() {
    assertThat(partitioned.entrySet()).isNotEmpty();
  }

  @Test
  void contains() {
    assertThat(partitioned.contains(1)).isTrue();
    assertThat(partitioned.contains(2)).isTrue();
    assertThat(partitioned.contains(12)).isFalse();
  }

  @Test
  void iterator() {
    assertThat(partitioned.iterator()).isNotNull().isInstanceOf(Iterator.class);
  }

  @Test
  void by() {
    assertThat(Partitioned.by(o -> true)).isEmpty();
    assertThat(Partitioned.by(o -> true, List.of(1, 2, 3))).isNotEmpty();
  }
}
