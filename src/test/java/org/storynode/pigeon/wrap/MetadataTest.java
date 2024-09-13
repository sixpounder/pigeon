package org.storynode.pigeon.wrap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;

class MetadataTest {

  @Test
  void fromWrapped() {
    Object innerValue = 1;
    assertThat(Metadata.from(new Metadata<>(innerValue)))
        .as("Metadata")
        .returns(true, Result::isOk)
        .satisfies(
            result -> {
              assertThat(result.unwrap().unwrap()).as("Inner value").isEqualTo(innerValue);
              assertThat(result.unwrap().getMetadata()).as("Inner metadata").isEmpty();
            });
  }

  @Test
  void fromWrappedAndAdditionalMetadata() {
    Object innerValue = 1;
    assertThat(Metadata.from(new Metadata<>(innerValue), Map.of("key", "value")))
        .as("Metadata")
        .returns(true, Result::isOk)
        .satisfies(
            result -> {
              assertThat(result.unwrap().unwrap()).as("Inner value").isEqualTo(innerValue);
              assertThat(result.unwrap().getMetadata())
                  .as("Inner metadata")
                  .isNotEmpty()
                  .returns("value", m -> m.get("key"));
            });
  }

  @Test
  void unwrap() {}

  @Test
  void toTuple() {}

  @Test
  void getMetadata() {}
}
