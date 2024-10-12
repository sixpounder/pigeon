package org.storynode.pigeon.result;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.option.Option;

/**
 * {@link org.storynode.pigeon.result.Result} variant for ok values.
 *
 * @see Err
 * @see Result
 * @author Andrea Coronese
 */
public class Ok<T, E> extends Result<T, E> {
  private final T value;

  /**
   * A variant of {@link Result} that indicates a success value
   *
   * @param value The value for the 'ok' result
   */
  protected Ok(@NotNull T value) {
    this.value = value;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOk() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public T unwrap() {
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public E unwrapError() throws UnwrapException {
    throw new UnwrapException("Cannot unwrap error on an Ok<> value");
  }

  /** {@inheritDoc} */
  @Override
  public T orElseGet(Supplier<T> defaultValueSupplier) {
    return value;
  }

  /** {@inheritDoc} */
  @Override
  public @NotNull Option<T> tryUnwrap() {
    return Option.some(value);
  }

  /** {@inheritDoc} */
  @Override
  public @NotNull Option<E> tryUnwrapError() {
    return Option.none();
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> map(@NotNull Function<? super T, ? extends U> fn) {
    return Result.ok(fn.apply(value));
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<U, E>> fn) {
    return fn.apply(value);
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<T, U> mapError(@NotNull Function<? super E, ? extends U> fn) {
    return Result.ok(value);
  }

  /** {@inheritDoc} */
  @Override
  public Result<T, E> ifOkOrElse(@NotNull Consumer<T> whenOk, Consumer<E> whenError) {
    whenOk.accept(this.unwrap());
    return this;
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> and(Result<U, E> res) {
    return res;
  }

  /** {@inheritDoc} */
  @Override
  public <U> Result<U, E> andThen(Function<T, Result<U, E>> res) {
    return and(res.apply(value));
  }
}
