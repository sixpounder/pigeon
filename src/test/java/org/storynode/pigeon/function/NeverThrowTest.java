package org.storynode.pigeon.function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.storynode.pigeon.option.None.none;

import org.junit.jupiter.api.Test;
import org.storynode.pigeon.result.Result;

class NeverThrowTest {

  @Test
  void executing() {
    assertThat(NeverThrow.executing(() -> 1))
        .as("Run result")
        .returns(true, Result::isOk)
        .returns(1, Result::unwrap);
  }

  @Test
  void runVoid() {
    assertThat(NeverThrow.executing(() -> System.out.println("Hello world")))
        .as("Run result")
        .isNotNull()
        .returns(true, Result::isOk)
        .returns(none(), Result::unwrap);

    assertThat(
            NeverThrow.executing(
                () -> {
                  throw new IllegalArgumentException("Oh no");
                }))
        .as("Run result")
        .isNotNull()
        .returns(true, Result::isErr)
        .returns(IllegalArgumentException.class, e -> e.unwrapError().getClass());
  }
}
