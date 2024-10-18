package org.storynode.pigeon.result;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;

/**
 * {@link org.storynode.pigeon.result.Result} variant for errors.
 *
 * @see Result
 * @see Ok
 * @author Andrea Coronese
 */
public class Err<T, E> extends Result<T, E> {
  private final E error;

  /**
   * A variant of {@link Result} that indicates an error value
   *
   * @param error The value for the 'error' result
   */
  public Err(@NotNull E error) {
    this.error = error;
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
    return error;
  }

  /** {@inheritDoc} */
  @Override
  public T orElseGet(@NotNull Supplier<T> defaultValueSupplier) {
    return defaultValueSupplier.get();
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> map(@NotNull Function<? super T, ? extends U> fn) {
    return Result.error(error);
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<U, E>> fn) {
    return Result.err(error);
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<T, U> mapError(@NotNull Function<? super E, ? extends U> fn) {
    return Result.error(fn.apply(error));
  }

  /** {@inheritDoc} */
  @Override
  public Result<T, E> ifOkOrElse(Consumer<T> whenOk, @NotNull Consumer<E> whenError) {
    whenError.accept(this.unwrapError());
    return this;
  }

  @Override
  public T orElse(T defaultValue) {
    return defaultValue;
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> and(Result<U, E> res) {
    return (Result<U, E>) this;
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> andThen(Function<T, Result<U, E>> res) {
    return (Result<U, E>) this;
  }
}
