package org.storynode.pigeon.math;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MathTest {

  @Test
  void clamp_int() {
    assertThat(Math.clamp(23, 0, 100)).as("Clamped int").isEqualTo(23);
    assertThat(Math.clamp(-12, 0, 100)).as("Clamped int").isZero();
    assertThat(Math.clamp(124, 0, 100)).as("Clamped int").isEqualTo(100);
    assertThat(Math.clamp(0, 0, 100)).as("Clamped int").isZero();
    assertThat(Math.clamp(100, 0, 100)).as("Clamped int").isEqualTo(100);
  }

  @Test
  void clamp_double() {
    assertThat(Math.clamp(23d, 0d, 100d)).as("Clamped double").isEqualTo(23);
    assertThat(Math.clamp(-12d, 0d, 100d)).as("Clamped double").isZero();
    assertThat(Math.clamp(124d, 0d, 100d)).as("Clamped double").isEqualTo(100);
    assertThat(Math.clamp(0d, 0d, 100d)).as("Clamped double").isZero();
    assertThat(Math.clamp(100d, 0d, 100d)).as("Clamped double").isEqualTo(100);
  }

  @Test
  void clamp_float() {
    assertThat(Math.clamp(23.0f, 0.0f, 100.2f)).as("Clamped float").isEqualTo(23.0f);
    assertThat(Math.clamp(-12.12f, 0.98f, 100.12f)).as("Clamped float").isEqualTo(0.98f);
    assertThat(Math.clamp(124.1f, 0.0f, 100.1f)).as("Clamped float").isEqualTo(100.1f);
    assertThat(Math.clamp(0.0f, 0.0f, 100.0f)).as("Clamped float").isZero();
    assertThat(Math.clamp(100.0f, 0.1f, 100.0f)).as("Clamped float").isEqualTo(100.0f);
  }
}
