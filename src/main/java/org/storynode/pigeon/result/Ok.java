package org.storynode.pigeon.result;

import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.protocol.Wrapped;

/**
 * Ok class.
 *
 * @author sixpounder
 */
public class Ok<T, E> extends Result<T, E> implements Wrapped<T> {
  private final T inner;

  /**
   * Constructor for Ok.
   *
   * @param inner a T object
   */
  protected Ok(@NotNull T inner) {
    this.inner = inner;
  }

  /** {@inheritDoc} */
  @Override
  public boolean isOk() {
    return true;
  }

  /** {@inheritDoc} */
  @Override
  public T unwrap() {
    return inner;
  }

  /** {@inheritDoc} */
  @Override
  public E unwrapError() throws UnwrapException {
    throw new UnwrapException("Cannot unwrap error on an Ok<> value");
  }
}
