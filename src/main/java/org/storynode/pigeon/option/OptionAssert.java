package org.storynode.pigeon.option;

import java.util.function.Function;
import org.assertj.core.api.AbstractAssert;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class OptionAssert<T> extends AbstractAssert<OptionAssert<T>, Option<T>> {

  @Contract("_ -> new")
  public static <T> @NotNull OptionAssert<T> assertThat(Option<T> actual) {
    return new OptionAssert<>(actual);
  }

  public OptionAssert(Option<T> actual) {
    super(actual, OptionAssert.class);
  }

  public OptionAssert<T> isEmpty() {
    isNotNull();
    if (actual.isSome()) {
      failWithMessage("Expected Option to be None but was Some(%s)", actual.unwrap());
    }

    return this;
  }

  public OptionAssert<T> isNotEmpty() {
    isNotNull();
    if (actual.isNone()) {
      failWithMessage("This Option to be Some but was None");
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
