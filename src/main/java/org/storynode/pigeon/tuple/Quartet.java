package org.storynode.pigeon.tuple;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class Quartet<T1, T2, T3, T4> extends Tuple {
  private final T1 first;
  private final T2 second;
  private final T3 third;
  private final T4 fourth;

  public Quartet(T1 first, T2 second, T3 third, T4 fourth) {
    this.first = first;
    this.second = second;
    this.third = third;
    this.fourth = fourth;
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

  @Override
  public int cardinality() {
    return 4;
  }

  @Override
  public @NotNull Optional<Object> at(int index) {
    return switch (index) {
      case 0 -> Optional.of(this.first);
      case 1 -> Optional.of(this.second);
      case 2 -> Optional.of(this.third);
      case 3 -> Optional.of(this.fourth);
      default -> Optional.empty();
    };
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second, third, fourth);
  }

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
