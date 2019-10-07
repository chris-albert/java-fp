package io.lbert;

import java.util.function.Function;

public interface Either<L, R> {

    <RR> Either<L, RR> map(Function<R, RR> func);
    <RR> Either<L, RR> flatMap(Function<R, Either<L, RR>> func);

    static <L, R> Either<L, R> right(R r) {
        return new Right<>(r);
    }

    static <L, R> Either<L, R> left(L l) {
        return new Left<>(l);
    }

    class Left<L, R> implements Either<L, R> {

        public final L left;

        private Left(L l) {
            this.left = l;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <RR> Either<L, RR> map(Function<R, RR> func) {
            return (Either<L, RR>) this;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <RR> Either<L, RR> flatMap(Function<R, Either<L, RR>> func) {
            return (Either<L, RR>) this;
        }
    }

    class Right<L, R> implements Either<L, R> {

        public final R right;

        private Right(R r) {
            this.right = r;
        }

        @Override
        public <RR> Either<L, RR> map(Function<R, RR> func) {
            return new Right<>(func.apply(right));
        }

        @Override
        public <RR> Either<L, RR> flatMap(Function<R, Either<L, RR>> func) {
            return func.apply(right);
        }
    }
}
