package org.storynode.pigeon.protocol;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.BiFunction;
import org.junit.jupiter.api.Test;

class ThrowingSupplierTest {

  @Test
  void getWithException_returnsValue() throws Exception {
    ThrowingSupplier<String> supplier = () -> "test";
    assertEquals("test", supplier.getWithException());
  }

  @Test
  void get_returnsValue() {
    ThrowingSupplier<String> supplier = () -> "hello";
    assertEquals("hello", supplier.get());
  }

  @Test
  void get_throwsRuntimeExceptionDirectly() {
    ThrowingSupplier<String> supplier =
        () -> {
          throw new IllegalStateException("fail");
        };
    IllegalStateException thrown = assertThrows(IllegalStateException.class, supplier::get);
    assertEquals("fail", thrown.getMessage());
  }

  @Test
  void get_wrapsCheckedExceptionWithDefaultWrapper() {
    ThrowingSupplier<String> supplier =
        () -> {
          throw new Exception("checked!");
        };
    RuntimeException thrown = assertThrows(RuntimeException.class, supplier::get);
    assertEquals("checked!", thrown.getMessage());
    assertInstanceOf(Exception.class, thrown.getCause());
    assertEquals("checked!", thrown.getCause().getMessage());
  }

  @Test
  void get_withCustomWrapper() {
    ThrowingSupplier<String> supplier =
        () -> {
          throw new Exception("oops");
        };
    BiFunction<String, Exception, RuntimeException> wrapper =
        (msg, ex) -> new IllegalArgumentException("wrapped: " + msg, ex);
    IllegalArgumentException thrown =
        assertThrows(IllegalArgumentException.class, () -> supplier.get(wrapper));
    assertEquals("wrapped: oops", thrown.getMessage());
    assertInstanceOf(Exception.class, thrown.getCause());
  }

  @Test
  void get_withNullExceptionMessage() {
    ThrowingSupplier<String> supplier =
        () -> {
          throw new Exception();
        };
    BiFunction<String, Exception, RuntimeException> wrapper = RuntimeException::new;
    RuntimeException thrown = assertThrows(RuntimeException.class, () -> supplier.get(wrapper));
    assertNull(thrown.getMessage());
    assertInstanceOf(Exception.class, thrown.getCause());
  }
}
