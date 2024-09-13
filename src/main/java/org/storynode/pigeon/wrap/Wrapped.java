package org.storynode.pigeon.wrap;

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
  T unwrap();
}
