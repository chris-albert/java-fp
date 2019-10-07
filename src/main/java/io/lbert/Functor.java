package io.lbert;

import java.util.function.Function;

public interface Functor<A, F extends Functor<?, ?>> {
    public <B> F map(Function<A, B> mapper);
}

class Identity<A> implements Functor<A, Identity<?>> {

    private final A value;

    Identity(A value) { this.value = value; }

    @Override
    public <B> Identity<?> map(Function<A, B> mapper) {
        return new Identity<>(mapper.apply(value));
    }
}