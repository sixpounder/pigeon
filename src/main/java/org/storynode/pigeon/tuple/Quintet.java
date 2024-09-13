package org.storynode.pigeon.tuple;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class Quintet<T1, T2, T3, T4, T5> extends Tuple {
  private final T1 first;
  private final T2 second;
  private final T3 third;
  private final T4 fourth;
  private final T5 fifth;

  public Quintet(T1 first, T2 second, T3 third, T4 fourth, T5 fifth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
    this.fifth = fifth;
  }

  public T1 first() {
    return first;
  }

  public T2 second() {
    return second;
  }

  public T3 third() {
    return third;
  }

  public T4 fourth() {
    return fourth;
  }

  public T5 fifth() {
    return fifth;
  }

  @Override
  public int cardinality() {
    return 5;
  }

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

  @Override
  public int hashCode() {
    return Objects.hash(first, second, third, fourth, fifth);
  }

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
