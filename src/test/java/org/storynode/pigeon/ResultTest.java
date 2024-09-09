package org.storynode.pigeon;

import org.junit.jupiter.api.Test;
import org.storynode.pigeon.error.UnwrapException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ResultTest {

  @Test
  void ok() {
    Result<Integer, Object> obj = Result.ok(1);
    assertThat(obj.isOk()).as("State").isTrue();
    assertThat(obj.isError()).as("State").isFalse();
    assertThat(obj.unwrap()).isEqualTo(1);
    assertThatExceptionOfType(UnwrapException.class).isThrownBy(obj::unwrapError);
    assertThat(obj.tryUnwrap()).as("Optionally unwrapped").isNotEmpty();
    assertThat(obj.tryUnwrapError()).as("Optionally unwrapped").isEmpty();
  }

  @Test
  void error() {
    Result<Object, String> obj = Result.error("Nope");
    assertThat(obj.isOk()).as("State").isFalse();
    assertThat(obj.isError()).as("State").isTrue();
    assertThat(obj.unwrapError()).isEqualTo("Nope");
    assertThatExceptionOfType(UnwrapException.class).isThrownBy(obj::unwrap);
    assertThat(obj.tryUnwrap()).as("Optionally unwrapped").isEmpty();
    assertThat(obj.tryUnwrapError()).as("Optionally unwrapped").isNotEmpty();
  }

  @Test
  void map() {
    Result<Double, Object> obj = Result.ok(3.14D);
    Result<Double, Object> mapped = obj.map(v -> Math.pow(v, 2D));
    assertThat(mapped).as("Mapped value").satisfies(Result::isOk);
    assertThat(mapped.unwrap()).as("Mapped inner value").isEqualTo(Math.pow(3.14D, 2D));

    assertThat(Result.ok(Math.PI).mapError(pi -> "The PI!")).as("Result::map with value").returns(Math.PI, Result::unwrap);
  }

  @Test
  void mapError() {
    Result<Double, Double> obj = Result.error(3.14D);
    Result<Double, Double> mapped = obj.mapError(v -> Math.pow(v, 2D));
    assertThat(mapped).as("Mapped value").satisfies(Result::isError);
    assertThat(mapped.unwrapError()).as("Mapped inner value").isEqualTo(Math.pow(3.14D, 2D));

    assertThat(Result.error("THE ERROR").map(v -> "THE VALUE")).as("Result::map with error").returns("THE ERROR", Result::unwrapError);
  }

  @Test
  void tryUnwraps() {
    Result<Double, Object> obj = Result.ok(3.14D);
    assertThat(obj.tryUnwrap()).as("Optionally unwrapped").isNotNull();
    assertThat(obj.tryUnwrapError()).as("Optionally unwrapped error").isNotNull();

    Result<Double, Object> obj2 = Result.error(3.14D);
    assertThat(obj.tryUnwrap()).as("Optionally unwrapped").isNotNull();
    assertThat(obj.tryUnwrapError()).as("Optionally unwrapped error").isNotNull();
  }

  @Test
  void orElse() {
    assertThat(Result.ok(Math.PI).orElse(1D)).as("Extracted value with default").isEqualTo(Math.PI);
    assertThat(Result.error(Math.PI).orElse(1D)).as("Extracted value with default").isEqualTo(1D);
  }

  @Test
  void orElseGet() {
    assertThat(Result.ok(Math.PI).orElseGet(() -> 1D)).as("Extracted value with default").isEqualTo(Math.PI);
    assertThat(Result.error(Math.PI).orElseGet(() -> 1D)).as("Extracted value with default").isEqualTo(1D);
  }
}
