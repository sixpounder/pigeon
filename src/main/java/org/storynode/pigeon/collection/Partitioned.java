package org.storynode.pigeon.collection;

import static org.storynode.pigeon.result.Result.err;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.Option;
import org.storynode.pigeon.result.Result;

/**
 * A collection that automatically partition its values based on a partitioning function, designed
 * for performance.
 *
 * @param <K> The type of key returned by the partitioning function
 * @param <V> The type of values held in each partition. Values must be {@link Comparable} with each
 *     other.
 */
public class Partitioned<K, V extends Comparable<V>>
    implements Iterable<Partitioned.Partition<K, V>> {
  private final Map<K, Collection<V>> partitions;
  private final Function<V, K> partitioner;

  /**
   * Creates a new {@link Partitioned} with the given partitioning function
   *
   * @param partitioner The partitioning function to use
   */
  public Partitioned(Function<V, K> partitioner) {
    this(partitioner, null);
  }

  /**
   * Creates a new {@link Partitioned} with the given partitioning function and partition some
   * initial values in it
   *
   * @param partitioner The partitioning function
   * @param values The initial values to partition
   */
  public Partitioned(Function<V, K> partitioner, Collection<V> values) {
    this.partitions = new HashMap<>();
    this.partitioner = partitioner;
    if (values != null) {
      this.addAll(values);
    }
  }

  /**
   * The number of partitions included in this instance
   *
   * @return The number of partitions
   */
  public int size() {
    return partitions.size();
  }

  /**
   * Adds a value to its target partition
   *
   * @param value The value to add
   * @return <code>true</code> if this operation modifies the collection, <code>false</code>
   *     otherwise
   */
  public boolean add(V value) {
    K maybePartitionKey = partitioner.apply(value);
    Collection<V> partition = partitions.get(maybePartitionKey);
    if (partition == null) {
      partition = new TreeSet<>(Comparator.naturalOrder());
      partition.add(value);
      partitions.put(maybePartitionKey, partition);
      return true;
    } else if (partition.contains(value)) {
      return false;
    } else {
      partition.add(value);
      return true;
    }
  }

  /**
   * Adds multiple values to their target partitions
   *
   * @param values The values to add
   * @return <code>true</code> if this operation modifies the collection, <code>false</code>
   *     otherwise
   */
  public boolean addAll(@NotNull Collection<? extends V> values) {
    boolean changed = false;
    for (V v : values) {
      changed = add(v);
    }
    return changed;
  }

  /**
   * Whether this collection is empty or not
   *
   * @return <code>true</code> if the collection is empty, <code>false</code> otherwise
   */
  public boolean isEmpty() {
    return partitions.isEmpty();
  }

  /**
   * Checks if the collection contains a given key
   *
   * @param key They key to check for
   * @return <code>true</code> if they key is found, <code>false</code> otherwise
   */
  public boolean containsKey(K key) {
    return partitions.containsKey(key);
  }

  /**
   * Checks if any of the partitions contain a given value
   *
   * @param value They value to check for
   * @return <code>true</code> if the value is found, <code>false</code> otherwise
   */
  public boolean containsValue(V value) {
    return values().contains(value);
  }

  /**
   * Gets a given partition by key, if it exists. The outcome is wrapped in a {@link Result} in
   * order to handle potential runtime errors. For example, it can contain an error when a null key
   * is passed and the underlying storage doesn't allow for null keys. <br>
   * <br>
   * For an unchecked version of this, use {@link Partitioned#get}
   *
   * @param key The key of the partition to search for
   * @return A {@link Result} containing the found partition or an empty {@link Option} if not found
   */
  public @NotNull Result<Option<Collection<V>>, Throwable> tryGet(K key) {
    return Result.of(() -> Option.of(partitions.get(key)));
  }

  /**
   * Like {@link Partitioned#tryGet(Object)}, but can return <code>null</code> and throw if
   * something is not right.
   *
   * @param key The key of the partition to search for
   * @return the found partition or <code>null</code> if not found
   */
  public Collection<V> get(K key) {
    return partitions.get(key);
  }

  /**
   * Tries to remove a partition from this collection
   *
   * @param key The key of the partition to remove
   * @return a {@link Result} containing an {@link Option} of the removed partition if it was found,
   *     empty otherwise.
   */
  @Contract(mutates = "this")
  public @NotNull Result<Option<Collection<V>>, Throwable> tryRemove(K key) {
    return Result.of(() -> Option.of(partitions.remove(key)));
  }

  /**
   * Tries to remove a value from the partition that contains it
   *
   * @param value The value to remove
   * @return a {@link Result} containing an {@link Option} of the removed value if it was found,
   *     empty otherwise.
   */
  @Contract(mutates = "this")
  public @NotNull Result<Option<V>, Throwable> tryRemoveValue(V value) {
    if (value == null) {
      return err(new NullPointerException("value cannot be null"));
    }

    return tryGet(partitioner.apply(value))
        .map(
            maybePartition -> {
              if (maybePartition.isSome()) {
                return Option.of(maybePartition.unwrap().remove(value) ? value : null);
              } else {
                return Option.none();
              }
            });
  }

  /** Deletes all partitions */
  @Contract(mutates = "this")
  public void clear() {
    partitions.clear();
  }

  /**
   * Gets all they partition keys
   *
   * @return A set containing the partition keys
   */
  public @NotNull Set<K> keySet() {
    return partitions.keySet();
  }

  public @NotNull Collection<V> values() {
    return partitions.values().stream()
        .flatMap(Collection::stream)
        .collect(TreeSet::new, TreeSet::add, TreeSet::addAll);
  }

  public @NotNull Set<Partition<K, V>> entrySet() {
    return partitions.entrySet().stream()
        .map(e -> new Partition<>(e.getKey(), e.getValue()))
        .collect(Collectors.toSet());
  }

  /**
   * Alias for {@link Partitioned#containsValue}
   *
   * @param value The value to check for
   * @return <code>true</code> if the value is found, <code>false</code> otherwise
   */
  public boolean contains(V value) {
    return containsValue(value);
  }

  /**
   * Iterates over the partitions of this instance
   *
   * @return An iterator on the partitions of this instance
   */
  @Override
  public @NotNull Iterator<Partitioned.Partition<K, V>> iterator() {
    return new PartitionIterator<>(this);
  }

  @Contract("_, _ -> new")
  public static <K, V extends Comparable<V>> @NotNull Partitioned<K, V> by(
      Function<V, K> partitioner, Collection<V> values) {
    return new Partitioned<>(partitioner, values);
  }

  @Contract("_ -> new")
  public static <K, V extends Comparable<V>> @NotNull Partitioned<K, V> by(
      Function<V, K> partitioner) {
    return by(partitioner, null);
  }

  public record Partition<K, V>(K key, Collection<V> values) {}

  public static class PartitionIterator<K, V extends Comparable<V>>
      implements Iterator<Partitioned.Partition<K, V>> {
    private final Iterator<K> keysIterator;
    private final Partitioned<K, V> owner;

    protected PartitionIterator(@NotNull Partitioned<K, V> owner) {
      this.owner = owner;
      this.keysIterator = owner.keySet().iterator();
    }

    @Override
    public boolean hasNext() {
      return keysIterator.hasNext();
    }

    @Override
    public Partitioned.Partition<K, V> next() {
      K nextKey = keysIterator.next();
      return new Partition<>(nextKey, owner.get(nextKey));
    }
  }
}
