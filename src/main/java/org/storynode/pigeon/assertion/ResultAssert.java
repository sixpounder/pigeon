package org.storynode.pigeon.assertion;

import java.util.function.Function;
import org.assertj.core.api.AbstractAssert;
import org.jetbrains.annotations.NotNull;
import org.storynode.pigeon.result.Result;

/**
 * ResultAssert class.
 *
 * @author Andrea Coronese
 */
public class ResultAssert<T, E> extends AbstractAssert<ResultAssert<T, E>, Result<T, E>> {

  /**
   * Constructor for ResultAssert.
   *
   * @param teResult a {@link org.storynode.pigeon.result.Result} object
   */
  public ResultAssert(Result<T, E> teResult) {
    super(teResult, ResultAssert.class);
  }

  /**
   * Asserts that the variant of the {@link Result} is {@link org.storynode.pigeon.result.Ok}
   *
   * @return a {@link org.storynode.pigeon.assertion.ResultAssert} object
   */
  public ResultAssert<T, E> isOk() {
    isNotNull();
    if (!actual.isOk()) {
      failWithMessage("Expected to be Ok() but was Err(%s)", actual.unwrapError());
    }
    return this;
  }

  /**
   * Asserts that the variant of the {@link Result} is {@link org.storynode.pigeon.result.Ok}
   *
   * @return a {@link org.storynode.pigeon.assertion.ResultAssert} object
   */
  public ResultAssert<T, E> unwraps(T value) {
    isNotNull();
    isOk();
    if (!actual.unwrap().equals(value)) {
      failWithMessage("Expected unwrapped value to be %s but was %s", value, actual.unwrap());
    }
    return this;
  }

  /**
   * Asserts that the variant of the {@link Result} is {@link org.storynode.pigeon.result.Err}
   *
   * @return a {@link org.storynode.pigeon.assertion.ResultAssert} object
   */
  public ResultAssert<T, E> isError() {
    isNotNull();
    if (!actual.isErr()) {
      failWithMessage("Expected to be Err() but was %s", actual);
    }
    return this;
  }

  /**
   * returns.
   *
   * @param expected a U object
   * @param provider a {@link java.util.function.Function} object
   * @param <U> a U class
   * @return a {@link org.storynode.pigeon.assertion.ResultAssert} object
   */
  public <U> ResultAssert<T, E> returns(U expected, @NotNull Function<Result<T, E>, U> provider) {
    var ret = provider.apply(actual);
    if (!ret.equals(expected)) {
      failWithMessage("Comparison failed: expected %s but got %s", expected, ret);
    }
    return this;
  }
}
