package org.storynode.pigeon.result;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.option.Option;

/**
 * {@link org.storynode.pigeon.result.Result} variant for errors.
 *
 * @see Result
 * @see Ok
 * @author Andrea Coronese
 */
public class Err<T, E> extends Result<T, E> {
  private final E inner;

  /**
   * Constructor for Err.
   *
   * @param inner a E object
   */
  public Err(@NotNull E inner) {
    this.inner = inner;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOk() {
    return false;
  }

  /** {@inheritDoc} */
  @Override
  public T unwrap() throws UnwrapException {
    throw new UnwrapException("Cannot unwrap error on an Err<> value");
  }

  /** {@inheritDoc} */
  @Override
  public E unwrapError() {
    return inner;
  }

  /** {@inheritDoc} */
  @Override
  public T orElseGet(@NotNull Supplier<T> defaultValueSupplier) {
    return defaultValueSupplier.get();
  }

  /** {@inheritDoc} */
  @Override
  public @NotNull Option<T> tryUnwrap() {
    return Option.none();
  }

  /** {@inheritDoc} */
  @Override
  public @NotNull Option<E> tryUnwrapError() {
    return Option.some(inner);
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> map(Function<T, U> fn) {
    return Result.error(inner);
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<T, U> mapError(@NotNull Function<E, U> fn) {
    return Result.error(fn.apply(inner));
  }

  /** {@inheritDoc} */
  @Override
  public Result<T, E> ifOkOrElse(Consumer<T> whenOk, @NotNull Consumer<E> whenError) {
    whenError.accept(this.unwrapError());
    return this;
  }
}
