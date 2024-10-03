package org.storynode.pigeon.option;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link org.storynode.pigeon.option.Option} with some value in it.
 *
 * @see Option
 * @author Andrea Coronese
 */
public class Some<T> extends Option<T> {
  private final T value;

  /**
   * Creates an {@link org.storynode.pigeon.option.Option} that has a non-null value in it
   *
   * @param value The inner value
   */
  protected Some(@NonNull T value) {
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
    return Option.some(mapper.apply(value));
  }

  /** {@inheritDoc} */
  @Override
  public <U> Option<U> flatMap(@NotNull Function<? super T, ? extends Option<? extends U>> mapper) {
    return (Option<U>) mapper.apply(value);
  }

  @Override
  public Option<? extends T> or(@NotNull Supplier<? extends Option<? extends T>> supplier) {
    return this;
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

  /** {@inheritDoc} */
  @Override
  public T orElseThrow() {
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public <E extends Throwable> T orElseThrow(@NotNull Supplier<E> throwable) {
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public Stream<T> stream() {
    return Stream.of(value);
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
