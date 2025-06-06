package org.storynode.pigeon.option;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.protocol.SafelyWrapped;

/**
 * Describes a value that can be {@link org.storynode.pigeon.option.Some} value or {@link
 * org.storynode.pigeon.option.None}.
 *
 * <h2>Why this and not {@link java.util.Optional}?</h2>
 *
 * <p>This implementation aims to be a - slightly - faster implementation of {@link
 * java.util.Optional}, ditching all checks that comes at every method call that needs to assess if
 * the value is empty or not before doing anything, in favour of fixed implementations.
 *
 * <p>For example: when calling {@link org.storynode.pigeon.option.Option#map(Function)} it doesn't
 * need to check if this is empty or not because {@link org.storynode.pigeon.option.Option} itself
 * is abstract, and its inheritors ({@link org.storynode.pigeon.option.Some} and {@link
 * org.storynode.pigeon.option.None}) already know if they must execute the mapping function or not.
 *
 * <p>The same applies to a variety of other methods, so for an intensive of this type of construct
 * using {@link org.storynode.pigeon.option.Option} instead of {@link java.util.Optional} can avoid
 * ** a lot** of condition checks (potentially).
 *
 * @param <T>
 * @author Andrea Coronese
 */
public abstract class Option<T> implements SafelyWrapped<T> {
  /**
   * Creates a new {@link org.storynode.pigeon.option.Option} with some value in it. If the provided
   * value is null then the option will be empty ({@link org.storynode.pigeon.option.None}).
   *
   * @param value The value to wrap
   * @return The created {@link org.storynode.pigeon.option.Option}
   * @param <T> The type of the inner value
   */
  @Contract(value = "_ -> new", pure = true)
  public static <T> @NotNull Option<T> some(@NotNull T value) {
    return of(value);
  }

  /**
   * Creates a new {@link org.storynode.pigeon.option.Option} with some value in it. If the provided
   * value is null then the option will be empty ({@link org.storynode.pigeon.option.None}).
   *
   * @param value The value to wrap
   * @return The created {@link org.storynode.pigeon.option.Option}
   * @param <T> The type of the inner value
   */
  public static <T> @NotNull Option<T> of(T value) {
    if (value == null) {
      return Option.none();
    } else {
      return new Some<>(value);
    }
  }

  /**
   * Creates a new {@link org.storynode.pigeon.option.Option} with some value in it. If the provided
   * value is null then the option will be empty ({@link org.storynode.pigeon.option.None}).
   *
   * @param valueSupplier The supplier for the value to wrap
   * @return The created {@link org.storynode.pigeon.option.Option}
   * @param <T> The type of the inner value
   */
  public static <T> @NotNull Option<T> of(@NotNull Supplier<T> valueSupplier) {
    return Option.of(valueSupplier.get());
  }

  /**
   * Creates an empty ({@code None}) {@code Option}
   *
   * @param <T> a T class
   * @return a {@link org.storynode.pigeon.option.None} object
   */
  @SuppressWarnings("unchecked")
  public static <T> @NotNull None<T> none() {
    return (None<T>) None.INSTANCE;
  }

  /**
   * Whether this option contains a value or not
   *
   * @return <code>true</code> if this contains a value, <code>false</code> otherwise
   * @see Option#isNone()
   */
  public abstract boolean isSome();

  /**
   * Whether this option contains a value or not
   *
   * @return <code>false</code> if this contains a value, <code>true</code> otherwise
   * @see Option#isSome()
   */
  public boolean isNone() {
    return !isSome();
  }

  /**
   * Executes <code>func</code> if this is {@link org.storynode.pigeon.option.Some}, consuming the
   * contained value.
   *
   * @param func The function to execute
   */
  public abstract void ifPresent(Consumer<T> func);

  /**
   * Executes <code>whenPresent</code> if this is {@link org.storynode.pigeon.option.Some},
   * consuming the contained value, or runs <code>otherwise</code> if this is {@link
   * org.storynode.pigeon.option.None}
   *
   * @param whenPresent The function to execute if this is {@link org.storynode.pigeon.option.Some}
   * @param otherwise The function to execute if this is {@link org.storynode.pigeon.option.None}
   */
  public abstract void ifPresentOrElse(Consumer<T> whenPresent, Runnable otherwise);

  /**
   * If a value is present, and the value matches the given predicate, returns an {@code Option}
   * describing the value, otherwise returns an empty {@code Option}.
   *
   * @param predicate the predicate to apply to a value, if present
   * @return an {@code Option} describing the value of this {@code Option}, if a value is present
   *     and the value matches the given predicate, otherwise an empty {@code Option}
   * @throws java.lang.NullPointerException if the predicate is {@code null}
   */
  public abstract Option<T> filter(Predicate<? super T> predicate);

  /**
   * If a value is present, returns an {@code Option} describing (as if by {@link #some}) the result
   * of applying the given mapping function to the value, otherwise returns an empty {@code Option}.
   *
   * <p>If the mapping function returns a {@code null} result then this method returns an empty
   * {@code Option}.
   *
   * @param mapper the mapping function to apply to a value, if present
   * @param <U> The type of the value returned from the mapping function
   * @return an {@code Option} describing the result of applying a mapping function to the value of
   *     this {@code Option}, if a value is present, otherwise an empty {@code Option}
   * @throws java.lang.NullPointerException if the mapping function is {@code null}
   */
  public abstract <U> Option<U> map(@NotNull Function<T, U> mapper);

  /**
   * If a value is present, returns the result of applying the given {@code Optional}-bearing
   * mapping function to the value, otherwise returns an empty {@code Optional}.
   *
   * <p>This method is similar to {@link #map(Function)}, but the mapping function is one whose
   * result is already an {@code Optional}, and if invoked, {@code flatMap} does not wrap it within
   * an additional {@code Optional}.
   *
   * @param <U> The type of value of the {@code Optional} returned by the mapping function
   * @param mapper the mapping function to apply to a value, if present
   * @return the result of applying an {@code Optional}-bearing mapping function to the value of
   *     this {@code Optional}, if a value is present, otherwise an empty {@code Optional}
   */
  public abstract <U> Option<U> flatMap(Function<? super T, ? extends Option<? extends U>> mapper);

  /**
   * If a value is present, returns an {@link org.storynode.pigeon.option.Option} describing the
   * value, otherwise returns an {@link org.storynode.pigeon.option.Option} produced by the given
   * supplying function
   *
   * @param supplier The supplier that produces the {@link org.storynode.pigeon.option.Option} in
   *     case a value is not present in <code>this</code>
   * @return An {@link org.storynode.pigeon.option.Option} with the value of <code>this</code>, if
   *     any is present, or another {@link org.storynode.pigeon.option.Option} supplied by the
   *     provided supplying function
   */
  public abstract Option<T> or(@NotNull Supplier<? extends Option<T>> supplier);

  /**
   * If a value is present, returns the value, otherwise returns the result produced by the
   * supplying function.
   *
   * @param supplier the supplying function that produces a value to be returned
   * @return the value, if present, otherwise the result produced by the supplying function
   * @throws java.lang.NullPointerException if no value is present and the supplying function is
   *     {@code null}
   */
  public abstract T orElseGet(@NotNull Supplier<T> supplier);

  /**
   * If a value is present, returns the value, otherwise returns {@code other}.
   *
   * @param other the value to be returned, if no value is present. May be {@code null}.
   * @return the value, if present, otherwise {@code other}
   */
  public abstract T orElse(T other);

  /**
   * If a value is present returns that value, otherwise throws {@link
   * java.util.NoSuchElementException}. <br>
   *
   * <p>Please note that this is mainly for API compatibility with {@link java.util.Optional} but
   * should be avoided in favour of {@link org.storynode.pigeon.option.Option#orElse(Object)} and
   * {@link org.storynode.pigeon.option.Option#orElseGet(Supplier)}
   *
   * @return The contained value, if present
   * @throws java.util.NoSuchElementException When the option is {@link
   *     org.storynode.pigeon.option.None}
   */
  public abstract T orElseThrow() throws NoSuchElementException;

  /**
   * If a value is present returns that value, otherwise throws the supplied {@link
   * java.lang.Throwable}. <br>
   *
   * <p>Please note that this is mainly for API compatibility with {@link java.util.Optional} but
   * should be avoided in favour of {@link org.storynode.pigeon.option.Option#orElse(Object)} and
   * {@link org.storynode.pigeon.option.Option#orElseGet(Supplier)}
   *
   * @param throwable The function that supplies the exception to throw
   * @param <E> The concrete type of the {@link java.lang.Throwable}
   * @return a T object
   * @throws E if any.
   */
  public abstract <E extends Throwable> T orElseThrow(@NotNull Supplier<E> throwable) throws E;

  /**
   * Streams the contained value, if any.
   *
   * @return a {@link java.util.stream.Stream} object
   */
  public abstract Stream<T> stream();
}
