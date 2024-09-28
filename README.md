# Pigeon

**Pigeon** is a Java library that provides a safer, exception-free approach to error handling. It leverages *never-throw* oriented types, such as `Result` and `Option`, to manage success and failure explicitly, inspired by functional programming paradigms. With Pigeon, errors are no longer hidden behind exceptions, promoting cleaner, more maintainable code by making error handling a core part of the type system.

## Features

- **Never-throw philosophy:** Avoids traditional exception handling in favor of `Result` and `Option` types, ensuring all outcomes are explicitly handled.
- **Core types:**
    - `Result<T, E>`: Represents an operation's success (`Ok<T>`) or failure (`Err<E>`).
    - `Option<T>`: Encapsulates a value (`Some<T>`) or the absence of one (`None`).
- **Utility types**:
    - `Metadata<T>`: Decorates a type with metadata
- **Chainable API:** Methods are designed to be easily composed and chained, leading to concise and expressive code.
- **Explicit error handling:** Prevents silent failures and ensures all potential errors are addressed at compile time.
- **Functional approach:** Aligns with functional programming practices, encouraging pure functions and immutability.

## Installation

To use Pigeon in your project, add the following dependency to your `pom.xml` (for Maven):

```xml
<dependency>
    <groupId>org.storynode</groupId>
    <artifactId>pigeon</artifactId>
    <version>0.1.0</version>
</dependency>
```