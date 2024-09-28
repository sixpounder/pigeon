package org.storynode.pigeon.assertion;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.Option;
import org.storynode.pigeon.result.Result;

/**
 * Assertions class.
 *
 * @author Andrea Coronese
 */
public class Assertions {
  /**
   * assertThat.
   *
   * @param actual a {@link org.storynode.pigeon.option.Option} object
   * @param <T> a T class
   * @return a {@link org.storynode.pigeon.assertion.OptionAssert} object
   */
  @Contract("_ -> new")
  public static <T> @NotNull OptionAssert<T> assertThat(Option<T> actual) {
    return new OptionAssert<>(actual);
  }

  /**
   * assertThat.
   *
   * @param actual a {@link org.storynode.pigeon.result.Result} object
   * @param <T> a T class
   * @param <E> a E class
   * @return a {@link org.storynode.pigeon.assertion.ResultAssert} object
   */
  @Contract("_ -> new")
  public static <T, E> @NotNull ResultAssert<T, E> assertThat(Result<T, E> actual) {
    return new ResultAssert<>(actual);
  }
}
