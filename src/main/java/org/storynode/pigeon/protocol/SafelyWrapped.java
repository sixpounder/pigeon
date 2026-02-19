package org.storynode.pigeon.protocol;

import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.result.Result;

/**
 * The SafelyWrapped interface extends the {@link Wrapped} interface and provides a mechanism to
 * safely access the wrapped value without the risk of throwing exceptions.
 *
 * @param <T> The type of the value being wrapped.
 * @author Andrea Coronese
 */
public interface SafelyWrapped<T> extends Wrapped<T> {
  /**
   * The non-throwing variant of {@link org.storynode.pigeon.protocol.Wrapped#unwrap()}. This is
   * guaranteed to never throw and to always return a non-null value.
   *
   * @return An {@link org.storynode.pigeon.option.Option} containing the value, or empty if there
   *     is none or if an error would be raised while unwrapping said value.
   */
  default Result<T, Throwable> tryUnwrap() {
    try {
      return Result.ok(unwrap());
    } catch (Exception ex) {
      return Result.err(new UnwrapException("Cannot unwrap " + this));
    }
  }
}
