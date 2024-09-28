package org.storynode.pigeon.tuple;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.Option;

/**
 * Quartet class.
 *
 * @param <T1> The type of the first element
 * @param <T2> The type of the second element
 * @param <T3> The type of the third element
 * @param <T4> The type of the fourth element
 * @author Andrea Coronese
 * @since 1.0.0
 */
public final class Quartet<T1, T2, T3, T4> extends Tuple {
  private final T1 first;
  private final T2 second;
  private final T3 third;
  private final T4 fourth;

  /**
   * Constructor for Quartet.
   *
   * @param first a T1 object
   * @param second a T2 object
   * @param third a T3 object
   * @param fourth a T4 object
   */
  public Quartet(T1 first, T2 second, T3 third, T4 fourth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
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

  /**
   * The fourth element of the tuple
   *
   * @return The fourth element
   */
  public T4 fourth() {
    return fourth;
  }

  /** {@inheritDoc} */
  @Override
  public int cardinality() {
    return 4;
  }

  /** {@inheritDoc} */
  @Override
  public @NotNull Option<Object> at(int index) {
    return switch (index) {
      case 0 -> Option.of(this.first);
      case 1 -> Option.of(this.second);
      case 2 -> Option.of(this.third);
      case 3 -> Option.of(this.fourth);
      default -> Option.none();
    };
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(first, second, third, fourth);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "Quartet["
        + "first="
        + first
        + ", "
        + "second="
        + second
        + ", "
        + "third="
        + third
        + ", "
        + "fourth="
        + fourth
        + ']';
  }
}
