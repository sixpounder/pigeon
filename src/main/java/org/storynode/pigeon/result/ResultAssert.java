package org.storynode.pigeon.result;

import java.util.function.Function;
import org.assertj.core.api.AbstractAssert;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ResultAssert<T, E> extends AbstractAssert<ResultAssert<T, E>, Result<T, E>> {
  @Contract("_ -> new")
  public static <T, E> @NotNull ResultAssert<T, E> assertThat(Result<T, E> actual) {
    return new ResultAssert<>(actual);
  }

  public ResultAssert(Result<T, E> teResult) {
    super(teResult, ResultAssert.class);
  }

  public ResultAssert<T, E> isOk() {
    isNotNull();
    if (!actual.isOk()) {
      failWithMessage("Expected to be Ok() but was Err(%s)", actual.unwrapError());
    }
    return this;
  }

  public ResultAssert<T, E> isError() {
    if (!actual.isError()) {
      failWithMessage("Expected to be Err() but was Ok(%s)", actual.unwrap());
    }
    return this;
  }

  public <U> ResultAssert<T, E> returns(U expected, @NotNull Function<Result<T, E>, U> provider) {
    var ret = provider.apply(actual);
    if (!ret.equals(expected)) {
      failWithMessage("Comparison failed: expected %s but got %s", expected, ret);
    }
    return this;
  }
}
