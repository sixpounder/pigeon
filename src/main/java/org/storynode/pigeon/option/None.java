package org.storynode.pigeon.option;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;

/**
 * Represents an {@link org.storynode.pigeon.option.Option} with no value
 *
 * @see Option
 * @author Andrea Coronese
 */
public class None<T> extends Option<T> {

  /** Creates an option with no value in it */
  protected None() {
    super();
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    return obj instanceof None<?>;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return Objects.hash();
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return "None";
  }

  /** {@inheritDoc} */
  @Override
  public boolean isSome() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public void ifPresent(Consumer<T> func) {}

  /** {@inheritDoc} */
  @Override
  public void ifPresentOrElse(Consumer<T> whenPresent, @NotNull Runnable otherwise) {
    otherwise.run();
  }

  /** {@inheritDoc} */
  @Override
  public <U> Option<U> map(@NotNull Function<T, U> mapper) {
    return Option.none();
  }

  /** {@inheritDoc} */
  @Override
  public <U> Option<U> flatMap(Function<? super T, ? extends Option<? extends U>> mapper) {
    return none();
  }

  @Override
  public Option<? extends T> or(@NotNull Supplier<? extends Option<? extends T>> supplier) {
    return supplier.get();
  }

  /** {@inheritDoc} */
  @Override
  public T orElseGet(@NotNull Supplier<T> supplier) {
    return supplier.get();
  }

  /** {@inheritDoc} */
  @Override
  public T orElse(T other) {
    return other;
  }

  /** {@inheritDoc} */
  @Override
  public T orElseThrow() throws NoSuchElementException {
    throw new NoSuchElementException("No value present");
  }

  /** {@inheritDoc} */
  @Override
  public <E extends Throwable> T orElseThrow(@NotNull Supplier<E> throwable) throws E {
    throw throwable.get();
  }

  /** {@inheritDoc} */
  @Override
  public Stream<T> stream() {
    return Stream.empty();
  }

  /** {@inheritDoc} */
  @Override
  public T unwrap() throws UnwrapException {
    return null;
  }
}
