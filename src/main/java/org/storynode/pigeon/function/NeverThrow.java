package org.storynode.pigeon.function;

import static org.storynode.pigeon.option.None.none;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.None;
import org.storynode.pigeon.protocol.ThrowingSupplier;
import org.storynode.pigeon.result.Result;

/**
 * Static functions for exception-less execution
 *
 * @author Andrea Coronese
 * @since 1.0.0
 */
@UtilityClass
public class NeverThrow {
  /**
   * Runs the given function and returns a {@link org.storynode.pigeon.result.Result} describing its
   * outcome
   *
   * @param func a function to run
   * @return a {@link org.storynode.pigeon.result.Result} object that will contain an empty {@link
   *     org.storynode.pigeon.option.Option} if the execution completed nominally or the error
   *     thrown if completed exceptionally.
   */
  public static @NotNull Result<None<?>, Exception> executing(Runnable func) {
    try {
      func.run();
      return Result.ok(none());
    } catch (Exception e) {
      return Result.error(e);
    }
  }

  /**
   * Runs the given function and returns a {@link org.storynode.pigeon.result.Result} with the
   * function returned value
   *
   * @param func a function to run
   * @param <T> the type of the return value
   * @return a {@link org.storynode.pigeon.result.Result} object that will contain the function
   *     return value if the execution completed nominally or the error thrown if completed
   *     exceptionally.
   */
  public static <T> @NotNull Result<T, ? extends Throwable> executing(ThrowingSupplier<T> func) {
    return Result.of(func);
  }
}
