package io.lbert.zio;

import io.lbert.Either;

import java.util.function.Function;
import java.util.function.Supplier;

abstract class ZIO<R, E, A> {

  abstract Tag getTag();

  public static <A> ZIO<Object, Nothing, A> succeed(A a) {
    return new Succeed<A>(a);
  }

  public static <E> ZIO<Object, E, Object> fail(E e) {
    return new Fail<E>(e);
  }

  public static <E, A> ZIO<Object, E, A> fromEither(Either<E,A> either) {
    return null;
//    return either.fold(l -> fail(l),r -> succeed(r));
  }

  public <B> ZIO<R, E, B> map(Function<A, B> mapping) {
    return new Map(this, mapping);
  }

  static class Map<R, E, A, B> extends ZIO<R, E, B> {

    final ZIO<R, E, A> zio;
    final Function<A, B> mapping;

    Map(ZIO<R, E, A> zio, Function<A, B> mapping) {
      this.zio = zio;
      this.mapping = mapping;
    }

    @Override
    Tag getTag() {
      return Tag.MAP;
    }
  }

  static class FlatMap<R, E, A, B> extends ZIO<R, E, B> {

    final ZIO<R, E, A> zio;
    final Function<A, ZIO<R, E, B>> mapping;

    FlatMap(ZIO<R, E, A> zio, Function<A, ZIO<R, E, B>> mapping) {
      this.zio = zio;
      this.mapping = mapping;
    }

    @Override
    Tag getTag() {
      return Tag.FLAT_MAP;
    }
  }

  static class Succeed<A> extends ZIO<Object, Nothing, A> {

    final A value;

    Succeed(A a) {
      value = a;
    }

    @Override
    Tag getTag() {
      return Tag.SUCCEED;
    }
  }

  static class EffectTotal<A> extends ZIO<Object, Nothing, A> {

    final Supplier<A> effect;

    EffectTotal(Supplier<A> effect) {
      this.effect = effect;
    }

    @Override
    Tag getTag() {
      return Tag.EFFECT_TOTAL;
    }
  }

  static class Fail<E> extends ZIO<Object, E, Object> {

    final E error;

    Fail(E error) {
      this.error = error;
    }

    @Override
    Tag getTag() {
      return Tag.FAIL;
    }
  }
}


interface Nothing {
}
