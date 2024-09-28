package org.storynode.pigeon.assertion;

import java.util.function.Function;
import org.assertj.core.api.AbstractAssert;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.Option;

/**
 * OptionAssert class.
 *
 * @author Andrea Coronese
 */
public class OptionAssert<T> extends AbstractAssert<OptionAssert<T>, Option<T>> {

  /**
   * Constructor for OptionAssert.
   *
   * @param actual a {@link org.storynode.pigeon.option.Option} object
   */
  public OptionAssert(Option<T> actual) {
    super(actual, OptionAssert.class);
  }

  /**
   * isSome.
   *
   * @return a {@link org.storynode.pigeon.assertion.OptionAssert} object
   */
  public OptionAssert<T> isSome() {
    return isNotEmpty();
  }

  /**
   * isNone.
   *
   * @return a {@link org.storynode.pigeon.assertion.OptionAssert} object
   */
  public OptionAssert<T> isNone() {
    return isEmpty();
  }

  /**
   * isEmpty.
   *
   * @return a {@link org.storynode.pigeon.assertion.OptionAssert} object
   */
  public OptionAssert<T> isEmpty() {
    isNotNull();
    if (actual.isSome()) {
      failWithMessage("Expected to be None but was Some(%s)", actual.unwrap());
    }

    return this;
  }

  /**
   * isNotEmpty.
   *
   * @return a {@link org.storynode.pigeon.assertion.OptionAssert} object
   */
  public OptionAssert<T> isNotEmpty() {
    isNotNull();
    if (actual.isNone()) {
      failWithMessage("Expected to be Some but was None");
    }

    return this;
  }

  /**
   * returns.
   *
   * @param expected a U object
   * @param provider a {@link java.util.function.Function} object
   * @param <U> a U class
   * @return a {@link org.storynode.pigeon.assertion.OptionAssert} object
   */
  public <U> OptionAssert<T> returns(U expected, @NotNull Function<Option<T>, U> provider) {
    var ret = provider.apply(actual);
    if (!ret.equals(expected)) {
      failWithMessage("Comparison failed: expected %s but got %s", expected, ret);
    }
    return this;
  }
}
