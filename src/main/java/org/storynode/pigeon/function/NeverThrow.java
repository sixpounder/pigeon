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
   * executing.
   *
   * @param func a {@link java.lang.Runnable} object
   * @return a {@link org.storynode.pigeon.wrap.Result} object
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
   * executing.
   *
   * @param func a {@link java.util.function.Supplier} object
   * @param <T> a T class
   * @return a {@link org.storynode.pigeon.wrap.Result} object
   */
  public static <T> @NotNull Result<T, ? extends Throwable> executing(Supplier<T> func) {
    return Result.of(func);
  }
}
