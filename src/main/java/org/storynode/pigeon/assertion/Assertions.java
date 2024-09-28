package org.storynode.pigeon.assertion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.Option;
import org.storynode.pigeon.result.Result;

public class Assertions {
  @Contract("_ -> new")
  public static <T> @NotNull OptionAssert<T> assertThat(Option<T> actual) {
    return new OptionAssert<>(actual);
  }

  @Contract("_ -> new")
  public static <T, E> @NotNull ResultAssert<T, E> assertThat(Result<T, E> actual) {
    return new ResultAssert<>(actual);
  }
}
