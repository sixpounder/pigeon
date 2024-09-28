package org.storynode.pigeon.assertion;

import java.util.function.Function;
import org.assertj.core.api.AbstractAssert;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.option.Option;

public class OptionAssert<T> extends AbstractAssert<OptionAssert<T>, Option<T>> {

  public OptionAssert(Option<T> actual) {
    super(actual, OptionAssert.class);
  }

  public OptionAssert<T> isSome() {
    return isNotEmpty();
  }

  public OptionAssert<T> isNone() {
    return isEmpty();
  }

  public OptionAssert<T> isEmpty() {
    isNotNull();
    if (actual.isSome()) {
      failWithMessage("Expected to be None but was Some(%s)", actual.unwrap());
    }

    return this;
  }

  public OptionAssert<T> isNotEmpty() {
    isNotNull();
    if (actual.isNone()) {
      failWithMessage("Expected to be Some but was None");
    }

    return this;
  }

  public <U> OptionAssert<T> returns(U expected, @NotNull Function<Option<T>, U> provider) {
    var ret = provider.apply(actual);
    if (!ret.equals(expected)) {
      failWithMessage("Comparison failed: expected %s but got %s", expected, ret);
    }
    return this;
  }
}
