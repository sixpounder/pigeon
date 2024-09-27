package org.storynode.pigeon.option;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * Some class.
 *
 * @author sixpounder
 */
public class Some<T> extends Option<T> {
  private final T value;

  /**
   * Creates an {@link Option} that has a non-null value in it
   *
   * @param value The inner value
   */
  public Some(@NonNull T value) {
    this.value = value;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSome() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public void ifPresent(@NotNull Consumer<T> func) {
    func.accept(unwrap());
  }

  /** {@inheritDoc} */
  @Override
  public void ifPresentOrElse(@NotNull Consumer<T> whenPresent, Runnable otherwise) {
    whenPresent.accept(unwrap());
  }

  /** {@inheritDoc} */
  @Override
  public <U> Option<U> map(@NotNull Function<T, U> mapper) {
    return Option.some(mapper.apply(unwrap()));
  }

  /** {@inheritDoc} */
  @Override
  public T orElseGet(@NotNull Supplier<T> supplier) {
    return unwrap();
  }

  /** {@inheritDoc} */
  @Override
  public T orElse(T other) {
    return unwrap();
  }

  /**
   * Returns the contained value
   *
   * @return The contained value
   * @see Option#unwrap()
   */
  public T value() {
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;

    return Objects.equals(this.value, ((Some<?>) obj).value);
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "Some[" + "value=" + value + ']';
  }

  /** {@inheritDoc} */
  @Override
  public T unwrap() {
    return value;
  }
}
