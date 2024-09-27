package org.storynode.pigeon.protocol;

import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.option.Option;

/**
 * A type that serves as a wrapper for a given value that allows accessing that value.
 *
 * @param <T> The type of the wrapped value
 * @author Andrea Coronese
 * @since 1.0.0
 */
public interface Wrapped<T> {
  /**
   * Gets the wrapped value. This method is allowed to throw a {@link
   * org.storynode.pigeon.error.UnwrapException} if the specific implementors requires so.
   *
   * @return The wrapped value
   * @throws org.storynode.pigeon.error.UnwrapException if any.
   */
  T unwrap() throws UnwrapException;

  /**
   * The non-throwing variant of {@link org.storynode.pigeon.protocol.Wrapped#unwrap()}. This is
   * guaranteed to never throw and to always return a non-null value.
   *
   * @return An {@link org.storynode.pigeon.option.Option} containing the value, or empty if there
   *     is none or if an error would be raised while unwrapping said value.
   */
  default Option<T> tryUnwrap() {
    try {
      return Option.some(unwrap());
    } catch (UnwrapException unwrapException) {
      return Option.none();
    }
  }
}
