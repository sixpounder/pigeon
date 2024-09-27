package org.storynode.pigeon.protocol;

import java.util.function.BiFunction;
import java.util.function.Supplier;

/**
 * A {@link java.util.function.Supplier} that allows code to throw checked exceptions
 *
 * @param <T> The type of the supplied value
 * @author sixpounder
 */
@FunctionalInterface
public interface ThrowingSupplier<T> extends Supplier<T> {
  /**
   * Gets the supplied value, throwing any caught exception back to the caller
   *
   * @return The supplied value
   * @throws java.lang.Exception if code completes exceptionally
   */
  T getWithException() throws Exception;

  /** {@inheritDoc} */
  @Override
  default T get() {
    return get(RuntimeException::new);
  }

  /**
   * get.
   *
   * @param exceptionWrapper a {@link java.util.function.BiFunction} object
   * @return a T object
   */
  default T get(BiFunction<String, Exception, RuntimeException> exceptionWrapper) {
    try {
      return getWithException();
    } catch (RuntimeException runtimeException) {
      throw runtimeException;
    } catch (Exception exception) {
      throw exceptionWrapper.apply(exception.getMessage(), exception);
    }
  }
}
