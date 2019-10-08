package io.lbert.zio;

import java.util.function.Function;
import java.util.function.Supplier;

abstract class ZIO<R, E, A> {

    abstract Tag getTag();

    public static <A> UIO<A> succeed(A a) {
        return new Succeed<A>(a);
    }

    public static <E> IO<E, Object> fail(E e) {
        return new Fail<E>(e);
    }

    public <B> ZIO<R, E, B> map(Function<A, B> mapping) {

        Function<A, B> f = a -> mapping.apply(a);
        Function<A, ZIO<R, E, B>> zm = a -> {
            ZIO<Object, Nothing, B> r = succeed(mapping.apply(a));
            UIO<B> rr = succeed(mapping.apply(a));
            ZIO<R, E, B> re = (ZIO<R, E, B>) succeed(mapping.apply(a));
            return null;
        };
//        new FlatMap<R, E, A, B>(this, a -> succeed(mapping.apply(a)));
        return null;
    }


    static class FlatMap<R, E, A, B> extends ZIO<R, E, B>{

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

    static class Succeed<A> extends UIO<A> {

        final A value;

        Succeed(A a) {
            value = a;
        }

        @Override
        Tag getTag() {
            return Tag.SUCCEED;
        }
    }

    static class EffectTotal<A> extends UIO<A> {

        final Supplier<A> effect;

        EffectTotal(Supplier<A> effect) {
            this.effect = effect;
        }

        @Override
        Tag getTag() {
            return Tag.EFFECT_TOTAL;
        }
    }

    static class Fail<E> extends IO<E, Object> {

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

abstract class IO<E, A> extends ZIO<Object, E, A> {}
abstract class Task<A> extends ZIO<Object, Throwable, A> {}
abstract class UIO<A> extends ZIO<Object, Nothing, A> {}

interface Nothing {}
