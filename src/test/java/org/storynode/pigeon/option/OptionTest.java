package org.storynode.pigeon.option;

import static org.storynode.pigeon.assertion.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class OptionTest {
  @Test
  void construction() {
    Option<Integer> value = Option.some(1);
    assertThat(value).as("Some value").isNotEmpty().isInstanceOf(Some.class);
  }

  @Test
  void unwrap() {
    Option<Integer> value = Option.some(1);
    assertThat(value).as("Some value").returns(1, Option::unwrap);
  }

  @Test
  void isSome() {
    Option<Integer> value = Option.some(1);
    assertThat(value).as("Some value").isNotEmpty();
  }

  @Test
  void isNone() {
    Option<Integer> value = Option.none();
    assertThat(value).as("Some value").isEmpty();
  }
}
