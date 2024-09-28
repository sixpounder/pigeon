package org.storynode.pigeon.option;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.storynode.pigeon.assertion.Assertions.assertThat;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;

public class OptionTest {
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
  void orElseThrow() {
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(
            () -> {
              Option.none().orElseThrow(IllegalArgumentException::new);
            });

    assertThatExceptionOfType(NoSuchElementException.class)
        .isThrownBy(
            () -> {
              Option.none().orElseThrow();
            });
  }
}
