package org.storynode.pigeon.tuple;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;

class PairTest {

  @Test
  void cardinality() {
    assertThat(new Pair<>(1, 1).cardinality()).as("Cardinality").isEqualTo(2);
  }

  @Test
  void at() {
    Pair<Integer, Integer> pair = new Pair<>(1, 2);
    assertThat(pair.at(0)).as("Index 0").isEqualTo(Optional.of(1));
    assertThat(pair.at(1)).as("Index 1").isEqualTo(Optional.of(2));
    assertThat(pair.at(2)).as("Index out of range").isEqualTo(Optional.empty());
    assertThat(pair.at(100)).as("Index WAY out of range").isEqualTo(Optional.empty());
  }

  @Test
  void first() {
    Pair<Integer, Integer> pair = new Pair<>(1, 2);
    assertThat(pair.first()).as("First").isEqualTo(1);
  }

  @Test
  void second() {
    Pair<Integer, Integer> pair = new Pair<>(1, 2);
    assertThat(pair.second()).as("Second").isEqualTo(2);
  }
}
