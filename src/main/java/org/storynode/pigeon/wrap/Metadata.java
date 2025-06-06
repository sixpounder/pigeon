package org.storynode.pigeon.wrap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.function.NeverThrow;
import org.storynode.pigeon.protocol.SafelyWrapped;
import org.storynode.pigeon.protocol.Wrapped;
import org.storynode.pigeon.result.Result;
import org.storynode.pigeon.tuple.Pair;
import org.storynode.pigeon.tuple.Tuple;

/**
 * Wraps a value associating it with arbitrary metadata
 *
 * @param <T> The type of the inner value
 * @author Andrea Coronese
 * @since 1.0.0
 */
public class Metadata<T> implements SafelyWrapped<T> {
  @Getter private final Map<Object, Object> enclosedMetadata;
  private final @NotNull T inner;

  /**
   * Tries to create a {@link org.storynode.pigeon.wrap.Metadata} instance from another {@link
   * Wrapped} value. Since {@link org.storynode.pigeon.protocol.Wrapped#unwrap()} can fail in some
   * implementations (like in {@link org.storynode.pigeon.result.Result}) this returns a {@link
   * org.storynode.pigeon.result.Result} containing the {@link
   * org.storynode.pigeon.error.UnwrapException} obtained when unwrapping the originally wrapped
   * value, if any is thrown. <br>
   * If the original {@link org.storynode.pigeon.protocol.Wrapped} is already a {@link
   * org.storynode.pigeon.wrap.Metadata} instance then its metadata will be copied into the new
   * instance.
   *
   * @param wrapped The source wrapped value
   * @param <T> The type of the value
   * @return A {@link org.storynode.pigeon.wrap.Metadata} instance with the original value that was
   *     {@link org.storynode.pigeon.protocol.Wrapped}
   */
  public static <T, E extends Throwable> @NotNull Result<Metadata<T>, E> from(
      @NotNull Wrapped<T> wrapped) {
    return Metadata.from(wrapped, Map.of());
  }

  /**
   * Tries to create a {@link org.storynode.pigeon.wrap.Metadata} instance from another {@link
   * Wrapped} value. Since {@link org.storynode.pigeon.protocol.Wrapped#unwrap()} can fail in some
   * implementations (like in {@link org.storynode.pigeon.result.Result}) this returns a {@link
   * org.storynode.pigeon.result.Result} containing the {@link
   * org.storynode.pigeon.error.UnwrapException} obtained when unwrapping the originally wrapped
   * value, if any is thrown. <br>
   * If the original {@link org.storynode.pigeon.protocol.Wrapped} is already a {@link
   * org.storynode.pigeon.wrap.Metadata} instance then its metadata will be copied into the new
   * instance.
   *
   * @param wrapped The source wrapped value
   * @param additionalMetadata Metadata to add
   * @param <T> The type of the value
   * @return A {@link org.storynode.pigeon.wrap.Metadata} instance with the original value that was
   *     {@link org.storynode.pigeon.protocol.Wrapped}
   */
  public static <T, E extends Throwable> @NotNull Result<Metadata<T>, E> from(
      @NotNull Wrapped<T> wrapped, Map<Object, Object> additionalMetadata) {
    return NeverThrow.executing(
        () -> {
          Map<Object, Object> metadata = new HashMap<>(additionalMetadata);

          if (wrapped instanceof Metadata<T> asMetadata) {
            metadata.putAll(asMetadata.getEnclosedMetadata());
          }

          metadata = Collections.unmodifiableMap(metadata);
          return new Metadata<>(wrapped.unwrap(), metadata);
        });
  }

  /**
   * Creates a {@link org.storynode.pigeon.wrap.Metadata} with a value and no metadata associated
   *
   * @param value The wrapped value
   */
  public Metadata(@NotNull T value) {
    this(value, Map.of());
  }

  /**
   * Creates a {@link org.storynode.pigeon.wrap.Metadata} with a value and associated <code>metadata
   * </code>
   *
   * @param value the wrapped value
   * @param metadata a {@link java.util.Map} object describing the initial metadata entries
   */
  public Metadata(@NotNull T value, Map<Object, Object> metadata) {
    this.inner = value;
    this.enclosedMetadata = Map.copyOf(metadata);
  }

  /** {@inheritDoc} */
  @Override
  public T unwrap() {
    return inner;
  }

  /**
   * A {@link org.storynode.pigeon.tuple.Pair} composed by the value and its metadata map
   *
   * @return a {@link org.storynode.pigeon.tuple.Pair} object with the value and the metadata
   */
  public Pair<T, Map<Object, Object>> toTuple() {
    return Tuple.of(this.unwrap(), Map.copyOf(this.getEnclosedMetadata()));
  }
}
