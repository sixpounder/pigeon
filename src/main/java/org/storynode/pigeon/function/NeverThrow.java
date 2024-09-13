package org.storynode.pigeon.function;

import java.util.Optional;
import java.util.function.Supplier;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.wrap.Result;

@UtilityClass
public class NeverThrow {
  public static @NotNull Result<Optional<Void>, ? extends Throwable> executing(Runnable func) {
    try {
      func.run();
      return Result.ok(Optional.empty());
    } catch (Throwable e) {
      return Result.error(e);
    }
  }

  public static <T> @NotNull Result<T, ? extends Throwable> executing(Supplier<T> func) {
    return Result.of(func);
  }
}
