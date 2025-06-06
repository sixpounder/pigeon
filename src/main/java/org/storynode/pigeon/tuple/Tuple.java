package org.storynode.pigeon.tuple;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.Option;

/**
 * A tuple may be defined in many ways, but simply speaking it's a finite, heterogeneous sequence of
 * elements.
 *
 * @author Andrea Coronese
 * @since 1.0.0
 */
public abstract class Tuple {
  /**
   * Creates a {@link org.storynode.pigeon.tuple.Tuple} of two elements
   *
   * @param first The first element
   * @param second The second element
   * @param <T1> The type of the first element
   * @param <T2> The type of the second element
   * @return The {@link org.storynode.pigeon.tuple.Tuple}
   */
  @Contract("_, _ -> new")
  public static <T1, T2> @NotNull Pair<T1, T2> of(T1 first, T2 second) {
    return new Pair<>(first, second);
  }

  /**
   * Creates a {@link org.storynode.pigeon.tuple.Tuple} of three elements
   *
   * @param first The first element
   * @param second The second element
   * @param third The third element
   * @param <T1> The type of the first element
   * @param <T2> The type of the second element
   * @param <T3> The type of the third element
   * @return The [Tuple]
   */
  @Contract("_, _, _ -> new")
  public static <T1, T2, T3> @NotNull Triplet<T1, T2, T3> of(T1 first, T2 second, T3 third) {
    return new Triplet<>(first, second, third);
  }

  /**
   * Creates a {@link org.storynode.pigeon.tuple.Tuple} of four elements
   *
   * @param first The first element
   * @param second The second element
   * @param third The third element
   * @param fourth The fourth element
   * @param <T1> The type of the first element
   * @param <T2> The type of the second element
   * @param <T3> The type of the third element
   * @param <T4> The type of the fourth element
   * @return The {@link org.storynode.pigeon.tuple.Tuple}
   */
  @Contract("_, _, _, _ -> new")
  public static <T1, T2, T3, T4> @NotNull Quartet<T1, T2, T3, T4> of(
      T1 first, T2 second, T3 third, T4 fourth) {
    return new Quartet<>(first, second, third, fourth);
  }

  /**
   * Creates a {@link org.storynode.pigeon.tuple.Tuple} of five elements
   *
   * @param first The first element
   * @param second The second element
   * @param third The third element
   * @param fourth The fourth element
   * @param fifth The fifth element
   * @param <T1> The type of the first element
   * @param <T2> The type of the second element
   * @param <T3> The type of the third element
   * @param <T4> The type of the fourth element
   * @param <T5> The type of the fifth element
   * @return The [Tuple]
   */
  @Contract("_, _, _, _, _ -> new")
  public static <T1, T2, T3, T4, T5> @NotNull Quintet<T1, T2, T3, T4, T5> of(
      T1 first, T2 second, T3 third, T4 fourth, T5 fifth) {
    return new Quintet<>(first, second, third, fourth, fifth);
  }

  /**
   * The number of items in the tuple
   *
   * @return The number of items in the tuple
   */
  abstract int cardinality();

  /**
   * The n-th item in the tuple
   *
   * @param index the 0-based index that identifies the item position
   * @return {@link org.storynode.pigeon.option.Some} item at the given position or {@link
   *     org.storynode.pigeon.option.None} if item is found
   */
  abstract @NotNull Option<Object> at(int index);

  /**
   * Enumerates the items in this tuple
   *
   * @return An {@link java.lang.Iterable} of pairs each containing an item and its index
   */
  public Iterable<Pair<Object, Integer>> enumerate() {
    return new IterableForTuple(this);
  }

  /**
   * {@inheritDoc}
   *
   * <p>Checks whether the <code>other</code> instance is equal to <code>this</code>
   */
  @Override
  public boolean equals(Object other) {
    if (other == null || getClass() != other.getClass()) {
      return false;
    }

    Tuple otherTuple = (Tuple) other;

    if (this.cardinality() != otherTuple.cardinality()) {
      return false;
    }

    AtomicBoolean eq = new AtomicBoolean(true);

    for (Pair<Object, Integer> entry : this.enumerate()) {
      if (!entry.first().equals(otherTuple.at(entry.second()).unwrap())) {
        eq.set(false);
        break;
      }
    }

    return eq.get();
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    return super.hashCode();
  }

  private static class IterableForTuple implements Iterable<Pair<Object, Integer>> {
    private final @NotNull Tuple tuple;
    private int i = 0;

    public IterableForTuple(@NotNull Tuple tuple) {
      this.tuple = tuple;
    }

    @Override
    public @NotNull Iterator<Pair<Object, Integer>> iterator() {
      return new Iterator<>() {
        @Override
        public boolean hasNext() {
          return i < tuple.cardinality();
        }

        @Override
        public Pair<Object, Integer> next() {
          if (!this.hasNext()) {
            throw new NoSuchElementException();
          }

          int index = i;

          // We can unwrap because of the previous check
          Object item = tuple.at(index).unwrap();

          i += 1;
          return new Pair<>(item, index);
        }
      };
    }
  }
}
