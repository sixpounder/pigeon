package org.storynode.pigeon.option;

import static net.bytebuddy.matcher.ElementMatchers.none;
import static org.assertj.core.api.Assertions.assertThat;
import static org.storynode.pigeon.option.Option.some;

import org.junit.jupiter.api.Test;

public class SomeTest {
  @Test
  void equality() {
    assertThat(some(Double.valueOf("1"))).as("Some value").isEqualTo(some(Double.valueOf("1")));
    assertThat(some(1)).as("Some value").isNotEqualTo(some(2));
    assertThat(some(1)).as("Some value").isNotEqualTo(some(new Object()));
    assertThat(some(1)).as("Some value").isNotEqualTo(none());
  }
}
