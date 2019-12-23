package io.lbert.zio;

import io.lbert.Either;
import lombok.Value;

import java.util.function.Function;
import java.util.function.Supplier;

abstract class ZIO<R, E, A> {

  abstract Tag getTag();

  public static <A, E> ZIO<Object, E, A> succeed(A a) {
    return Succeed.of(a);
  }

  public static <E, A> ZIO<Object, E, A> fail(E e) {
    return Fail.of(e);
  }

  public static <E, A> ZIO<Object, E, A> fromEither(Either<E, A> either) {
    return either.fold(
        l -> (ZIO<Object, E, A>) fail(l),
        r -> (ZIO<Object, E, A>) succeed(r)
    );
  }

  public static <A> ZIO<Object, Throwable, A> effect(Supplier<A> func) {
    return new EffectTotal<>(func);
  }

  public <B> ZIO<R, E, B> map(Function<A, B> mapping) {
    return Map.of(this, mapping);
  }

  public <B> ZIO<R, E, B> flatMap(Function<A, ZIO<R, E, B>> mapping) {
    return FlatMap.of(this, mapping);
  }

  @Value(staticConstructor = "of")
  static class Map<R, E, A, B> extends ZIO<R, E, B> {

    ZIO<R, E, A> zio;
    Function<A, B> mapping;

    @Override
    Tag getTag() {
      return Tag.MAP;
    }
  }

  @Value(staticConstructor = "of")
  static class FlatMap<R, E, A, B> extends ZIO<R, E, B> {

    ZIO<R, E, A> zio;
    Function<A, ZIO<R, E, B>> mapping;

    @Override
    Tag getTag() {
      return Tag.FLAT_MAP;
    }
  }

  @Value(staticConstructor = "of")
  static class Succeed<A, E> extends ZIO<Object, E, A> {

    A value;

    @Override
    Tag getTag() {
      return Tag.SUCCEED;
    }
  }

  @Value(staticConstructor = "of")
  static class EffectTotal<A> extends ZIO<Object, Throwable, A> {

    Supplier<A> effect;

    @Override
    Tag getTag() {
      return Tag.EFFECT_TOTAL;
    }
  }

  @Value(staticConstructor = "of")
  static class Fail<E, A> extends ZIO<Object, E, A> {

    E error;

    @Override
    Tag getTag() {
      return Tag.FAIL;
    }
  }
}


interface Nothing {
}
