package org.storynode.pigeon.wrap;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.storynode.pigeon.result.Result;
import org.storynode.pigeon.tuple.Pair;

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
              assertThat(result.unwrap().getEnclosedMetadata()).as("Inner metadata").isEmpty();
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
              assertThat(result.unwrap().getEnclosedMetadata())
                  .as("Inner metadata")
                  .isNotEmpty()
                  .returns("value", m -> m.get("key"));
            });
  }

  @Test
  void unwrap() {
    Object innerValue = 1;
    assertThat(Metadata.from(new Metadata<>(innerValue), Map.of("key", "value")).unwrap())
        .as("Metadata")
        .returns(1, Metadata::unwrap);
  }

  @Test
  void toTuple() {
    Object innerValue = 1;
    Map<Object, Object> meta = Map.of("key", "value");
    assertThat(Metadata.from(new Metadata<>(innerValue, meta)).unwrap())
        .as("Metadata")
        .returns(Pair.of(1, meta), Metadata::toTuple);
  }

  @Test
  void getEnclosedMetadata() {
    Object innerValue = 1;
    Map<Object, Object> meta = Map.of("key", "value");
    assertThat(Metadata.from(new Metadata<>(innerValue, meta)).unwrap())
        .as("Metadata")
        .returns(meta, Metadata::getEnclosedMetadata);
  }
}
