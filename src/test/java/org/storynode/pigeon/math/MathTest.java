package org.storynode.pigeon.math;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MathTest {

  @Test
  void clamp_int() {
    assertThat(Math.clamp(23, 0, 100)).as("Clamped int").isEqualTo(23);
    assertThat(Math.clamp(-12, 0, 100)).as("Clamped int").isEqualTo(0);
    assertThat(Math.clamp(124, 0, 100)).as("Clamped int").isEqualTo(100);
    assertThat(Math.clamp(0, 0, 100)).as("Clamped int").isEqualTo(0);
    assertThat(Math.clamp(100, 0, 100)).as("Clamped int").isEqualTo(100);
  }

  @Test
  void clamp_double() {
    assertThat(Math.clamp(23d, 0d, 100d)).as("Clamped int").isEqualTo(23);
    assertThat(Math.clamp(-12d, 0d, 100d)).as("Clamped int").isEqualTo(0);
    assertThat(Math.clamp(124d, 0d, 100d)).as("Clamped int").isEqualTo(100);
    assertThat(Math.clamp(0d, 0d, 100d)).as("Clamped int").isEqualTo(0);
    assertThat(Math.clamp(100d, 0d, 100d)).as("Clamped int").isEqualTo(100);
  }

  @Test
  void clamp_float() {
    assertThat(Math.clamp(23.0, 0.0, 100.2)).as("Clamped int").isEqualTo(23.0);
    assertThat(Math.clamp(-12.12, 0.98, 100.12)).as("Clamped int").isEqualTo(0.98);
    assertThat(Math.clamp(124.1, 0.0, 100.1)).as("Clamped int").isEqualTo(100.1);
    assertThat(Math.clamp(0.0, 0.0, 100.0)).as("Clamped int").isEqualTo(0.0);
    assertThat(Math.clamp(100.0, 0.1, 100.0)).as("Clamped int").isEqualTo(100.0);
  }
}
