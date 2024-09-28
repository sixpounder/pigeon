package org.storynode.pigeon.result;

import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.error.UnwrapException;

/**
 * {@link Result} variant for ok values.
 *
 * @see Err
 * @see Result
 * @author Andrea Coronese
 */
public class Ok<T, E> extends Result<T, E> {
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
