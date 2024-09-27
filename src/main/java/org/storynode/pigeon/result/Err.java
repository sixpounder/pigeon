package org.storynode.pigeon.result;

import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;

/**
 * Err class.
 *
 * @author sixpounder
 */
public class Err<T, E> extends Result<T, E> {
  private final E inner;

  /**
   * Constructor for Err.
   *
   * @param inner a E object
   */
  public Err(@NotNull E inner) {
    this.inner = inner;
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
    return inner;
  }
}
