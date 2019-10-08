package io.lbert.zio;


import io.lbert.Either;
import io.lbert.zio.ZIO;

public class Runtime {

    public static <R, E, A> Either<E, A> attempt(ZIO<R, E, A> zio) {

        Either<E, A> result = null;
        switch(zio.getTag()) {
            case SUCCEED:
                ZIO.Succeed<A> succeed = (ZIO.Succeed<A>) zio;
                result = Either.right(succeed.value);
                break;
            case FAIL:
                ZIO.Fail<E> fail = (ZIO.Fail<E>) zio;
                result = Either.left(fail.error);
                break;
        }


        return result;
    }
}
