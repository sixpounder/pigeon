package org.storynode.pigeon.tuple;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class Triplet<T1, T2, T3> extends Tuple {
  private final T1 first;
  private final T2 second;
  private final T3 third;

  public Triplet(T1 first, T2 second, T3 third) {
    this.first = first;
    this.second = second;
    this.third = third;
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

  @Override
  public int cardinality() {
    return 3;
  }

  @Override
  public @NotNull Optional<Object> at(int index) {
    return switch (index) {
      case 0 -> Optional.of(this.first);
      case 1 -> Optional.of(this.second);
      case 2 -> Optional.of(this.third);
      default -> Optional.empty();
    };
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second, third);
  }

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
