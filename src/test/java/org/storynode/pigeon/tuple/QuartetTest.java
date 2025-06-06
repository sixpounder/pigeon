package org.storynode.pigeon.tuple;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.storynode.pigeon.option.Option;

class QuartetTest {
  @Test
  void cardinality() {
    assertThat(new Quartet<>(1, 1, 1, 1).cardinality()).as("Cardinality").isEqualTo(4);
  }

  @Test
  void at() {
    Quartet<Integer, Integer, Integer, Integer> value = new Quartet<>(1, 2, 3, 4);
    assertThat(value.at(0)).as("Index 0").isEqualTo(Option.of(1));
    assertThat(value.at(1)).as("Index 1").isEqualTo(Option.of(2));
    assertThat(value.at(2)).as("Index 2").isEqualTo(Option.of(3));
    assertThat(value.at(3)).as("Index 3").isEqualTo(Option.of(4));
    assertThat(value.at(5)).as("Index out of range").isEqualTo(Option.none());
    assertThat(value.at(100)).as("Index WAY out of range").isEqualTo(Option.none());
  }

  @Test
  void first() {
    Quartet<Integer, Integer, Integer, Integer> value = new Quartet<>(1, 2, 3, 4);
    assertThat(value.first()).as("First").isEqualTo(1);
  }

  @Test
  void second() {
    Quartet<Integer, Integer, Integer, Integer> value = new Quartet<>(1, 2, 3, 4);
    assertThat(value.second()).as("Second").isEqualTo(2);
  }

  @Test
  void third() {
    Quartet<Integer, Integer, Integer, Integer> value = new Quartet<>(1, 2, 3, 4);
    assertThat(value.third()).as("Third").isEqualTo(3);
  }

  @Test
  void fourth() {
    Quartet<Integer, Integer, Integer, Integer> value = new Quartet<>(1, 2, 3, 4);
    assertThat(value.fourth()).as("Fourth").isEqualTo(4);
  }

  @Test
  void equals() {
    assertThat(new Quartet<>(1, 2, "Hello", "World").equals(new Quartet<>(1, 2, "Hello", "World")))
        .as("Simple equality check")
        .isTrue();

    assertThat(
            new Quartet<>(1, 2, "Hello", "World").equals(new Quartet<>(1, 2, "Hello", "Everyone")))
        .as("Simple inequality check")
        .isFalse();
  }

  @Test
  void testToString() {
    Tuple value = new Quartet<>(1, 2, "Hello", "World");
    assertThat(value.toString())
        .as("String representation")
        .satisfies(
            v -> {
              for (Pair<Object, Integer> item : value.enumerate()) {
                assertThat(v).as("String representation").contains(item.first().toString());
              }
            });
  }

  @Test
  void testHashCode() {
    assertThat(Tuple.of(1, 2, 3, 4).hashCode()).as("Hash code").isNotZero();
  }
}
