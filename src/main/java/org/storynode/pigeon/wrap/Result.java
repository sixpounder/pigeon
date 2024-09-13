package org.storynode.pigeon.wrap;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.tuple.Pair;

/// A type representing the outcome of some operation, which value can be some value or some error
/// but not neither nor both at the same time.
///
/// # Examples
///
/// ### Known variant construction
///
/// An ok result with the value "Hello world"
///
/// ```java
/// Result.ok("Hello world")
/// ```
///
/// An error result with the error value set to 45D
/// ```java
/// Result.error(45D)
/// ```
///
/// ### Construct by execution
///
/// Construct a result with the outcome of a http request
///
/// ```java
/// Result.of(() -> httpGet("https://www.wikipedia.com")).map(Response:getBody)
/// ```
///
/// This would result in an error result with its error value set to the instance of the exception
/// caught when running the provided function
///
/// ```java
/// Result.of(() -> 8 / 0)
/// ```
///
/// @param <T> The type of the ok value
/// @param <E> The type of the error value
/// @author Andrea Coronese
/// @since 1.0.0
public class Result<T, E> implements Wrapped<T> {
  private final T inner;
  private final E error;

  /// Constructs a new [Result] with the given couple of values. These values must be mutually
  /// exclusively non-null, and at least one of them must be non-null.
  ///
  /// @param inner The value of the ok result
  /// @param error The value of the error
  /// @throws IllegalArgumentException if either both values are null or both values are non-null
  private Result(T inner, E error) {
    if (inner != null ^ error != null) {
      this.inner = inner;
      this.error = error;
    } else {
      throw new IllegalArgumentException("Inner value and error cannot be both null or non null");
    }
  }

  /// Constructs an _ok_ variant of a [Result]
  ///
  /// @param inner The value of the result for the _ok_ state
  /// @return The constructed result
  /// @param <T> The type of the _ok_ value
  /// @param <E> The type of the _error_ value
  @Contract(value = "_ -> new", pure = true)
  public static <T, E> @NotNull Result<T, E> ok(@NotNull T inner) {
    return new Result<>(Objects.requireNonNull(inner), null);
  }

  /// Constructs an _error_ variant of a [Result]
  ///
  /// @param error The value of the result for the _error_ state
  /// @return The constructed result
  /// @param <T> The type of the _ok_ value
  /// @param <E> The type of the _error_ value
  @Contract(value = "_ -> new", pure = true)
  public static <T, E> @NotNull Result<T, E> error(@NotNull E error) {
    return new Result<>(null, Objects.requireNonNull(error));
  }

  /// Constructs a new [Result] by using the provided function return value. If the supplier
  /// completes exceptionally, the [Result] will contain the caught exception as error
  ///
  /// @param fn The function to execute to obtain the value of the result
  /// @return A [Result] with a value or an error, depending on the function execution
  /// @param <T> The type of the contained value
  public static <T> @NotNull Result<T, ? extends Throwable> of(Supplier<T> fn) {
    try {
      return Result.ok(fn.get());
    } catch (Throwable throwable) {
      return Result.error(throwable);
    }
  }

  /// Whether this [Result] is _ok_
  ///
  /// @return <code>true</code> if this contains an ok value, <code>false</code> if it contains an
  ///     error
  public boolean isOk() {
    return this.inner != null;
  }

  /// Returns <code>true</code> if the result contains a value and that value satisfies a <code>
  /// predicate</code>
  ///
  /// @param predicate The predicate to satisfy
  /// @return If the value is present and satisfies the predicate
  public boolean isOkAnd(Predicate<T> predicate) {
    return this.isOk() && predicate.test(this.unwrap());
  }

  /// Whether this [Result] is an _error_
  ///
  /// @return <code>true</code> if this contains an error, <code>false</code> if it contains a value
  public boolean isError() {
    return this.error != null;
  }

  /// Returns <code>true</code> if the result contains an error and that error satisfies a <code>
  /// predicate</code>
  ///
  /// @param predicate The predicate to satisfy
  /// @return If the error is present and satisfies the predicate
  public boolean isErrorAnd(Predicate<E> predicate) {
    return this.isError() && predicate.test(this.unwrapError());
  }

  /// Unwraps and return the inner value, if present. Throws an error if this result contains an
  /// error. If you need a non throwing version of this method use [#tryUnwrap()]
  ///
  /// @return The inner value
  /// @throws UnwrapException if this contains an error
  public T unwrap() {
    if (this.isError()) {
      throw new UnwrapException("Cannot unwrap: not an ok value");
    }

    return this.inner;
  }

  /// Unwraps and return the inner error, if present. Throws an error if this result contains a
  /// value. If you need a non throwing version of this method use [#tryUnwrapError()]
  ///
  /// @return The inner error
  /// @throws UnwrapException if this contains a value
  public E unwrapError() {
    if (this.isOk()) {
      throw new UnwrapException("Cannot unwrap error: not an error value");
    }

    return this.error;
  }

  /// Unwraps the contained value, or returns a default one if this contains an error
  ///
  /// @param defaultValue The value to return in place of the error
  /// @return The contained value or the default one, depending on which is appropriate
  public T orElse(T defaultValue) {
    return tryUnwrap().orElse(defaultValue);
  }

  /// Unwraps the contained value, or returns a default one if this contains an error
  ///
  /// @param defaultValueSupplier The supplier for the value to return in place of the error
  /// @return The contained value or the default one, depending on which is appropriate
  public T orElseGet(Supplier<T> defaultValueSupplier) {
    return tryUnwrap().orElseGet(defaultValueSupplier);
  }

  /// The non-throwing variant of [#unwrap()]. This is guaranteed to never throw and to
  /// always return a non-null value.
  ///
  /// @return An [Optional] containing the value, or empty if there is none
  public @NotNull Optional<T> tryUnwrap() {
    return Optional.ofNullable(this.inner);
  }

  /// The non-throwing variant of [#unwrapError()]. This is guaranteed to never throw and
  /// to always return a non-null value.
  ///
  /// @return An [Optional] containing the error, or empty if there is none
  public @NotNull Optional<E> tryUnwrapError() {
    return Optional.ofNullable(this.error);
  }

  /// Maps a `Result<T, E>` to `Result<U, E>` by applying a
  /// function to a contained value, leaving a result that contains an error untouched.
  /// This can be used to compose the result of two functions.
  /// # Example
  ///
  /// ```java
  /// Result.ok(2).map(Math:sqrt) // Gets the square root of two as a Result
  /// ```
  ///
  /// @param fn The function to apply to the value
  /// @return The mapped [Result]
  /// @param <U> The type of the new value
  public <U> Result<U, E> map(Function<T, U> fn) {
    if (this.isOk()) {
      return Result.ok(fn.apply(this.unwrap()));
    } else {
      return Result.error(this.unwrapError());
    }
  }

  /// Maps a `Result<T, E>` to `Result<T, U>` by applying a
  /// function to a contained error, leaving a result that contains a value untouched.
  /// This can be used to compose the result of two functions
  ///
  /// @param fn The function to apply to the error
  /// @return The mapped [Result]
  /// @param <U> The type of the new error
  public <U> Result<T, U> mapError(Function<E, U> fn) {
    if (this.isError()) {
      return Result.error(fn.apply(this.unwrapError()));
    } else {
      return Result.ok(this.unwrap());
    }
  }

  /// Executes the provided function if this contains a value
  /// @param whenOk The function to execute
  /// @return this instance (for chaining)
  public Result<T, E> ifOk(Consumer<T> whenOk) {
    return this.ifOkOrElse(whenOk, null);
  }

  /// Executes the provided function if this contains an error
  /// @param whenError The function to execute
  /// @return this instance (for chaining)
  public Result<T, E> ifError(Consumer<E> whenError) {
    return this.ifOkOrElse(null, whenError);
  }

  /// Executes `whenOk` if this contains a value, `whenError` otherwise.
  /// @param whenOk The function to execute when this contains a value
  /// @param whenError The function to execute when this contains an error
  /// @return this instance (for chaining)
  public Result<T, E> ifOkOrElse(Consumer<T> whenOk, Consumer<E> whenError) {
    if (this.isOk() && whenOk != null) {
      whenOk.accept(this.unwrap());
    } else if (whenError != null) {
      whenError.accept(this.unwrapError());
    }

    return this;
  }

  /// Converts this result to a [Pair] tuple
  /// having the value and the error as fields
  /// @return The constructed tuple
  public Pair<T, E> toTuple() {
    return new Pair<>(inner, error);
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
