package org.storynode.pigeon.result;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.option.Option;
import org.storynode.pigeon.protocol.SafelyWrapped;
import org.storynode.pigeon.protocol.ThrowingSupplier;
import org.storynode.pigeon.protocol.Wrapped;
import org.storynode.pigeon.tuple.Pair;

/**
 * A type representing the outcome of some operation, which value can be some value or some error
 * but not neither nor both at the same time.
 *
 * <h2>Examples</h2>
 *
 * <h3>Known variant construction</h3>
 *
 * An {@link org.storynode.pigeon.result.Ok} result with the value "Hello world"
 *
 * <pre>
 * {@code Result.ok("Hello world")}
 * </pre>
 *
 * An {@link org.storynode.pigeon.result.Err} result with the error value set to 45D
 *
 * <pre>
 * {@code Result.err(45D)}
 * </pre>
 *
 * <h3>Construct by execution</h3>
 *
 * Construct a result with the outcome of a http request
 *
 * <pre>
 * {@code Result.of(() -> httpGet("https://www.wikipedia.com")).map(Response:getBody)}
 * </pre>
 *
 * This would result in an {@link org.storynode.pigeon.result.Err} result with its error value set
 * to the instance of the exception caught when running the provided function
 *
 * <pre>
 * {@code Result.of(() -> 8 / 0)}
 * </pre>
 *
 * @param <T> The type of the ok value
 * @param <E> The type of the error value
 * @author Andrea Coronese
 * @since 1.0.0
 */
public abstract class Result<T, E> implements SafelyWrapped<T> {

  /**
   * Constructs an ok variant of a {@link org.storynode.pigeon.result.Result}.
   *
   * @param inner The value of the result for the ok state
   * @return The constructed result
   * @param <T> The type of the ok value
   * @param <E> The type of the error value
   */
  @Contract(value = "_ -> new", pure = true)
  public static <T, E> @NotNull Result<T, E> ok(@NotNull T inner) {
    return new Ok<>(Objects.requireNonNull(inner));
  }

  /**
   * Constructs an error variant of a {@link org.storynode.pigeon.result.Result}
   *
   * @param error The value of the result for the error state
   * @return The constructed result
   * @param <T> The type of the ok value
   * @param <E> The type of the error value
   */
  @Contract(value = "_ -> new", pure = true)
  public static <T, E> @NotNull Result<T, E> err(@NotNull E error) {
    return new Err<>(Objects.requireNonNull(error));
  }

  /**
   * Constructs an error variant of a {@link org.storynode.pigeon.result.Result}
   *
   * @param error The value of the result for the error state
   * @return The constructed result
   * @param <T> The type of the ok value
   * @param <E> The type of the error value
   */
  @Contract(value = "_ -> new", pure = true)
  public static <T, E> @NotNull Result<T, E> error(@NotNull E error) {
    return err(error);
  }

  /**
   * Constructs a new {@link org.storynode.pigeon.result.Result} by using the provided function
   * return value. If the supplier completes exceptionally, the {@link
   * org.storynode.pigeon.result.Result} will contain the caught exception as error
   *
   * @param fn The function to execute to obtain the value of the result
   * @return A {@link org.storynode.pigeon.result.Result} with a value or an error, depending on the
   *     function execution
   * @param <T> The type of the contained value
   */
  public static <T> @NotNull Result<T, ? extends Throwable> of(ThrowingSupplier<T> fn) {
    try {
      return Result.ok(fn.getWithException());
    } catch (Throwable throwable) {
      return Result.error(throwable);
    }
  }

  /**
   * Whether this {@link org.storynode.pigeon.result.Result} is ok, meaning it contains a value and
   * not an error
   *
   * @return <code>true</code> if this contains an ok value, <code>false</code> if it contains an
   *     error
   */
  public abstract boolean isOk();

  /**
   * Unwraps and return the inner value, if present. Throws an error if this result contains an
   * error. If you need a non throwing version of this method use {@link #tryUnwrap()}
   *
   * @return The inner value
   * @throws org.storynode.pigeon.error.UnwrapException if this contains an error
   */
  public abstract T unwrap() throws UnwrapException;

  /**
   * Unwraps and return the inner error, if present. Throws an error if this result contains a
   * value. If you need a non throwing version of this method use [#tryUnwrapError()]
   *
   * @return The inner error
   */
  public abstract E unwrapError();

  /**
   * Unwraps the contained value, or returns a default one if this contains an error
   *
   * @param defaultValueSupplier The supplier for the value to return in place of the error
   * @return The contained value or the default one, depending on which is appropriate
   */
  public abstract T orElseGet(Supplier<T> defaultValueSupplier);

  /**
   * {@inheritDoc}
   *
   * <p>The non-throwing variant of {@link Wrapped#unwrap()}. This is guaranteed to never throw and
   * to always return a non-null value.
   */
  @Override
  public abstract @NotNull Option<T> tryUnwrap();

  /**
   * The non-throwing variant of {@link #unwrapError()}. This is guaranteed to never throw and to
   * always return a non-null value.
   *
   * @return An {@link org.storynode.pigeon.option.Option} containing the error, or empty if there
   *     is none
   */
  public abstract @NotNull Option<E> tryUnwrapError();

  /**
   * Maps a <code>Result&lt;T, E&gt;</code> to <code>Result&lt;U, E&gt;</code> by applying a
   * function to a contained value, leaving a result that contains an error untouched. This can be
   * used to compose the result of two functions.
   *
   * <h4>Example</h4>
   *
   * <pre>
   * {@code Result.ok(2).map(Math:sqrt) // Gets the square root of two as a Result}
   * </pre>
   *
   * @param fn The function to apply to the value
   * @return The mapped {@link org.storynode.pigeon.result.Result}
   * @param <U> The type of the new value
   */
  public abstract <U> Result<U, E> map(@NotNull Function<? super T, ? extends U> fn);

  /**
   * Like {@link Option#map(Function)} but does not re-wrap the result of the provided mapping
   * function to a {@link Result}
   *
   * @param fn The mapping function to apply
   * @return The mapped value
   * @param <U> The type of the mapped {@link Ok} value
   */
  public abstract <U> Result<U, E> flatMap(@NotNull Function<? super T, ? extends Result<U, E>> fn);

  /**
   * Maps a <code>Result&lt;T, E&gt;</code> to <code>Result&lt;T, U&gt;</code> by applying a
   * function to a contained error, leaving a result that contains a value untouched. This can be
   * used to compose the result of two functions
   *
   * @param fn The function to apply to the error
   * @return The mapped {@link org.storynode.pigeon.result.Result}
   * @param <U> The type of the new error
   */
  public abstract <U> Result<T, U> mapError(Function<? super E, ? extends U> fn);

  /**
   * Executes <code>whenOk</code> if this contains a value, <code>whenError</code> otherwise.
   *
   * @param whenOk The function to execute when this contains a value
   * @param whenError The function to execute when this contains an error
   * @return this instance (for chaining)
   */
  public abstract Result<T, E> ifOkOrElse(Consumer<T> whenOk, Consumer<E> whenError);

  /**
   * Returns <code>true</code> if the result contains a value and that value satisfies a <code>
   * predicate</code>
   *
   * @param predicate The predicate to satisfy
   * @return If the value is present and satisfies the predicate
   */
  public boolean isOkAnd(Predicate<T> predicate) {
    return this.isOk() && predicate.test(this.unwrap());
  }

  /**
   * Whether this {@link org.storynode.pigeon.result.Result} is an error
   *
   * @return <code>true</code> if this contains an error, <code>false</code> if it contains a value
   */
  public boolean isErr() {
    return !this.isOk();
  }

  /**
   * Returns <code>true</code> if the result contains an error and that error satisfies a <code>
   * predicate</code>
   *
   * @param predicate The predicate to satisfy
   * @return If the error is present and satisfies the predicate
   */
  public boolean isErrAnd(Predicate<E> predicate) {
    return this.isErr() && predicate.test(this.unwrapError());
  }

  /**
   * Unwraps the contained value, or returns a default one if this contains an error
   *
   * @param defaultValue The value to return in place of the error
   * @return The contained value or the default one, depending on which is appropriate
   */
  public T orElse(T defaultValue) {
    return tryUnwrap().orElse(defaultValue);
  }

  /**
   * Executes the provided function if this contains a value
   *
   * @param whenOk The function to execute
   * @return this instance (for chaining)
   */
  public Result<T, E> ifOk(Consumer<T> whenOk) {
    return this.ifOkOrElse(whenOk, null);
  }

  /**
   * Executes the provided function if this contains an error
   *
   * @param whenError The function to execute
   * @return this instance (for chaining)
   */
  public Result<T, E> ifError(Consumer<E> whenError) {
    return this.ifOkOrElse(null, whenError);
  }

  /**
   * Returns {@code res} if the result is {@code Ok}, otherwise returns the Err value of {@code
   * this}. <br>
   * Arguments passed to {@code and} are eagerly evaluated; if you are passing the result of a
   * function call, it is recommended to use {@link Result#andThen(Function)}, which is lazily
   * evaluated.
   *
   * @param res The other result
   * @return {@code res} if this is {@code Ok}, this error otherwise
   * @param <U> The type of the other result (if {@code Ok})
   * @see Result#andThen(Function)
   */
  public abstract <U> Result<U, E> and(Result<U, E> res);

  /**
   * Like {@link Result#and(Result)} but lazily evaluated. The function is supplied the current
   * value of the result if it's {@code Ok}
   *
   * @param res The other result
   * @return {@code res} if this is {@code Ok}, this error otherwise
   * @param <U> The type of the other result (if {@code Ok})
   * @see Result#and(Result)
   */
  public abstract <U> Result<U, E> andThen(Function<T, Result<U, E>> res);

  /**
   * Converts this result to a {@link org.storynode.pigeon.tuple.Pair} tuple having the value and
   * the error as fields
   *
   * @return The constructed tuple
   */
  public Pair<T, E> toTuple() {
    if (this.isOk()) {
      return new Pair<>(unwrap(), null);
    } else {
      return new Pair<>(null, unwrapError());
    }
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    Result<?, ?> otherResult = (Result<?, ?>) other;
    return (otherResult.isOk() && isOk() && unwrap().equals(otherResult.unwrap()))
        || (otherResult.isErr() && isErr() && unwrapError().equals(otherResult.unwrapError()));
  }
}
