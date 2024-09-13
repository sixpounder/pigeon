package org.storynode.pigeon.tuple;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;

class TupleTest {

  @Test
  void of_pair() {
    Tuple t = Tuple.of(1, "Hello");
    assertThat(t).as("Tuple").isInstanceOf(Pair.class).returns(2, Tuple::cardinality);
  }

  @Test
  void of_triplet() {
    Tuple t = Tuple.of(1, "Hello", new Object());
    assertThat(t).as("Tuple").isInstanceOf(Triplet.class).returns(3, Tuple::cardinality);
  }

  @Test
  void of_quartet() {
    Tuple t = Tuple.of(1, "Hello", new Object(), new Pair<>(10, 9));
    assertThat(t).as("Tuple").isInstanceOf(Quartet.class).returns(4, Tuple::cardinality);
  }

  @Test
  void of_quintet() {
    Tuple t = Tuple.of(1, "Hello", new Object(), new Pair<>(10, 9), 7D);
    assertThat(t).as("Tuple").isInstanceOf(Quintet.class).returns(5, Tuple::cardinality);
  }

  @Test
  void at() {
    Tuple t = Tuple.of(1, "Hello", new Object(), new Pair<>(10, 9), 7D);
    assertThat(t.at(0)).as("First element").contains(1);
    assertThat(t.at(3)).as("Fourth element").contains(Pair.of(10, 9));
    assertThat(t.at(100)).as("Non existing element").isEmpty();
  }

  @Test
  void testEquals() {
    assertThat(Pair.of(1, "Hello")).isEqualTo(Pair.of(1, "Hello"));
    assertThat(Triplet.of(1, "Hello", "World")).isEqualTo(Triplet.of(1, "Hello", "World"));
    assertThat(Triplet.of(1, "Hello", "World")).isNotEqualTo(Pair.of(1, "Hello"));
  }

  @Test
  void enumerate() {
    Tuple tuple = Tuple.of("a", 1, "b", 2, "c");

    Object[] expected = {"a", 1, "b", 2, "c"};
    AtomicInteger counter = new AtomicInteger();
    for (Pair<Object, Integer> entry : tuple.enumerate()) {
      var current = counter.getAndIncrement();
      assertThat(entry.first()).as("Element value").isEqualTo(Optional.of(expected[current]));
      assertThat(entry.second()).as("Element index").isEqualTo(current);
    }

    assertThat(counter.get()).as("Number of cycles").isEqualTo(tuple.cardinality());
  }
}
