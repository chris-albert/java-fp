package io.lbert.zio;

import io.lbert.Either;
import lombok.Value;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Try<A> {

  <B> Try<B> map(Function<A, B> func);
  <B> Try<B> flatMap(Function<A, Try<B>> func);
  Try<A> mapError(Function<RuntimeException, RuntimeException> func);
  <B> B fold(Function<RuntimeException, B> errorFunc, Function<A, B> goodFunc);
  Optional<A> toOption();
  Either<RuntimeException, A> toEither();

  boolean isSuccess();
  default boolean isFailure() {return !isSuccess();}

  static <A> Try<A> success(A a) {
    return Success.of(a);
  }

  static <A> Try<A> failure(RuntimeException t) {
    return Failure.of(t);
  }

  static <A> Try<A> of(Supplier<A> func) {
    try {
      return Success.of(func.get());
    } catch (RuntimeException t) {
      return Failure.of(t);
    }
  }

  @Value(staticConstructor = "of")
  class Success<A> implements Try<A> {

    private final A value;

    @Override
    public <B> Try<B> map(Function<A, B> func) {
      return Success.of(func.apply(value));
    }

    @Override
    public <B> Try<B> flatMap(Function<A, Try<B>> func) {
      return func.apply(value);
    }

    @Override
    public Try<A> mapError(Function<RuntimeException, RuntimeException> func) {
      return this;
    }

    @Override
    public <B> B fold(Function<RuntimeException, B> errorFunc, Function<A, B> goodFunc) {
      return goodFunc.apply(value);
    }

    @Override
    public Optional<A> toOption() {
      return Optional.of(value);
    }

    @Override
    public Either<RuntimeException, A> toEither() {
      return Either.right(value);
    }

    @Override
    public boolean isSuccess() {
      return true;
    }
  }

  @Value(staticConstructor = "of")
  class Failure<A> implements Try<A> {

    private final RuntimeException runtimeException;

    @Override
    @SuppressWarnings("unchecked")
    public <B> Try<B> map(Function<A, B> func) {
      return (Try<B>) this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <B> Try<B> flatMap(Function<A, Try<B>> func) {
      return (Try<B>) this;
    }

    @Override
    public Try<A> mapError(Function<RuntimeException, RuntimeException> func) {
      return Failure.of(func.apply(runtimeException));
    }

    @Override
    public <B> B fold(Function<RuntimeException, B> errorFunc, Function<A, B> goodFunc) {
      return errorFunc.apply(runtimeException);
    }

    @Override
    public Optional<A> toOption() {
      return Optional.empty();
    }

    @Override
    public Either<RuntimeException, A> toEither() {
      return Either.left(runtimeException);
    }

    @Override
    public boolean isSuccess() {
      return false;
    }
  }
}
