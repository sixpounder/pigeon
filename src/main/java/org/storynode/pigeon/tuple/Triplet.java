package org.storynode.pigeon.tuple;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.Option;

/**
 * Triplet class.
 *
 * @param <T1> The type of the first element
 * @param <T2> The type of the second element
 * @param <T3> The type of the third element
 * @author Andrea Coronese
 * @since 1.0.0
 */
public final class Triplet<T1, T2, T3> extends Tuple {
  private final T1 first;
  private final T2 second;
  private final T3 third;

  /**
   * Constructor for Triplet.
   *
   * @param first a T1 object
   * @param second a T2 object
   * @param third a T3 object
   */
  public Triplet(T1 first, T2 second, T3 third) {
    this.first = first;
    this.second = second;
    this.third = third;
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

  /**
   * The third element of the tuple
   *
   * @return The third element
   */
  public T3 third() {
    return third;
  }

  /** {@inheritDoc} */
  @Override
  public int cardinality() {
    return 3;
  }

  /** {@inheritDoc} */
  @Override
  public @NotNull Option<Object> at(int index) {
    return switch (index) {
      case 0 -> Option.of(this.first);
      case 1 -> Option.of(this.second);
      case 2 -> Option.of(this.third);
      default -> Option.none();
    };
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(first, second, third);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "Triplet["
        + "first="
        + first
        + ", "
        + "second="
        + second
        + ", "
        + "third="
        + third
        + ']';
  }
}
