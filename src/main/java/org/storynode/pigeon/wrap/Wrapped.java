package org.storynode.pigeon.wrap;

/// A type that serves as a wrapper for a given value that
/// allows accessing that value.
/// @param <T> The type of the wrapped value
public interface Wrapped<T> {
  /// Gets the wrapped value. Implementors that need to complete
  /// this operation exceptionally are advised to use [UnwrapException]
  /// as the thrown type.
  /// @return The wrapped value
  T unwrap();
}
