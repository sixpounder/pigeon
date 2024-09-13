package org.storynode.pigeon.wrap;

import java.util.Optional;
import org.storynode.pigeon.error.UnwrapException;

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
   */
  T unwrap() throws UnwrapException;

  /**
   * The non-throwing variant of {@link Wrapped#unwrap()}. This is guaranteed to never throw and to
   * always return a non-null value.
   *
   * @return An {@link java.util.Optional} containing the value, or empty if there is none or if an
   *     error would be raised while unwrapping said value.
   */
  default Optional<T> tryUnwrap() {
    try {
      return Optional.ofNullable(unwrap());
    } catch (UnwrapException unwrapException) {
      return Optional.empty();
    }
  }
}
