package org.storynode.pigeon.tuple;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Pair class.
 *
 * @param <T1> The type of the first element
 * @param <T2> The type of the second element
 * @author Andrea Coronese
 * @since 1.0.0
 */
public final class Pair<T1, T2> extends Tuple {
  private final T1 first;
  private final T2 second;

  /**
   * Constructor for Pair.
   *
   * @param first a T1 object
   * @param second a T2 object
   */
  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  /** {@inheritDoc} */
  @Override
  public int cardinality() {
    return 2;
  }

  /** {@inheritDoc} */
  @Override
  public @NotNull Optional<Object> at(int index) {
    return switch (index) {
      case 0 -> Optional.of(this.first);
      case 1 -> Optional.of(this.second);
      default -> Optional.empty();
    };
  }

  /**
   * The first element of the tuple
   *
   * @return The first element
   */
  public T1 first() {
    return first;
  }

  /**
   * The second element of the tuple
   *
   * @return The second element
   */
  public T2 second() {
    return second;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "Pair[" + "first=" + first + ", " + "second=" + second + ']';
  }
}