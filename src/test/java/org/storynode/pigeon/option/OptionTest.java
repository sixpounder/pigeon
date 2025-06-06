package org.storynode.pigeon.option;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.storynode.pigeon.assertion.Assertions.assertThat;

import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OptionTest {
  @Test
  void construction() {
    assertThat(Option.some(1)).as("Some value").isNotEmpty().isInstanceOf(Some.class);
  }

  @Test
  void unwrap() {
    assertThat(Option.some(1)).as("Some value").returns(1, Option::unwrap);
  }

  @Test
  void isSome() {
    assertThat(Option.some(1)).as("Some value").isNotEmpty();
  }

  @Test
  void isNone() {
    assertThat(Option.none()).as("Some value").isEmpty();
  }

  @Test
  void orElse() {
    assertThat(Option.some(1)).as("Optional value").returns(1, v -> v.orElse(2));
    assertThat(Option.none()).as("Optional value").returns(2, v -> v.orElse(2));
  }

  @Test
  void orElseGet() {
    assertThat(Option.some(1)).as("Optional value").returns(1, v -> v.orElseGet(() -> 2));
    assertThat(Option.none()).as("Optional value").returns(2, v -> v.orElseGet(() -> 2));
  }

  @Test
  void orElseThrow() {
    assertThatNoException()
        .isThrownBy(
            () -> {
              var orElseThrow = Option.some(1).orElseThrow();
              Assertions.assertThat(orElseThrow).isEqualTo(1);
            });

    assertThatNoException()
        .isThrownBy(
            () -> {
              var orElseThrow =
                  Option.some(1).orElseThrow(() -> new IllegalArgumentException("Sample error"));
              Assertions.assertThat(orElseThrow).isEqualTo(1);
            });

    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Option.none().orElseThrow(IllegalArgumentException::new));

    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(Option.none()::orElseThrow);
  }

  @Test
  void ifPresent() {
    AtomicBoolean called = new AtomicBoolean(false);
    Option.some(1).ifPresent(v -> called.set(true));
    Assertions.assertThat(called).as("Modified value").isTrue();

    AtomicBoolean notCalled = new AtomicBoolean(false);
    Option.none().ifPresent(v -> notCalled.set(true));
    Assertions.assertThat(notCalled).as("Modified value").isFalse();
  }

  @Test
  void ifPresentOrElse() {
    AtomicInteger value = new AtomicInteger(0);
    Option.some(1).ifPresentOrElse(v -> value.set(1), () -> value.set(-1));
    Assertions.assertThat(value.get()).as("Modified value").isEqualTo(1);

    AtomicInteger value2 = new AtomicInteger(0);
    Option.none().ifPresentOrElse(v -> value2.set(1), () -> value2.set(-1));
    Assertions.assertThat(value2.get()).as("Modified value").isEqualTo(-1);
  }

  @Test
  void map() {
    Option<Integer> value = Option.some(2);
    assertThat(value.map(v -> Math.pow(v, 2))).isEqualTo(Option.some(4.0));

    None<Integer> noValue = Option.none();
    assertThat(noValue.map(v -> Math.pow(v, 2))).isEqualTo(Option.none());
  }

  @Test
  void flatMap() {
    Option<Integer> value = Option.some(2);
    assertThat(value.flatMap(v -> Option.some(Math.pow(v, 2)))).isEqualTo(Option.some(4.0));

    None<Integer> noValue = Option.none();
    assertThat(noValue.flatMap(v -> Option.some(Math.pow(v, 2)))).isEqualTo(Option.none());
  }

  @Test
  void or() {
    assertThat(Option.none().or(Option::none)).as("None or none").isEqualTo(Option.none());
    assertThat(Option.none().or(() -> Option.some(1)))
        .as("None or value")
        .isEqualTo(Option.some(1));
    assertThat(Option.some("First").or(() -> Option.some("Second")))
        .as("Value or value")
        .isEqualTo(Option.some("First"));
    assertThat(Option.some(10).or(Option::none)).as("Value or none").isEqualTo(Option.some(10));
  }

  @Test
  void filter() {
    assertThat(Option.none().filter(v -> v.equals("1"))).isNone();
    assertThat(Option.some(1).filter(v -> v.equals(1))).isSome();
    assertThat(Option.some(1).filter(v -> v.equals(2))).isNone();
  }

  @Test
  void stream() {
    Assertions.assertThat(Option.none().stream().count()).isZero();
    Assertions.assertThat(Option.some(1).stream().count()).isEqualTo(1);
  }

  @Test
  void tryUnwraps() {
    assertThat(Option.none().tryUnwrap()).isError();
    assertThat(Option.some(2).tryUnwrap()).isOk();
  }
}
