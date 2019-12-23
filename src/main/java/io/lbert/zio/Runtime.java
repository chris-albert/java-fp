package io.lbert.zio;


import io.lbert.Either;
import io.lbert.zio.ZIO;

public class Runtime {

  public static <R, E, A, B> Either<E, B> attempt(ZIO<R, E, A> zio) {

    Either<E, B> result = null;
    switch (zio.getTag()) {
      case SUCCEED:
        ZIO.Succeed<A> succeed = (ZIO.Succeed<A>) zio;
        result = (Either<E, B>) Either.right(succeed.value);
        break;
      case FAIL:
        ZIO.Fail<E> fail = (ZIO.Fail<E>) zio;
        result = Either.left(fail.error);
        break;
      case MAP:
        ZIO.Map<R, E, A, B> map = (ZIO.Map<R, E, A, B>) zio;
        Either<E, A> res = attempt(map.zio);
        result = res.rMap(map.mapping);
        break;
    }


    return result;
  }
}
