package org.storynode.pigeon.wrap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Metadata<T> implements Wrapped<T> {
  @Getter private final Map<String, Object> metadata;
  private final @NotNull T inner;

  public Metadata(@NotNull Wrapped<T> wrapped) {
    this.inner = wrapped.unwrap();
    if (wrapped instanceof Metadata<T> asMetadata) {
      this.metadata = asMetadata.getMetadata();
    } else {
      this.metadata = Map.of();
    }
  }

  public Metadata(@NotNull Wrapped<T> wrapped, Map<String, Object> additionalMetadata) {
    Map<String, Object> metadata = new HashMap<>(additionalMetadata);
    if (wrapped instanceof Metadata<T> asMetadata) {
      metadata.putAll(asMetadata.getMetadata());
    }

    this.metadata = Collections.unmodifiableMap(metadata);
    this.inner = wrapped.unwrap();
  }

  public Metadata(@NotNull T value) {
    this(value, Map.of());
  }

  public Metadata(@NotNull T value, Map<String, Object> metadata) {
    this.inner = value;
    this.metadata = Map.copyOf(metadata);
  }

  @Override
  public T unwrap() {
    return inner;
  }
}
