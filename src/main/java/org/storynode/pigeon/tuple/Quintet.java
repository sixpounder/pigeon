package org.storynode.pigeon.tuple;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Quintet class.
 *
 * @param <T1> The type of the first element
 * @param <T2> The type of the second element
 * @param <T3> The type of the third element
 * @param <T4> The type of the fourth element
 * @param <T5> The type of the fifth element
 * @author Andrea Coronese
 * @since 1.0.0
 */
public final class Quintet<T1, T2, T3, T4, T5> extends Tuple {
  private final T1 first;
  private final T2 second;
  private final T3 third;
  private final T4 fourth;
  private final T5 fifth;

  /**
   * Constructor for Quintet.
   *
   * @param first a T1 object
   * @param second a T2 object
   * @param third a T3 object
   * @param fourth a T4 object
   * @param fifth a T5 object
   */
  public Quintet(T1 first, T2 second, T3 third, T4 fourth, T5 fifth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
    this.fifth = fifth;
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

  /**
   * The fifth element of the tuple
   *
   * @return The fith element
   */
  public T5 fifth() {
    return fifth;
  }

  /** {@inheritDoc} */
  @Override
  public int cardinality() {
    return 5;
  }

  /** {@inheritDoc} */
  @Override
  public @NotNull Optional<Object> at(int index) {
    return switch (index) {
      case 0 -> Optional.of(this.first);
      case 1 -> Optional.of(this.second);
      case 2 -> Optional.of(this.third);
      case 3 -> Optional.of(this.fourth);
      case 4 -> Optional.of(this.fifth);
      default -> Optional.empty();
    };
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(first, second, third, fourth, fifth);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "Quintet["
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
        + ", "
        + "fifth="
        + fifth
        + ']';
  }
}
