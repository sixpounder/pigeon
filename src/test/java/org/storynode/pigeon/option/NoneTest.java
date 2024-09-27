package org.storynode.pigeon.option;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NoneTest {
  @Test
  void testEquals() {
    assertThat(None.none()).isEqualTo(None.none());
  }
}
