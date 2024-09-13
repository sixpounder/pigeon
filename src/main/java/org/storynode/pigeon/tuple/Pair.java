package org.storynode.pigeon.tuple;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public final class Pair<T1, T2> extends Tuple {
  private final T1 first;
  private final T2 second;

  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  @Override
  public int cardinality() {
    return 2;
  }

  @Override
  public @NotNull Optional<Object> at(int index) {
    return switch (index) {
      case 0 -> Optional.of(this.first);
      case 1 -> Optional.of(this.second);
      default -> Optional.empty();
    };
  }

  public T1 first() {
    return first;
  }

  public T2 second() {
    return second;
  }

  @Override
  public int hashCode() {
    return Objects.hash(first, second);
  }

  @Override
  public String toString() {
    return "Pair[" + "first=" + first + ", " + "second=" + second + ']';
  }
}
