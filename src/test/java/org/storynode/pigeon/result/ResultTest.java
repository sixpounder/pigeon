package org.storynode.pigeon.result;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.storynode.pigeon.assertion.Assertions.assertThat;

import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.storynode.pigeon.error.UnwrapException;
import org.storynode.pigeon.tuple.Tuple;

class ResultTest {

  @Test
  void ok() {
    Result<Integer, Object> obj = Result.ok(1);
    assertThat(obj.isOk()).as("State").isTrue();
    assertThat(obj.isErr()).as("State").isFalse();
    assertThat(obj.unwrap()).isEqualTo(1);
    assertThatExceptionOfType(UnwrapException.class).isThrownBy(obj::unwrapError);
  }

  @Test
  void error() {
    Result<Object, String> obj = Result.error("Nope");
    assertThat(obj.isOk()).as("State").isFalse();
    assertThat(obj.isErr()).as("State").isTrue();
    assertThat(obj.unwrapError()).isEqualTo("Nope");
    assertThatExceptionOfType(UnwrapException.class).isThrownBy(obj::unwrap);
  }

  @Test
  void of_static() {
    assertThat(Result.of(() -> "Hello world"))
        .as("Result from supplier")
        .isEqualTo(Result.ok("Hello world"));
    assertThat(
            Result.of(
                () -> {
                  int[] numbers = {1, 2, 3};
                  return numbers[(int) Instant.now().toEpochMilli()];
                }))
        .as("Result from throwing supplier")
        .returns(true, Result::isErr);
  }

  @Test
  void of_throwing() {
    assertThat(Result.of(DummyThrower::buggedMethod))
        .as("Result from throwing supplier")
        .isError()
        .returns(IOException.class, r -> r.unwrapError().getClass());
  }

  @Test
  void map() {
    Result<Double, Object> obj = Result.ok(3.14D);
    Result<Double, Object> mapped = obj.map(v -> Math.pow(v, 2D));
    assertThat(mapped).as("Mapped value").isOk();
    assertThat(mapped.unwrap()).as("Mapped inner value").isEqualTo(Math.pow(3.14D, 2D));

    assertThat(Result.ok(Math.PI).mapError(pi -> "The PI!"))
        .as("Result::map with value")
        .returns(Math.PI, Result::unwrap);
  }

  @Test
  void mapError() {
    Result<Double, Double> obj = Result.error(3.14D);
    Result<Double, Double> mapped = obj.mapError(v -> Math.pow(v, 2D));
    assertThat(mapped).as("Mapped value").isError();
    assertThat(mapped.unwrapError()).as("Mapped inner value").isEqualTo(Math.pow(3.14D, 2D));

    assertThat(Result.error("THE ERROR").map(v -> "THE VALUE"))
        .as("Result::map with error")
        .returns("THE ERROR", Result::unwrapError);
  }

  @Test
  void orElse() {
    assertThat(Result.ok(Math.PI).orElse(1D)).as("Extracted value with default").isEqualTo(Math.PI);
    assertThat(Result.error(Math.PI).orElse(1D)).as("Extracted value with default").isEqualTo(1D);
  }

  @Test
  void orElseGet() {
    assertThat(Result.ok(Math.PI).orElseGet(() -> 1D))
        .as("Extracted value with default")
        .isEqualTo(Math.PI);
    assertThat(Result.error(Math.PI).orElseGet(() -> 1D))
        .as("Extracted value with default")
        .isEqualTo(1D);
  }

  @Test
  void isOkAnd() {
    assertThat(Result.ok("Hello world").isOkAnd(v -> v.length() == 11))
        .as("Composited predicate on value")
        .isTrue();
    assertThat(Result.error("Hello world").isOkAnd(v -> true))
        .as("Composited predicate on value when error")
        .isFalse();
  }

  @Test
  void isErrAnd() {
    assertThat(Result.error("Hello world").isErrAnd(v -> v.length() == 11))
        .as("Composited predicate on error")
        .isTrue();
    assertThat(Result.ok("Hello world").isErrAnd(v -> true))
        .as("Composited predicate on error when ok")
        .isFalse();
  }

  @Test
  void and() {
    assertThat(Result.ok(1).and(Result.err("late error"))).isEqualTo(Result.err("late error"));
    assertThat(Result.err("early error").and(Result.ok("foo")))
        .isEqualTo(Result.err("early error"));
    assertThat(Result.err("not a 1").and(Result.err("late error")))
        .isEqualTo(Result.err("not a 1"));
    assertThat(Result.ok(1).and(Result.ok(2))).isEqualTo(Result.ok(2));
  }

  @Test
  void andThen() {
    assertThat(Result.ok(2).andThen(v -> Result.ok(v * v))).isEqualTo(Result.ok(4));
  }

  @Test
  void ifOkOrElse() {
    AtomicInteger ok = new AtomicInteger(0);
    Result.ok(2).ifOkOrElse(ok::set, err -> ok.set(-1));
    assertThat(ok.get()).as("Probe value").isEqualTo(2);

    Result.err(new IllegalArgumentException()).ifOkOrElse(v -> ok.set(2), err -> ok.set(-1));
    assertThat(ok.get()).as("Probe value").isEqualTo(-1);
  }

  @Test
  void toTuple() {
    assertThat(Result.ok(1).toTuple()).as("Tuple").isEqualTo(Tuple.of(1, null));
    assertThat(Result.err(1).toTuple()).as("Tuple").isEqualTo(Tuple.of(null, 1));
  }

  @Test
  void equality() {
    assertThat(Result.ok("Hello")).as("Result").isEqualTo(Result.ok("Hello"));
    assertThat(Result.ok("Hello")).as("Result").isNotEqualTo(Result.ok("World"));
    assertThat(Result.ok("Hello"))
        .as("Result")
        .isNotEqualTo(Result.err(new IllegalArgumentException("Ops")));
    assertThat(Result.ok("Hello")).as("Result").isNotEqualTo(null);
  }

  @Test
  void testHashCode() {
    assertThat(Result.ok(1).hashCode()).as("Result hashcode").isNotZero();
    assertThat(Result.err(new IllegalArgumentException("Ops")).hashCode())
        .as("Result hashcode")
        .isNotZero();
  }

  private static class DummyThrower {
    public static Object buggedMethod() throws IOException {
      throw new IOException("Some IO error occurred here");
    }
  }
}
