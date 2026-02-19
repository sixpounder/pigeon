package org.storynode.pigeon.protocol;

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
   * Gets the wrapped value. This method is allowed to throw a {@link UnwrapException} if the
   * implementor requires so.
   *
   * @return The wrapped value
   * @throws org.storynode.pigeon.error.UnwrapException if any.
   */
  T unwrap() throws UnwrapException;
}
