package org.storynode.pigeon.protocol;

import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.result.Result;

/**
 * SafelyWrapped interface.
 *
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
  default Result<T, ? extends Throwable> tryUnwrap() {
    try {
      return Result.ok(unwrap());
    } catch (Exception ex) {
      return Result.err(new UnwrapException("Cannot unwrap " + this));
    }
  }
}
