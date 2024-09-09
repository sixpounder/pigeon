package org.storynode.pigeon;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A type representing the outcome of some operation, which value can be some value or some error
 * but not neither nor both at the same time.
 *
 * <h1>Examples</h1>
 * Observe the outcome of a function
 *
 * <pre>
 * Result.ok("Hello world") // will be ok with value "Hello world"
 * Result.error(45D) // will yield an error with type Double and value 45
 * Result.of(() -> "Hello world") // will be ok with value "Hello world"
 * Result.of(() -> 8 / 0) // will yield a result with an error
 * </pre>
 *
 * @param <T> The type of the ok value
 * @param <E> The type of the error value
 * @author Andrea Coronese
 * @since 1.0.0
 */
public class Result<T, E> {
  private final T inner;
  private final E error;

  /**
   * Constructs a new {@link Result} with the given couple of values. These values must be mutually
   * exclusively non-null, and at least one of them must be non-null.
   *
   * @param inner The value of the ok result
   * @param error The value of the error
   * @throws IllegalArgumentException if either both values are null or both values are non-null
   */
  private Result(T inner, E error) {
    if (inner != null ^ error != null) {
      this.inner = inner;
      this.error = error;
    } else {
      throw new IllegalArgumentException("Inner value and error cannot be both null or non null");
    }
  }

  /**
   * Constructs an <i>ok</i> variant of a {@link Result}
   *
   * @param inner The value of the result for the <i>ok</i> state
   * @return The constructed result
   * @param <T> The type of the <i>ok</i> value
   * @param <E> The type of the <i>error</i> value
   */
  @Contract(value = "_ -> new", pure = true)
  public static <T, E> @NotNull Result<T, E> ok(@NotNull T inner) {
    return new Result<>(inner, null);
  }

  /**
   * Constructs an <i>error</i> variant of a {@link Result}
   *
   * @param error The value of the result for the <i>error</i> state
   * @return The constructed result
   * @param <T> The type of the <i>ok</i> value
   * @param <E> The type of the <i>error</i> value
   */
  @Contract(value = "_ -> new", pure = true)
  public static <T, E> @NotNull Result<T, E> error(@NotNull E error) {
    return new Result<>(null, error);
  }

  /**
   * Constructs a new {@link Result} by using the provided function return value. If the
   * supplier completes exceptionally, the {@link Result} will contain the caught exception
   * as error
   * @param fn The function to execute to obtain the value of the result
   * @return A {@link Result} with a value or an error, depending on the function execution
   * @param <T> The type of the contained value
   */
  public static <T> @NotNull Result<T, ? extends Throwable> of(Supplier<T> fn) {
    try {
      return Result.ok(fn.get());
    } catch (Throwable throwable) {
      return Result.error(throwable);
    }
  }

  /**
   * Whether this {@link Result} is <i>ok</i>
   *
   * @return <code>true</code> if this contains an ok value, <code>false</code> if it contains an
   *     error
   */
  public boolean isOk() {
    return this.inner != null;
  }

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
   * Whether this {@link Result} is an <i>error</i>
   *
   * @return <code>true</code> if this contains an error, <code>false</code> if it contains a value
   */
  public boolean isError() {
    return this.error != null;
  }

  /**
   * Returns <code>true</code> if the result contains an error and that error satisfies a <code>
   * predicate</code>
   *
   * @param predicate The predicate to satisfy
   * @return If the error is present and satisfies the predicate
   */
  public boolean isErrorAnd(Predicate<E> predicate) {
    return this.isError() && predicate.test(this.unwrapError());
  }

  /**
   * Unwraps and return the inner value, if present. Throws an error if this result contains an
   * error. If you need a non throwing version of this method use {@link Result#tryUnwrap()}
   *
   * @return The inner value
   * @throws UnwrapException if this contains an error
   */
  public T unwrap() {
    if (this.isError()) {
      throw new UnwrapException("Cannot unwrap: not an ok value");
    }

    return this.inner;
  }

  /**
   * Unwraps and return the inner error, if present. Throws an error if this result contains a
   * value. If you need a non throwing version of this method use {@link Result#tryUnwrapError()}
   *
   * @return The inner error
   * @throws UnwrapException if this contains a value
   */
  public E unwrapError() {
    if (this.isOk()) {
      throw new UnwrapException("Cannot unwrap error: not an error value");
    }

    return this.error;
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
   * Unwraps the contained value, or returns a default one if this contains an error
   *
   * @param defaultValueSupplier The supplier for the value to return in place of the error
   * @return The contained value or the default one, depending on which is appropriate
   */
  public T orElseGet(Supplier<T> defaultValueSupplier) {
    return tryUnwrap().orElseGet(defaultValueSupplier);
  }

  /**
   * The non-throwing variant of {@link Result#unwrap()}. This is guaranteed to never throw and to
   * always return a non-null value.
   *
   * @return An {@link Optional} containing the value, or empty if there is none
   */
  public @NotNull Optional<T> tryUnwrap() {
    return Optional.ofNullable(this.inner);
  }

  /**
   * The non-throwing variant of {@link Result#unwrapError()}. This is guaranteed to never throw and
   * to always return a non-null value.
   *
   * @return An {@link Optional} containing the error, or empty if there is none
   */
  public @NotNull Optional<E> tryUnwrapError() {
    return Optional.ofNullable(this.error);
  }

  /**
   * Maps a <code>Result&lt;T, E&gt;</code> to <code>Result&lt;U, E&gt;</code> by applying a
   * function to a contained value, leaving a result that contains an error untouched. <br>
   * This can be used to compose the result of two functions.
   *
   * @param fn The function to apply to the value
   * @return The mapped {@link Result}
   * @param <U> The type of the new value
   */
  public <U> Result<U, E> map(Function<T, U> fn) {
    if (this.isOk()) {
      return Result.ok(fn.apply(this.unwrap()));
    } else {
      return Result.error(this.unwrapError());
    }
  }

  /**
   * Maps a <code>Result&lt;T, E&gt;</code> to <code>Result&lt;T, U&gt;</code> by applying a
   * function to a contained error, leaving a result that contains a value untouched. <br>
   * This can be used to compose the result of two functions
   *
   * @param fn The function to apply to the error
   * @return The mapped {@link Result}
   * @param <U> The type of the new error
   */
  public <U> Result<T, U> mapError(Function<E, U> fn) {
    if (this.isError()) {
      return Result.error(fn.apply(this.unwrapError()));
    } else {
      return Result.ok(this.unwrap());
    }
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }

    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    Result<?, ?> result = (Result<?, ?>) other;
    return Objects.equals(inner, result.inner) && Objects.equals(error, result.error);
  }
}
