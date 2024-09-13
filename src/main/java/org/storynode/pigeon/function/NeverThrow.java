package org.storynode.pigeon.function;

import java.util.Optional;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.wrap.Result;

/**
 * Static functions for exception-less execution
 *
 * @author Andrea Coronese
 * @since 1.0.0
 */
@UtilityClass
public class NeverThrow {
  /**
   * Runs the given function and returns a {@link Result} describing its outcome
   *
   * @param func a function to run
   * @return a {@link org.storynode.pigeon.wrap.Result} object that will contain an empty {@link
   *     Optional} if the execution completed nominally or the error thrown if completed
   *     exceptionally.
   */
  public static @NotNull Result<Optional<Void>, ? extends Throwable> executing(Runnable func) {
    try {
      func.run();
      return Result.ok(Optional.empty());
    } catch (Throwable e) {
      return Result.error(e);
    }
  }

  /**
   * Runs the given function and returns a {@link Result} with the function returned value
   *
   * @param func a function to run
   * @param <T> the type of the return value
   * @return a {@link org.storynode.pigeon.wrap.Result} object that will contain the function return
   *     value if the execution completed nominally or the error thrown if completed exceptionally.
   */
  public static <T> @NotNull Result<T, ? extends Throwable> executing(Supplier<T> func) {
    return Result.of(func);
  }
}
